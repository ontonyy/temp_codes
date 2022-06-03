package ee.taltech.iti0202.store.stores;

import ee.taltech.iti0202.store.Client;
import ee.taltech.iti0202.store.exceptions.product.ProductAlreadyUsingException;
import ee.taltech.iti0202.store.exceptions.product.ProductNegativePriceException;
import ee.taltech.iti0202.store.exceptions.product.ProductNotFoundException;
import ee.taltech.iti0202.store.exceptions.store.StoreCannotHoldProductTypeException;
import ee.taltech.iti0202.store.products.Product;

public class DepartmentStore extends Store {

    public DepartmentStore(String name, int vipAmount) {
        super(name, StoreType.DEPARTMENT, vipAmount);
    }

    public void returnProduct(Client client, Product product)
            throws StoreCannotHoldProductTypeException,
            ProductNegativePriceException, ProductNotFoundException, ProductAlreadyUsingException {
        if (client.getProducts().contains(product)) {
            client.removeProduct(product);
            client.addBudget(product.getPrice());
            reduceProfit(product.getPrice());
            product.setHoldStore(null);
            addProduct(product);
        } else {
            throw new ProductNotFoundException();
        }
    }
}
