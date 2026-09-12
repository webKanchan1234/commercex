package com.commercex.product.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BrandResponse {

    private UUID id;

    private String name;

    private String slug;

    private String description;

    private Boolean active;
}