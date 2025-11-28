package com.app.ed.products.model;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class ProductRequest {

    private String title;
    private BigDecimal price;
    private Integer quantity;

}
