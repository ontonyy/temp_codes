package ee.taltech.iti0202.store;

import ee.taltech.iti0202.store.exceptions.client.ClientNegativeBudgetException;
import ee.taltech.iti0202.store.exceptions.client.ClientNotEnoughMoneyOrBonusesException;
import ee.taltech.iti0202.store.exceptions.client.ClientUnderageException;
import ee.taltech.iti0202.store.exceptions.product.ProductNotFoundException;
import ee.taltech.iti0202.store.exceptions.shopping.ShoppingBagNotFoundException;
import ee.taltech.iti0202.store.exceptions.store.StoreEmptyProductsException;
import ee.taltech.iti0202.store.exceptions.strategies.StrategyNotFoundException;
import ee.taltech.iti0202.store.products.Product;
import ee.taltech.iti0202.store.products.ProductType;
import ee.taltech.iti0202.store.shop_components.BonusCard;
import ee.taltech.iti0202.store.shop_components.ShoppingBag;
import ee.taltech.iti0202.store.stores.Store;
import ee.taltech.iti0202.store.strategies.Strategy;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static ee.taltech.iti0202.store.Constants.AGE_EDGE;
import static ee.taltech.iti0202.store.Constants.VIP_BONUS;

public class Client {
    private String name;
    private int age;
    private long budget;
    private List<Product> products = new LinkedList<>();
    private Map<Store, BonusCard> storeBonusCards = new LinkedHashMap<>();
    private Map<Store, ShoppingBag> storesShoppingBags = new LinkedHashMap<>();
    private Map<Store, Boolean> storesVipStatus = new LinkedHashMap<>();
    private List<ProductType> favoriteProductTypes = new LinkedList<>();
    private Strategy strategy;

    public Client(String name, int age, long budget) throws ClientNegativeBudgetException, ClientUnderageException {
        this.name = name;
        setAge(age);
        setBudget(budget);
    }

    /*
    Client buy all products got from strategy and follow it
     */
    public void followStrategy(Store store) throws StrategyNotFoundException, StoreEmptyProductsException,
            ProductNotFoundException {
        if (strategy != null) {
            List<Product> storeProducts = store.getProducts();
            if (storeProducts.size() > 0) {
                for (Product product : strategy.sortProducts(storeProducts)) {
                    try {
                        store.buyProduct(this, product);
                    } catch (ClientNotEnoughMoneyOrBonusesException ignored) {

                    }
                }
            } else {
                throw new StoreEmptyProductsException();
            }
        } else {
            throw new StrategyNotFoundException();
        }
    }

    /*
    Clear store shopping bag, buy all products, remove them from store
     */
    public void devastateShoppingBag(Store store) throws ShoppingBagNotFoundException,
            ClientNotEnoughMoneyOrBonusesException, ProductNotFoundException {

        if (storesShoppingBags.containsKey(store)) {
            ShoppingBag bag = storesShoppingBags.get(store);
            int total = bag.getTotalPrice();

            if (budget >= total) {
                for (Product product : bag.getProducts()) {
                    store.buyProduct(this, product);
                }
                bag.devastate();
                storesShoppingBags.remove(store);
            } else {
                throw new ClientNotEnoughMoneyOrBonusesException();
            }
        } else {
            throw new ShoppingBagNotFoundException();
        }
    }

    public boolean buyProductByBonuses(Product product, Store store) throws ClientNotEnoughMoneyOrBonusesException {
        BonusCard bonusCard = createOrGetBonusCard(store);
        if (bonusCard.getBonuses() >= product.getBonusPrice()) {
            addProduct(product);
            bonusCard.reduceBonuses(product.getBonusPrice());
            return true;
        } else {
            throw new ClientNotEnoughMoneyOrBonusesException();
        }
    }

    public boolean buyProduct(Product product, Store store) throws ClientNotEnoughMoneyOrBonusesException {
        if (budget >= product.getPrice()) {
            budget -= product.getPrice();
            addProduct(product);
            addBonuses(store, product);

            return true;
        } else {
            if (buyProductByBonuses(product, store)) {
                return true;
            } else {
                throw new ClientNotEnoughMoneyOrBonusesException();
            }
        }
    }

    public void createShoppingBag(Store store) {
        if (!storesShoppingBags.containsKey(store)) {
            storesShoppingBags.put(store, new ShoppingBag());
        }
    }

    public void addProductToBag(Store store, Product product) throws ShoppingBagNotFoundException {
        if (storesShoppingBags.containsKey(store)) {
            ShoppingBag bag = storesShoppingBags.get(store);
            bag.addProduct(product);
        } else {
            throw new ShoppingBagNotFoundException();
        }
    }

    public void removeProductFromBag(Store store, Product product) throws ShoppingBagNotFoundException {
        if (storesShoppingBags.containsKey(store)) {
            ShoppingBag bag = storesShoppingBags.get(store);
            bag.removeProduct(product);
        } else {
            throw new ShoppingBagNotFoundException();
        }
    }

    public BonusCard createOrGetBonusCard(Store store) {
        if (!storeBonusCards.containsKey(store)) {
            addBonusCard(store);
        }
        return storeBonusCards.get(store);
    }

    public void addBonuses(Store store, Product product) {
        BonusCard bonusCard = createOrGetBonusCard(store);
        bonusCard.addBonuses(product.getPrice());
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    public List<Product> getProducts() {
        return products;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) throws ClientUnderageException {
        if (age >= AGE_EDGE) {
            this.age = age;
        } else {
            throw new ClientUnderageException();
        }
    }

    public long getBudget() {
        return budget;
    }

    public void setBudget(long budget) throws ClientNegativeBudgetException {
        if (budget >= 0) {
            this.budget = budget;
        } else {
            throw new ClientNegativeBudgetException();
        }
    }

    public void addBonusCard(Store store) {
        BonusCard bonusCard = new BonusCard(store.getType());
        storeBonusCards.put(store, bonusCard);
    }

    public void addBudget(int add) {
        budget += add;
    }

    public Map<Store, BonusCard> getStoreBonusCards() {
        return storeBonusCards;
    }

    public Map<Store, ShoppingBag> getStoresShoppingBags() {
        return storesShoppingBags;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public List<ProductType> getFavoriteProductTypes() {
        return favoriteProductTypes;
    }

    public void setFavoriteProductTypes(List<ProductType> favoriteProductTypes) {
        this.favoriteProductTypes = new LinkedList<>(favoriteProductTypes);
    }

    public boolean containsProduct(String name, ProductType type) {
        for (Product product : products) {
            if (product.getName().equals(name) && product.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public List<Product> getStoreProducts(Store store) {
        List<Product> storeProducts = new LinkedList<>();
        for (Product product : products) {
            if (product.getHoldStore() == store) {
                storeProducts.add(product);
            }
        }
        return storeProducts;
    }

    public int getStoreBonuses(Store store) {
        return storeBonusCards.get(store).getBonuses();
    }

    public int getTotalProductsPrice() {
        int total = 0;
        for (Product product : products) {
            total += product.getPrice();
        }
        return total;
    }

    public int getStoreTotalProductsPrice(Store store) {
        int total = 0;
        for (Product product : getStoreProducts(store)) {
            total += product.getPrice();
        }
        return total;
    }

    public void setVipStatus(Store store, boolean bool) {
        if (bool) {
            storeBonusCards.get(store).addVipBonuses(VIP_BONUS);
        }
        storesVipStatus.put(store, bool);
    }

    public boolean isVipInStore(Store store) {
        return storesVipStatus.get(store);
    }

    public ShoppingBag getStoreShoppingBag(Store store) {
        return storesShoppingBags.get(store);
    }
}
