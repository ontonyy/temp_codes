package ee.taltech.iti0202.store.strategies;

import ee.taltech.iti0202.store.products.Product;

import java.util.List;

public interface Strategy {
    List<Product> sortProducts(List<Product> products);
}
