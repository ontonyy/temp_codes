package ee.taltech.iti0202.store.shop_components;

import ee.taltech.iti0202.store.products.Product;

import java.util.LinkedList;
import java.util.List;

public class ShoppingBag {
    private List<Product> products = new LinkedList<>();

    public void devastate() {
        products.clear();
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public int getTotalPrice() {
        int total = 0;
        for (Product product : products) {
            total += product.getPrice();
        }
        return total;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }
}
