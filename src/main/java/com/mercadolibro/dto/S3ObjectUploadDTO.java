package com.mercadolibro.dto;

import lombok.*;

import java.io.ByteArrayInputStream;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class S3ObjectUploadDTO {
    private String key;
    private ByteArrayInputStream content;
}