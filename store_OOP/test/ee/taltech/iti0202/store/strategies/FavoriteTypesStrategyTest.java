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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class FavoriteTypesStrategyTest {
    private final int budget = 1000;
    private final int age = 19;
    private final int price1 = 300;
    private final int price2 = 200;
    private final int price3 = 50;
    private final int price4 = 10;
    private final int price5 = 1000;
    private final int size1 = 7;
    private final int size2 = 6;
    private final int budget1 = 90;

    Store store = new DepartmentStore("DEPO", 1);
    Client client = new Client("Kirill", age, budget);
    Strategy strategy = new FavoriteTypesStrategy(client, store);
    Product product1 = new Product("TV", price1, ProductType.TECHNICAL);
    Product product2 = new Product("Phone", price1, ProductType.TECHNICAL);
    Product product3 = new Product("Raadio", price5, ProductType.TECHNICAL);
    Product product4 = new Product("TV", price3, ProductType.TECHNICAL);
    Product product5 = new Product("Rex", price2, ProductType.TOYS);
    Product product6 = new Product("Penguin", price4, ProductType.TOYS);
    Product product7 = new Product("Apple", price3, ProductType.FOOD);

    FavoriteTypesStrategyTest() throws ClientUnderageException, ClientNegativeBudgetException,
            ProductNegativePriceException, StoreCannotHoldProductTypeException, ProductAlreadyUsingException {
        store.addProduct(product1);
        store.addProduct(product2);
        store.addProduct(product3);
        store.addProduct(product4);
        store.addProduct(product5);
        store.addProduct(product6);
        store.addProduct(product7);
    }

    @Test
    void sortProducts() throws StoreEmptyProductsException, ProductNotFoundException, StrategyNotFoundException {
        assertEquals(0, client.getProducts().size());
        assertEquals(size1, store.getProducts().size());
        assertEquals(1000, client.getBudget());
        client.setStrategy(strategy);
        client.setFavoriteProductTypes(List.of(ProductType.FOOD, ProductType.TECHNICAL, ProductType.TOYS));
        client.followStrategy(store);

        // Client by all products that have his favorite type, but because of budget cannot buy expensive Raadio
        assertEquals(size2, client.getProducts().size());
        assertFalse(client.getProducts().contains(product3));
        assertEquals(budget1, client.getBudget());

        assertEquals(1, store.getProducts().size());

        // Firstly was buy Food products, then Technical and then toys
        assertEquals(product7, client.getProducts().get(0));

        assertEquals(product1, client.getProducts().get(1));

        assertEquals(product6, client.getProducts().get(client.getProducts().size() - 1));
    }
}
