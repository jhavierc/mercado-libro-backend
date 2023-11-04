package com.mercadolibro.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class S3ObjectDTO {
    private String key;
    private String url;
    private String bucket;
}