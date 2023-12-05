package com.mercadolibro.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibro.dto.ImageDTO;
import com.mercadolibro.dto.ResponseDTO;
import com.mercadolibro.entity.Book;
import com.mercadolibro.entity.BookImage;
import com.mercadolibro.repository.BookImageRepository;
import com.mercadolibro.repository.BookRepository;
import com.mercadolibro.service.BookImageService;
import com.mercadolibro.util.Constants;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
public class BookImageServiceImpl implements BookImageService {

    private final BookImageRepository bookImageRepository;

    private final BookRepository bookRepository;

    private final AmazonS3 amazonS3Client;

    @Value("${aws.s3.name}")
    private  String bucketName;

    private Logger LOG = Logger.getLogger(BookImageServiceImpl.class.getSimpleName());

    public BookImageServiceImpl(BookImageRepository bookImageRepository,
                                BookRepository bookRepository,
                                ObjectMapper mapper,
                                AmazonS3 amazonS3){
        this.bookImageRepository = bookImageRepository;
        this.bookRepository = bookRepository;
        this.amazonS3Client=amazonS3;
    }

    @Override
    public List<ImageDTO> findByBookID(Long bookID) {
        Optional<List<BookImage>> listOptional = bookImageRepository.getByBookId(bookID);
        if(listOptional.isPresent()){
            List<ImageDTO> list = new ArrayList<>();
            listOptional.get().forEach(bookImages -> {
                ImageDTO imageDTO = new ImageDTO();
                imageDTO.setId(bookImages.getId());
                imageDTO.setName(bookImages.getName());
                imageDTO.setUrl(bookImages.getUrl());
                list.add(imageDTO);
            });
            return list;
        }
        return null;
    }


    @Override
    public ResponseDTO save(MultipartFile multipartFile, Long bookID) throws InternalError {ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setCode(Constants.CodeResponse.ERROR.getCodigo());
        try {

            Optional<Book> optionalBook = bookRepository.findById(bookID);
            if (!optionalBook.isPresent()) {
                responseDTO.setMessage(String.format(Constants.PRODUCT_NOT_EXIST, bookID));
                return responseDTO;
            }

            if (multipartFile.getBytes() == null || multipartFile.getSize() == 0) {
                responseDTO.setMessage(Constants.FILE_CONTENT_ERROR);
                return responseDTO;
            }

            URL s3Url = amazonS3Client.getUrl(bucketName, multipartFile.getName());

            UUID uuid = UUID.randomUUID();

            StringBuilder path = new StringBuilder();
            path.append(s3Url.getProtocol()).append("://").append(s3Url.getHost()).append("/")
                    .append(uuid);

            BookImage bookImage = new BookImage();
            bookImage.setBook(optionalBook.get());
            bookImage.setName(uuid.toString());
            bookImage.setUrl(path.toString());

            bookImageRepository.save(bookImage);

            putObjectInS3(this.bucketName, bookImage.getName(), multipartFile.getBytes());

            responseDTO.setCode(Constants.CodeResponse.OK.getCodigo());
            responseDTO.setMessage(Constants.FILE_CONTENT_SUCCESS);

        } catch (Exception e) {
            responseDTO.setMessage(e.getMessage());
        }
        return responseDTO;
    }

    @Override
    public ResponseDTO delete(Long imageID) throws InternalError {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setCode(Constants.CodeResponse.ERROR.getCodigo());
        try {
            Optional<BookImage> optionalImage = bookImageRepository.findById(imageID);
            if (!optionalImage.isPresent()) {
                responseDTO.setMessage(String.format(Constants.IMAGE_NOT_EXIST, imageID));
                return responseDTO;
            }
            deleteObjectInS3(bucketName, optionalImage.get().getName());
            bookImageRepository.delete(optionalImage.get());
            responseDTO.setCode(Constants.CodeResponse.OK.getCodigo());
            responseDTO.setMessage(Constants.FILE_CONTENT_DELETE);
        } catch (Exception e) {
            throw new InternalError(Constants.GENERIC_ERROR_MSJ);
        }
        return responseDTO;
    }

    private void putObjectInS3(String bucketName, String name, byte[] content) throws InternalError {
        try {
            File file = new File(name);
            FileUtils.writeByteArrayToFile(file, content);

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, name, file)
                    .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3Client.putObject(putObjectRequest);
            file.delete();

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error to upload file in S3, {0}", e.getCause());
            throw new InternalError(Constants.GENERIC_ERROR_MSJ);
        }
    }

    private void deleteObjectInS3(String bucketName, String fileNname) throws InternalError {
        try {
            String objkeyArr[] = { fileNname };
            DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName).withKeys(objkeyArr);
            amazonS3Client.deleteObjects(deleteObjectsRequest);

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error to upload file in S3, {0}", e.getCause());
            throw new InternalError(Constants.GENERIC_ERROR_MSJ);
        }
    }
}
