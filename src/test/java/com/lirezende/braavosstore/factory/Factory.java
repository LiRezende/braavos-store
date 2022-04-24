package com.lirezende.braavosstore.factory;

import com.lirezende.braavosstore.dto.ProductDTO;
import com.lirezende.braavosstore.entities.Category;
import com.lirezende.braavosstore.entities.Product;

import java.time.Instant;

public class Factory {

    public static Product createProduct() {
        Product product = new Product(1L, "Phone", "Xangrilá", 800.00, "https://www.lg.com/br/celulares/lg-lmk420bmw-cinza#none", Instant.parse("2020-07-13T20:50:07.12345Z"));
        product.getCategories().add(new Category(2L, "Eletrônicos"));
        return product;
    }

    public static ProductDTO createProductDTO() {
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }
}
