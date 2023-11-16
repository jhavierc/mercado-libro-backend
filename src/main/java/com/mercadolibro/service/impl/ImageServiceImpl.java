package com.mercadolibro.service.impl;

import com.mercadolibro.dto.BookImageReqPatchDTO;
import com.mercadolibro.dto.S3ObjectDTO;
import com.mercadolibro.entity.Image;
import com.mercadolibro.exception.ImageNotFoundException;
import com.mercadolibro.repository.ImageRepository;
import com.mercadolibro.service.ImageService;
import com.mercadolibro.service.S3Service;
import com.mercadolibro.util.S3Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final S3Service s3Service;

    public static final String IMAGE_TO_UPDATE_NOT_FOUND_ERROR_FORMAT = "Could not found image to update with ID #%d.";
    public static final String IMAGES_TO_DELETE_NOT_FOUND = "Some images to delete could not be found.";


    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository, S3Service s3Service) {
        this.imageRepository = imageRepository;
        this.s3Service = s3Service;
    }

    @Override
    public Image save(MultipartFile file) {
        S3ObjectDTO uploadedObject = s3Service.uploadFile(file);

        Image image = new Image();
        image.setUrl(uploadedObject.getUrl());
        return imageRepository.save(image);
    }


    @Override
    public List<Image> updateAll(List<Long> existingIds, List<MultipartFile> newFiles) {
        List<Image> updatedImages = new ArrayList<>();
        deleteAll(existingIds);
        for (MultipartFile file : newFiles) {
            Image savedImage = save(file);
            updatedImages.add(savedImage);
        }
        return updatedImages;
    }

    @Override
    @Transactional
    public List<Image> patch(List<BookImageReqPatchDTO> toUpdate) {
        List<MultipartFile> files = new ArrayList<>();
        List<String> urls = new ArrayList<>();
        List<Image> existingImages = new ArrayList<>();

        for (BookImageReqPatchDTO dto : toUpdate) {
            Optional<Image> existingImageOptional = imageRepository.findById(dto.getId());
            if (existingImageOptional.isPresent()) {
                Image existingImage = existingImageOptional.get();
                files.add(dto.getImage());
                urls.add(existingImage.getUrl());
                existingImages.add(existingImageOptional.get());
            } else {
                throw new ImageNotFoundException(String.format(IMAGE_TO_UPDATE_NOT_FOUND_ERROR_FORMAT, dto.getId()));
            }
        }

        List<S3ObjectDTO> uploadedObjects = s3Service.replaceFilesByURLs(files, urls);

        List<Image> updatedImages = new ArrayList<>();
        for (int i = 0; i < toUpdate.size(); i++) {
            Image existingImage = existingImages.get(i);
            existingImage.setUrl(uploadedObjects.get(i).getUrl());
            Image updatedImage = imageRepository.save(existingImage);
            updatedImages.add(updatedImage);
        }

        return updatedImages;
    }

    @Override
    public void deleteAll(List<Long> ids) {
        List<Image> existingImages = imageRepository.findAllById(ids);
        if (existingImages.size() != ids.size()) {
            throw new ImageNotFoundException(IMAGES_TO_DELETE_NOT_FOUND);
        }
        List<S3ObjectDTO> s3ObjectDTOS = new ArrayList<>();
        existingImages.forEach(image -> s3ObjectDTOS.add(S3Util.parseS3Url(image.getUrl())));
        s3Service.deleteFiles(s3ObjectDTOS);
        imageRepository.deleteAll(existingImages);
    }
}