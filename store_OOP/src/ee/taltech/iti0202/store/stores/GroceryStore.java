package ee.taltech.iti0202.store.stores;

import ee.taltech.iti0202.store.exceptions.product.ProductAlreadyUsingException;
import ee.taltech.iti0202.store.exceptions.product.ProductNegativePriceException;
import ee.taltech.iti0202.store.exceptions.store.StoreCannotHoldProductTypeException;
import ee.taltech.iti0202.store.products.Product;
import ee.taltech.iti0202.store.products.ProductType;

public class GroceryStore extends Store {

    public GroceryStore(String name, int vipAmount) {
        super(name, StoreType.GROCERY, vipAmount);
    }

    /*
    Can add only product with type Food, else exception
     */
    @Override
    public void addProduct(Product product) throws ProductAlreadyUsingException,
            ProductNegativePriceException, StoreCannotHoldProductTypeException {
        if (product.getType() == ProductType.FOOD) {
            super.addProduct(product);
        } else {
            throw new StoreCannotHoldProductTypeException();
        }
    }
}
