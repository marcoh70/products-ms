package com.app.ed.products.service;

import com.app.ed.products.model.ProductRequest;

public interface ProductsService {

    String createProductEventAsync(ProductRequest productRequest);
    String createProductEventSync(ProductRequest productRequest);
}
