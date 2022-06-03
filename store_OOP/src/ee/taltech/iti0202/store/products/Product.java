package ee.taltech.iti0202.store.products;

import ee.taltech.iti0202.store.exceptions.product.ProductNegativePriceException;
import ee.taltech.iti0202.store.stores.Store;

import static ee.taltech.iti0202.store.Constants.BONUS_PRICE_COEFFICIENT;

public class Product {
    private String name;
    private Store holdStore;
    private int id;
    private static int count = 0;
    private int price;
    private int bonusPrice;
    private ProductType type;

    public Product(String name, int price, ProductType type) throws ProductNegativePriceException {
        this.id = ++count;
        this.name = name;
        this.type = type;
        setPrice(price);
        this.bonusPrice = price * BONUS_PRICE_COEFFICIENT;
    }

    public int getId() {
        return id;
    }

    public Store getHoldStore() {
        return holdStore;
    }

    public void setHoldStore(Store holdStore) {
        this.holdStore = holdStore;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) throws ProductNegativePriceException {
        if (price > 0) {
            this.price = price;
        } else {
            throw new ProductNegativePriceException();
        }
    }

    public ProductType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBonusPrice() {
        return bonusPrice;
    }
}
