package com.commercex.product.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBrandRequest {

    @Size(max = 100)
    private String name;

    @Size(max = 120)
    private String slug;

    @Size(max = 500)
    private String description;

    private Boolean active;
}