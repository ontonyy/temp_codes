package ee.taltech.iti0202.store.stores;

import ee.taltech.iti0202.store.Client;
import ee.taltech.iti0202.store.exceptions.client.ClientNegativeBudgetException;
import ee.taltech.iti0202.store.exceptions.client.ClientNotEnoughMoneyOrBonusesException;
import ee.taltech.iti0202.store.exceptions.client.ClientUnderageException;
import ee.taltech.iti0202.store.exceptions.product.ProductAlreadyUsingException;
import ee.taltech.iti0202.store.exceptions.product.ProductNegativePriceException;
import ee.taltech.iti0202.store.exceptions.product.ProductNotFoundException;
import ee.taltech.iti0202.store.exceptions.store.StoreCannotHoldProductTypeException;
import ee.taltech.iti0202.store.products.Product;
import ee.taltech.iti0202.store.products.ProductType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DepartmentStoreTest {
    private final int price1 = 50;
    private final int budget1 = 850;
    private final int budget2 = 150;
    private final int budget3 = 950;
    private final int age1 = 19;
    DepartmentStore store = new DepartmentStore("DEPO", 3);
    Product product1 = new Product("Kiwi", price1, ProductType.FOOD);
    Product product2 = new Product("Banana", 100, ProductType.FOOD);
    Client client = new Client("Stas", age1, 1000);

    DepartmentStoreTest() throws ClientUnderageException, ClientNegativeBudgetException,
            ProductNegativePriceException {
    }

    @Test
    void returnProduct() throws StoreCannotHoldProductTypeException, ProductAlreadyUsingException,
            ProductNegativePriceException, ClientNotEnoughMoneyOrBonusesException, ProductNotFoundException {
        store.addProduct(product1);
        store.addProduct(product2);
        store.buyProduct(client, product1);
        store.buyProduct(client, product2);

        assertEquals(0, store.getProducts().size());
        assertEquals(budget1, client.getBudget());
        assertEquals(budget2, store.getProfit());

        // After product returning, in store will add this product, and reduce profit,
        // and in client will add budget and remove product from client products
        store.returnProduct(client, product2);
        assertEquals(budget3, client.getBudget());
        assertEquals(price1, store.getProfit());
        assertEquals(1, store.getProducts().size());

    }
}
