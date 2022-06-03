package ee.taltech.iti0202.store.strategies;

import ee.taltech.iti0202.store.Client;
import ee.taltech.iti0202.store.products.Product;
import ee.taltech.iti0202.store.products.ProductType;
import ee.taltech.iti0202.store.stores.Store;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FavoriteTypesStrategy implements Strategy {
    private Client client;
    private Store store;

    public FavoriteTypesStrategy(Client client, Store store) {
        this.client = client;
        this.store = store;
    }

    @Override
    public List<Product> sortProducts(List<Product> products) {
        Map<ProductType, List<Product>> productMap = new LinkedHashMap<>();
        for (ProductType favType : client.getFavoriteProductTypes()) {
            for (Product product : products) {
                ProductType type = product.getType();
                String name = product.getName();
                List<Product> mapProducts = productMap.get(type);
                if (type == favType && !client.containsProduct(name, type)) {
                    if (!productMap.containsKey(product.getType())) {
                        productMap.put(type, store.getProductsByName(name));
                    } else {
                        if (!containsProduct(productMap.get(type), name)) {
                            if (store.countProducts(mapProducts.get(0).getName())
                                    < store.countProducts(name)) {
                                mapProducts.addAll(0, store.getProductsByName(name));
                            } else {
                                mapProducts.addAll(store.getProductsByName(name));
                            }
                        }
                    }
                }
            }
        }
        List<Product> favoriteProducts = new LinkedList<>();
        productMap.values().forEach(favoriteProducts::addAll);
        return favoriteProducts;
    }

    public boolean containsProduct(List<Product> products, String name) {
        for (Product product : products) {
            if (product.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
