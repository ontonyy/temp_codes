package ee.taltech.iti0202.store.strategies;

import ee.taltech.iti0202.store.products.Product;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CheapStrategy implements Strategy {

    @Override
    public List<Product> sortProducts(List<Product> products) {
        products = products.stream().sorted(Comparator.comparing(Product::getPrice)).collect(Collectors.toList());
        List<Product> removeProducts = new LinkedList<>();
        for (int i = 0; i < products.size(); i++) {
            for (int j = i + 1; j < products.size(); j++) {
                if (products.get(i).getName().equals(products.get(j).getName())
                        && products.get(i).getType() == products.get(j).getType()) {
                    removeProducts.add(products.get(j));
                }
            }
        }
        products.removeAll(removeProducts);
        return products;
    }
}
