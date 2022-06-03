package ee.taltech.iti0202.store.strategies;

import ee.taltech.iti0202.store.Client;
import ee.taltech.iti0202.store.exceptions.client.ClientNegativeBudgetException;
import ee.taltech.iti0202.store.exceptions.client.ClientUnderageException;
import ee.taltech.iti0202.store.exceptions.product.ProductAlreadyUsingException;
import ee.taltech.iti0202.store.exceptions.product.ProductNegativePriceException;
import ee.taltech.iti0202.store.exceptions.product.ProductNotFoundException;
import ee.taltech.iti0202.store.exceptions.store.StoreCannotHoldProductTypeException;
import ee.taltech.iti0202.store.exceptions.store.StoreEmptyProductsException;
import ee.taltech.iti0202.store.exceptions.strategies.StrategyNotFoundException;
import ee.taltech.iti0202.store.products.Product;
import ee.taltech.iti0202.store.products.ProductType;
import ee.taltech.iti0202.store.stores.DepartmentStore;
import ee.taltech.iti0202.store.stores.Store;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DifferentStrategyTest {
    private final int budget = 1000;
    private final int budget2 = 740;
    private final int profit1 = 260;
    private final int age = 19;
    private final int price1 = 400;
    private final int price2 = 200;
    private final int price3 = 50;
    private final int price4 = 10;
    private final int price5 = 100;

    Strategy strategy = new DifferentStrategy();
    Store store = new DepartmentStore("DEPO", 1);
    Client client = new Client("Kirill", age, budget);
    Product product1 = new Product("TV", price1, ProductType.TECHNICAL);
    Product product2 = new Product("Rex", price2, ProductType.TOYS);
    Product product3 = new Product("Apple", price3, ProductType.FOOD);
    Product product4 = new Product("Huawei", price4, ProductType.TECHNICAL);
    Product product5 = new Product("Egg", price5, ProductType.FOOD);

    DifferentStrategyTest() throws ClientUnderageException, ClientNegativeBudgetException,
            ProductNegativePriceException, StoreCannotHoldProductTypeException, ProductAlreadyUsingException {
        store.addProduct(product1);
        store.addProduct(product2);
        store.addProduct(product3);
        store.addProduct(product4);
        store.addProduct(product5);
    }

    @Test
    void sortProducts() throws StoreEmptyProductsException, ProductNotFoundException, StrategyNotFoundException {
        assertEquals(0, client.getProducts().size());
        assertEquals(1000, client.getBudget());
        client.setStrategy(strategy);
        client.followStrategy(store);

        // Client will have only 3 products, because of 3 different types of products
        assertEquals(3, client.getProducts().size());
        assertEquals(budget2, client.getBudget());
        assertEquals(profit1, store.getProfit());

        // In order expensive TV was first, but strategy choose cheapest Technical product
        for (Product product : client.getProducts()) {
            if (product.getType() == ProductType.TECHNICAL) {
                assertEquals(product4, product);
            }
        }
    }
}
