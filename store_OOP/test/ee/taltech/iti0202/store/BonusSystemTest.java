package ee.taltech.iti0202.store;

import ee.taltech.iti0202.store.exceptions.client.ClientNegativeBudgetException;
import ee.taltech.iti0202.store.exceptions.client.ClientNotEnoughMoneyOrBonusesException;
import ee.taltech.iti0202.store.exceptions.client.ClientUnderageException;
import ee.taltech.iti0202.store.exceptions.product.ProductAlreadyUsingException;
import ee.taltech.iti0202.store.exceptions.product.ProductNegativePriceException;
import ee.taltech.iti0202.store.exceptions.product.ProductNotFoundException;
import ee.taltech.iti0202.store.exceptions.shopping.ShoppingBagNotFoundException;
import ee.taltech.iti0202.store.exceptions.store.StoreCannotHoldProductTypeException;
import ee.taltech.iti0202.store.products.Product;
import ee.taltech.iti0202.store.products.ProductType;
import ee.taltech.iti0202.store.stores.DepartmentStore;
import ee.taltech.iti0202.store.stores.GroceryStore;
import ee.taltech.iti0202.store.stores.Store;
import ee.taltech.iti0202.store.stores.StoreType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BonusSystemTest {
    private final int age1 = 19;
    private final int budget1 = 4000;
    private final int price1 = 3000;
    private final int price2 = 500;
    private final int bonuses1 = 125;
    private final int bonuses2 = 105;
    private final int profit1 = 3489;
    private final int profit2 = 511;
    Store groceryStore = new GroceryStore("Rimi", 3);
    Store departmentStore = new DepartmentStore("DEPO", 1);
    Client client = new Client("John", age1, budget1);

    Product product1 = new Product("Apple", 10, ProductType.FOOD);
    Product product2 = new Product("Banana", 100, ProductType.FOOD);
    Product product5 = new Product("Banana", price1, ProductType.FOOD);
    Product product3 = new Product("Scooter", price2, ProductType.TECHNICAL);
    Product product4 = new Product("Pear", 1, ProductType.FOOD);

    public BonusSystemTest() throws ClientUnderageException, ClientNegativeBudgetException,
            ProductNegativePriceException {
    }

    @Test
    void actionsWithBonuses() throws StoreCannotHoldProductTypeException, ProductAlreadyUsingException,
            ProductNegativePriceException, ClientNotEnoughMoneyOrBonusesException, ProductNotFoundException,
            ShoppingBagNotFoundException, ClientNegativeBudgetException {
        groceryStore.addProduct(product1);
        groceryStore.addProduct(product2);
        groceryStore.addProduct(product4);
        groceryStore.addProduct(product5);

        // If client buy product in store, automatically creates StoreBonusCard and accrued first bought product points
        assertEquals(0, client.getStoreBonusCards().size());
        groceryStore.buyProduct(client, product2);
        assertEquals(1, client.getStoreBonusCards().size());
        assertEquals(4, client.getStoreBonuses(groceryStore));

        groceryStore.buyProduct(client, product1); // 10 / 25 and accrued 1 points to bonus card
        assertEquals(5, client.getStoreBonuses(groceryStore));

        // 2 types of stores
        assertEquals(StoreType.DEPARTMENT, departmentStore.getType());
        assertEquals(StoreType.GROCERY, groceryStore.getType());

        // Cannot add to grocery store, product with type not equaled FOOD
        assertThrows(StoreCannotHoldProductTypeException.class, () -> groceryStore.addProduct(product3));

        groceryStore.buyProduct(client, product5);
        assertEquals(3, client.getProducts().size());
        assertEquals(bonuses1, client.getStoreBonuses(groceryStore));

        // Product was bought by bonuses in card
        groceryStore.buyProductByBonuses(client, product4);
        assertEquals(bonuses2, client.getStoreBonuses(groceryStore));

        // Products have 6 different types
        assertEquals(ProductType.TECHNICAL, product3.getType());
        assertEquals(ProductType.FOOD, product1.getType());
    }

    @Test
    void actionWithShoppingBags() throws ShoppingBagNotFoundException, ClientNotEnoughMoneyOrBonusesException,
            ProductNotFoundException, StoreCannotHoldProductTypeException, ProductAlreadyUsingException,
            ProductNegativePriceException {
        departmentStore.addProduct(product1);
        departmentStore.addProduct(product3);
        departmentStore.addProduct(product4);
        assertEquals(0, client.getStoresShoppingBags().size());
        // Create 2 stores shopping bags
        departmentStore.createShoppingBag(client);
        assertEquals(1, client.getStoresShoppingBags().size());
        assertThrows(ShoppingBagNotFoundException.class, () -> groceryStore.devastateClientBag(client));

        departmentStore.addProductToBag(client, product1);
        departmentStore.addProductToBag(client, product3);
        departmentStore.addProductToBag(client, product4);


        // add to store shopping bag some products
        assertEquals(3, client.getStoreShoppingBag(departmentStore).getProducts().size());
        assertEquals(budget1, client.getBudget());

        departmentStore.devastateClientBag(client);

        // client by products from store shopping bag
        assertEquals(profit1, client.getBudget());
        assertEquals(profit2, departmentStore.getProfit());
        assertEquals(0, client.getStoresShoppingBags().size());
    }
}
