package com.mercadolibro.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
public class ImageDTO {

    private Long id;
    private String url;
    private String name;
}
