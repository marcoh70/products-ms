package com.app.ed.products.controller;

import com.app.ed.products.model.ProductRequest;
import com.app.ed.products.service.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/products/v1")
public class ProductsController {

    private final ProductsService productsService;

    @PostMapping(path = "/async")
    public ResponseEntity<String> createProductEventAsync(@RequestBody ProductRequest productRequest) {
        String productId= productsService.createProductEventAsync(productRequest);
        return ResponseEntity.ok(productId);
    }

    @PostMapping(path = "/sync")
    public ResponseEntity<String> createProductEventSync(@RequestBody ProductRequest productRequest) {
        String productId= productsService.createProductEventSync(productRequest);
        return ResponseEntity.ok(productId);
    }



}
