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
import ee.taltech.iti0202.store.stores.Store;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClientsRankingTest {
    private final int price1 = 70;
    private final int price2 = 20;
    private final int price3 = 50;
    private final int price4 = 40;
    private final int age1 = 19;
    private final int age2 = 25;
    private final int budget1 = 500;
    private final int vipBonuses = 2003;

    Store store = new DepartmentStore("DEPO", 3);
    Store store2 = new DepartmentStore("Jusk", 1);
    Product product1 = new Product("Pear", price1, ProductType.FOOD);
    Product product2 = new Product("Apple", price2, ProductType.FOOD);
    Product product3 = new Product("Watermelon", price3, ProductType.FOOD);
    Product product4 = new Product("Kivi", 10, ProductType.FOOD);
    Product product5 = new Product("Soup", price3, ProductType.FOOD);
    Client client1 = new Client("Alvado", age1, 100);
    Client client2 = new Client("Huston", age2, budget1);

    public ClientsRankingTest() throws ProductNegativePriceException, ClientUnderageException,
            ClientNegativeBudgetException, StoreCannotHoldProductTypeException, ProductAlreadyUsingException {
        store.addProduct(product1);
        store.addProduct(product2);
        store.addProduct(product3);
        store2.addProduct(product4);
        store2.addProduct(product5);
    }

    @Test
    void getOrderClientsByBoughtProductsAmount() throws ClientNotEnoughMoneyOrBonusesException,
            ProductNotFoundException, StoreEmptyClientsException {
        store.buyProduct(client1, product1);
        store.buyProduct(client2, product2);
        store.buyProduct(client2, product3);

        // Firstly return client that buy more products in store
        assertEquals(client2, store.getClients().get(0));
    }

    @Test
    void getOrderClientsByStoreBonuses() throws ClientNotEnoughMoneyOrBonusesException, ProductNotFoundException,
            StoreEmptyClientsException {
        store.buyProduct(client1, product1);
        store.buyProduct(client2, product2);

        // Return client with more bonuses in store
        assertEquals(client1, store.getClients().get(0));

    }

    @Test
    void getOrderClientsByTotalDivisionToAmount() throws ClientNotEnoughMoneyOrBonusesException,
            ProductNotFoundException, StoreEmptyClientsException, ProductNegativePriceException,
            StoreCannotHoldProductTypeException, ProductAlreadyUsingException {
        Product product4 = new Product("Honey", price4, ProductType.FOOD);
        Product product5 = new Product("Avocado", price4, ProductType.FOOD);
        Product product6 = new Product("Banana", price2, ProductType.FOOD);
        store.addProduct(product4);
        store.addProduct(product5);
        store2.addProduct(product6);
        store.buyProduct(client1, product4);
        store2.buyProduct(client1, product6);
        store.buyProduct(client2, product5);

        // Return client that have more coefficient if sum store products total
        // and divide to amount of all client products
        assertEquals(client2, store.getClients().get(0));

    }

    @Test
    void getOrderClientsByAlphabet() throws ClientNotEnoughMoneyOrBonusesException, ProductNotFoundException,
            StoreCannotHoldProductTypeException, ProductAlreadyUsingException, ProductNegativePriceException,
            StoreEmptyClientsException {
        Product product4 = new Product("Honey", price4, ProductType.FOOD);
        Product product5 = new Product("Avocado", price4, ProductType.FOOD);
        store.addProduct(product4);
        store.addProduct(product5);
        store.buyProduct(client1, product4);
        store.buyProduct(client1, product5);

        // Return client alphabetically
        assertEquals(client1, store.getClients().get(0));
    }

    @Test
    void getVipClients() throws StoreEmptyClientsException, ClientNotEnoughMoneyOrBonusesException,
            ProductNotFoundException {
        // Set vip status to store inside Client class and add extra bonuses for vip clients
        store.buyProduct(client1, product1);
        store.buyProduct(client2, product2);
        store2.buyProduct(client1, product4);
        store2.buyProduct(client2, product5);

        assertEquals(3, client1.getStoreBonuses(store));
        store.setVipClients();
        assertEquals(2, store.getVipClients().size());

        // To vip clients adding 2000 bonuses to store bonus card
        assertEquals(vipBonuses, client1.getStoreBonuses(store));

        assertTrue(client1.isVipInStore(store));

        // In store2 can be only 1 vip client, because of value in constructor
        store2.setVipClients();
        assertEquals(1, store2.getVipClients().size());
    }
}
