package com.mercadolibro.service;

import com.mercadolibro.dto.BookImageReqPatchDTO;
import com.mercadolibro.entity.Image;
import com.mercadolibro.exception.ResourceNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {

    /**
     * Save a new image
     *
     * @param file the file to save
     * @return the saved image
     */
    Image save(MultipartFile file);

    /**
     * Update all images
     *
     * @param existingIds ids of the existing images
     * @param newFiles new files to replace the existing ones
     * @return the updated images
     */
    List<Image> updateAll(List<Long> existingIds, List<MultipartFile> newFiles);

    /**
     * Patch images
     *
     * @param toUpdate images to update
     * @return the updated images
     * @throws ResourceNotFoundException if an image to update or delete is not found
     */
    List<Image> patch(List<BookImageReqPatchDTO> toUpdate);

    /**
     * Delete all images
     *
     * @param ids ids of images to delete
     */
    void deleteAll(List<Long> ids);
}