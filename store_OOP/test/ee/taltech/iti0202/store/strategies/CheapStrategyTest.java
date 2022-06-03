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
import static org.junit.jupiter.api.Assertions.assertThrows;

class CheapStrategyTest {
    private final int budget = 1000;
    private final int age = 19;
    private final int expensive = 700;
    private final int price2 = 200;
    private final int price3 = 50;
    private final int budget1 = 40;
    private final int cheapest = 10;

    Strategy strategy = new CheapStrategy();
    Store store = new DepartmentStore("DEPO", 1);
    Client client = new Client("Kirill", age, budget);
    Product product1 = new Product("TV", expensive, ProductType.TECHNICAL);
    Product product2 = new Product("Rex", price2, ProductType.TOYS);
    Product product3 = new Product("Apple", price3, ProductType.FOOD);
    Product product4 = new Product("Huawei", cheapest, ProductType.TECHNICAL);

    CheapStrategyTest() throws ClientUnderageException, ClientNegativeBudgetException,
            ProductNegativePriceException, StoreCannotHoldProductTypeException, ProductAlreadyUsingException {
        store.addProduct(product1);
        store.addProduct(product2);
        store.addProduct(product3);
        store.addProduct(product4);
    }

    @Test
    void sortProducts() throws StoreEmptyProductsException, ProductNotFoundException, StrategyNotFoundException {

        // client strategy is not set
        assertThrows(StrategyNotFoundException.class, () -> client.followStrategy(store));

        assertEquals(0, client.getProducts().size());
        assertEquals(1000, client.getBudget());
        client.setStrategy(strategy);
        client.followStrategy(store);

        assertEquals(4, client.getProducts().size());
        assertEquals(budget1, client.getBudget());

        // If watch to added products, then first product is cheapest and second is expensive
        assertEquals(cheapest, client.getProducts().get(0).getPrice());
        assertEquals(expensive, client.getProducts().get(client.getProducts().size() - 1).getPrice());

        // after client buy products by strategy, they deleted from store
        assertThrows(StoreEmptyProductsException.class, () -> client.followStrategy(store));
    }
}
