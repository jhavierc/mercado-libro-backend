package com.mercadolibro.controller;

import com.mercadolibro.dto.BookRespDTO;
import com.mercadolibro.dto.ImageDTO;
import com.mercadolibro.dto.ResponseDTO;
import com.mercadolibro.service.BookImageService;
import com.mercadolibro.util.Constants;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/book-image")
public class BookImageController {

    private final BookImageService bookImageService;

    @Autowired
    public BookImageController(BookImageService bookImageService) {
        this.bookImageService = bookImageService;
    }

    @PostMapping("/{id}")
    @ApiOperation(value = "Upload image to a book", notes = "Returns the created book")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "Image upload successfully", response = BookRespDTO.class),
                    @ApiResponse(code = 400, message = "Bad request"),
                    @ApiResponse(code = 409, message = "Book not exists"),
                    @ApiResponse(code = 500, message = "Internal server error"),
            }
    )
    public ResponseEntity<ResponseDTO> upload(@PathVariable("id") Long bookID, @RequestParam("file") MultipartFile file) {
        ResponseDTO responseDTO = bookImageService.save(file,bookID);
        try {
            if (responseDTO.getCode().equals(Constants.CodeResponse.ERROR.getCodigo())) {
                if(responseDTO.getMessage().contains("book")){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get all images to a book", notes = "Returns the list images")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "List images", response = BookRespDTO.class),
                    @ApiResponse(code = 204, message = "Not content"),
            }
    )
    public ResponseEntity<List<ImageDTO>> getFilesByProductID(@PathVariable("id") Long bookID) {
        List<ImageDTO> list = bookImageService.findByBookID(bookID);
        if(list==null){
            ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(list);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete image to a book", notes = "Delete image to a book")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "If delete image is success", response = BookRespDTO.class),
                    @ApiResponse(code = 400, message = "Bad request"),
            }
    )
    public ResponseEntity<ResponseDTO> deleteFile(@PathVariable("id") Long imageID) throws InternalError {
        ResponseDTO responseDTO = bookImageService.delete(imageID);
        if (responseDTO.getCode().equals(Constants.CodeResponse.ERROR.getCodigo())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }


}
