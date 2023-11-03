package com.mercadolibro.dto;

import lombok.*;

import java.io.ByteArrayInputStream;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class S3ObjectReqDTO {
    private String name;
    private ByteArrayInputStream content;
}