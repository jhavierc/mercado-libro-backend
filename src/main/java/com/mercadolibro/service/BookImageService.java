package com.mercadolibro.service;

import com.mercadolibro.dto.ImageDTO;
import com.mercadolibro.dto.ResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookImageService {

    /**
     * Find list images related to book
     *
     * @param bookID
     * @return Optional<List<ImageDTO>>
     */
    public List<ImageDTO> findByBookID(Long bookID);

    /**
     * Save image related to a book
     *
     * @param multipartFile file
     * @param bookID book identifier
     * @return ResponseDTO
     * @throws InternalError
     */
    public ResponseDTO save(MultipartFile multipartFile, Long bookID) throws InternalError;

    /**
     * Delete image by id related to a book
     *
     * @param imageID image identifier
     * @return ResponseDTO
     * @throws InternalError
     */
    public ResponseDTO delete(Long imageID) throws InternalError;
}
