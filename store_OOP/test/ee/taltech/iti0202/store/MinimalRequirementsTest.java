package ee.taltech.iti0202.store;

import ee.taltech.iti0202.store.exceptions.client.ClientNegativeBudgetException;
import ee.taltech.iti0202.store.exceptions.client.ClientNotEnoughMoneyOrBonusesException;
import ee.taltech.iti0202.store.exceptions.client.ClientUnderageException;
import ee.taltech.iti0202.store.exceptions.product.ProductAlreadyUsingException;
import ee.taltech.iti0202.store.exceptions.product.ProductNegativePriceException;
import ee.taltech.iti0202.store.exceptions.product.ProductNotFoundException;
import ee.taltech.iti0202.store.exceptions.store.StoreCannotHoldProductTypeException;
import ee.taltech.iti0202.store.exceptions.store.StoreEmptyClientsException;
import ee.taltech.iti0202.store.products.Product;
import ee.taltech.iti0202.store.products.ProductType;
import ee.taltech.iti0202.store.stores.DepartmentStore;
import ee.taltech.iti0202.store.stores.GroceryStore;
import ee.taltech.iti0202.store.stores.Store;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MinimalRequirementsTest {
    private final int age1 = 19;
    private final int age2 = 18;
    private final int age3 = 45;
    private final int age4 = 14;
    private final int price1 = 500;
    private final int price2 = 1500;
    private final int price3 = 3000;
    private final int price4 = 200;
    private final int price5 = 50;
    private final int price6 = 800;
    private final int budget1 = 2400;
    private final int id1 = 8;

    Store store = new GroceryStore("Euronics", 3);
    Store store2 = new DepartmentStore("Onoff", 3);
    Client client = new Client("John", age1, 1000);

    Product product1 = new Product("Apple", price1, ProductType.FOOD);
    Product product2 = new Product("Banana", 100, ProductType.FOOD);
    Product product3 = new Product("Scooter", price2, ProductType.FOOD);
    Product product4 = new Product("TV", price3, ProductType.FOOD);
    Product product5 = new Product("Banana", price4, ProductType.FOOD);
    Product product6 = new Product("Watermelon", price4, ProductType.FOOD);
    Product product7 = new Product("Watermelon", price5, ProductType.FOOD);

    public MinimalRequirementsTest() throws ProductNegativePriceException,
            ClientUnderageException, ClientNegativeBudgetException {
    }

    @Test
    void createSimpleSystem() throws StoreCannotHoldProductTypeException, ProductAlreadyUsingException,
            ProductNegativePriceException, ProductNotFoundException,
            ClientNotEnoughMoneyOrBonusesException, StoreEmptyClientsException {
        assertEquals("Euronics", store.getName());
        assertEquals(0, store.getProfit()); // in start, profit always 0
        assertEquals(0, store.getProducts().size());

        assertEquals("Apple", product1.getName());
        assertEquals(price1, product1.getPrice());
//        assertEquals(1, product1.getId()); // because of static id if run all tests will failed,
//         but if alone then passes

        assertEquals(0, store.getProducts().size());
        store.addProduct(product1);
        store.addProduct(product2);
        assertEquals(2, store.getProducts().size());
        store.removeProduct(product2);
        assertEquals(1, store.getProducts().size());

        store2.addProduct(product3);

        // If product is using in some store, then cannot be added to another store
        assertThrows(ProductAlreadyUsingException.class, () -> store.addProduct(product3));
        assertThrows(ProductAlreadyUsingException.class, () -> store2.addProduct(product1));

        store.addProduct(product6);
        store.addProduct(product7);
        store.addProduct(product4);
        store.addProduct(product5);
        // Find products by different parameters
//        assertEquals(product1, store.findProductById(1)); // if run alone then passes, because of static id parameter
        assertEquals(product6, store.findProductByName("Watermelon").get(0));
        assertEquals(2, store.findProductByName("Watermelon").size()); // founded 2 products with this name
        assertEquals(product4, store.findProductByPrice(price3).get(0));

        assertThrows(ClientNotEnoughMoneyOrBonusesException.class, () -> store.buyProduct(client, product4));

        assertEquals(1000, client.getBudget());
        store.buyProduct(client, product5);
        assertEquals(1, client.getProducts().size());
        assertEquals(1, store.getClients().size());

        assertEquals(price6, client.getBudget());
        assertEquals(price4, store.getProfit());

        store.buyProduct(client, product1);
        // If client already in store, he is not added
        assertEquals(1, store.getClients().size());


        assertEquals("John", client.getName());
        assertEquals(age1, client.getAge());
    }

    @Test
    void wrongComponents() throws ClientNegativeBudgetException,
            ClientUnderageException, ProductAlreadyUsingException, ProductNegativePriceException,
            StoreCannotHoldProductTypeException {
        Client client1 = new Client("John", age2, price1);
        Client client2 = new Client("Maxim", age3, budget1);
        assertThrows(ClientNegativeBudgetException.class, () -> new Client("John", age2, -price1));
        assertThrows(ClientUnderageException.class, () -> new Client("Feduk", age4, price1));
        assertThrows(ProductNegativePriceException.class, () -> new Product("TV", -price4, ProductType.TECHNICAL));

        store.addProduct(product1);
        store.addProduct(product2);
        store.addProduct(product3);
        store.addProduct(product4);

        assertThrows(ClientNotEnoughMoneyOrBonusesException.class, () -> store.buyProduct(client1, product4));
        assertThrows(ProductNotFoundException.class, () -> store.findProductById(id1));
        assertThrows(ProductAlreadyUsingException.class, () -> store.addProduct(product1));

    }
}
