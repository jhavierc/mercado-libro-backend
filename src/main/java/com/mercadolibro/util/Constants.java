package com.mercadolibro.util;

public class Constants {

    public static final String PRODUCT_NOT_EXIST ="The book with id %d does not exist";
    public static final String FILE_CONTENT_ERROR ="File content is invalid";
    public static final String FILE_CONTENT_SUCCESS ="The file was uploaded successfully";
    public static final String FILE_CONTENT_DELETE ="The file was deleted successfully";

    public static final String IMAGE_NOT_EXIST ="Image with id %d does not exist";

    public static final String GENERIC_ERROR_MSJ = "An error occurred while saving information, please try again. If the problem persists, consult your administrator.";

    public enum CodeResponse {
        OK("OK"),
        ERROR("ERROR");

        private final String codigo;

        private CodeResponse(String codigo) {
            this.codigo = codigo;
        }

        public String getCodigo() {
            return codigo;
        }
    }

}
