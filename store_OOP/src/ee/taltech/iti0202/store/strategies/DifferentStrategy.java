package ee.taltech.iti0202.store.strategies;

import ee.taltech.iti0202.store.products.Product;
import ee.taltech.iti0202.store.products.ProductType;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class DifferentStrategy implements Strategy {

    @Override
    public List<Product> sortProducts(List<Product> products) {
        Map<ProductType, Product> productMap = new LinkedHashMap<>();
        for (Product product : products) {
            if (!productMap.containsKey(product.getType())
                    || productMap.get(product.getType()).getPrice() > product.getPrice()) {
                productMap.put(product.getType(), product);
            }
        }
        return new LinkedList<>(productMap.values()).stream().sorted(Comparator.comparing(Product::getPrice))
                .collect(Collectors.toList());
    }
}
