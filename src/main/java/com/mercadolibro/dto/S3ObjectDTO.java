package com.mercadolibro.dto;

import lombok.*;

import java.io.ByteArrayInputStream;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class S3ObjectDTO {
    private String name;
    private ByteArrayInputStream content;
}