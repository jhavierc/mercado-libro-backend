package com.mercadolibro.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class S3ObjectRespDTO {
    private String path;
    private String url;
    private String bucket;
}
