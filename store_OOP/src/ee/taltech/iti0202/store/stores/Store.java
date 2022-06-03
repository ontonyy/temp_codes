package ee.taltech.iti0202.store.stores;

import ee.taltech.iti0202.store.Client;
import ee.taltech.iti0202.store.exceptions.client.ClientNotEnoughMoneyOrBonusesException;
import ee.taltech.iti0202.store.exceptions.product.ProductAlreadyUsingException;
import ee.taltech.iti0202.store.exceptions.product.ProductNegativePriceException;
import ee.taltech.iti0202.store.exceptions.product.ProductNotFoundException;
import ee.taltech.iti0202.store.exceptions.shopping.ShoppingBagNotFoundException;
import ee.taltech.iti0202.store.exceptions.store.StoreCannotHoldProductTypeException;
import ee.taltech.iti0202.store.exceptions.store.StoreEmptyClientsException;
import ee.taltech.iti0202.store.products.Product;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Store {
    private String name;
    private long profit;
    private List<Product> products = new LinkedList<>();
    private List<Client> clients = new LinkedList<>();
    private StoreType type;
    private int vipAmount = 0;

    public Store(String name, StoreType type, int vipAmount) {
        this.name = name;
        this.type = type;
        this.vipAmount = vipAmount;
    }

    public void devastateClientBag(Client client) throws ShoppingBagNotFoundException,
            ClientNotEnoughMoneyOrBonusesException, ProductNotFoundException {
        client.devastateShoppingBag(this);
    }

    /*
    Buy product by client bonuses in store bonuses card
     */
    public void buyProductByBonuses(Client client, Product product) throws ProductNotFoundException,
            ClientNotEnoughMoneyOrBonusesException {
        if (client.buyProductByBonuses(product, this)) {
            profit += product.getPrice();
            removeProduct(product);

            if (!clients.contains(client)) {
                clients.add(client);
            }
        }
    }

    public void buyProduct(Client client, Product product) throws ClientNotEnoughMoneyOrBonusesException,
            ProductNotFoundException {
        if (client.buyProduct(product, this)) {
            profit += product.getPrice();
            removeProduct(product);

            if (!clients.contains(client)) {
                clients.add(client);
            }
        }
    }

    public List<Client> getVipClients() {
        List<Client> vipClients = new LinkedList<>();
        for (Client client : clients) {
            if (client.isVipInStore(this)) {
                vipClients.add(client);
            }
        }
        return vipClients;
    }

    public void setVipClients() throws StoreEmptyClientsException {
        int count = 0;
        for (Client client : getClients()) {
            if (count < vipAmount) {
                client.setVipStatus(this, true);
                count++;
            } else {
                client.setVipStatus(this, false);
            }
        }
    }

    /*
    Order clients: check products from store, then check bonuses in store,
    then check products total price division to amount of all client products,
    then check name alphabetically
     */
    public List<Client> getClients() throws StoreEmptyClientsException {
        if (clients.size() > 0) {
            final Store store = this;
            return clients.stream().sorted((o1, o2) -> {
                if (o1.getStoreProducts(store).size() > o2.getStoreProducts(store).size()) {
                    return -1;
                } else if (o1.getStoreProducts(store).size() < o2.getStoreProducts(store).size()) {
                    return 1;
                } else {
                    if (o1.getStoreBonuses(store) > o2.getStoreBonuses(store)) {
                        return -1;
                    } else if (o1.getStoreBonuses(store) < o2.getStoreBonuses(store)) {
                        return 1;
                    } else {
                        double o1PriceCoefficient = Math.round(((double) o1.getStoreTotalProductsPrice(store)
                                / o1.getProducts().size()) * 10) / 10.0;
                        double o2PriceCoefficient = Math.round(((double) o2.getStoreTotalProductsPrice(store)
                                / o2.getProducts().size()) * 10) / 10.0;
                        if (o1PriceCoefficient > o2PriceCoefficient) {
                            return -1;
                        } else if (o1PriceCoefficient < o2PriceCoefficient) {
                            return 1;
                        } else {
                            return o1.getName().compareTo(o2.getName());
                        }
                    }
                }
            }).collect(Collectors.toList());
        } else {
            throw new StoreEmptyClientsException();
        }
    }

    public void addProduct(Product product)
            throws ProductAlreadyUsingException, ProductNegativePriceException,
            StoreCannotHoldProductTypeException {
        if (product.getHoldStore() == null) {
            if (product.getPrice() > 0) {
                product.setHoldStore(this);
                products.add(product);
            } else {
                throw new ProductNegativePriceException();
            }
        } else {
            throw new ProductAlreadyUsingException();
        }
    }

    public void removeProduct(Product product) throws ProductNotFoundException {
        if (products.contains(product)) {
            products.remove(product);
        } else {
            throw new ProductNotFoundException();
        }
    }

    public List<Product> findProductByName(String name) {
        List<Product> foundedProducts = new LinkedList<>();
        for (Product product : products) {
            if (product.getName().equals(name)) {
                foundedProducts.add(product);
            }
        }
        return foundedProducts;
    }

    public List<Product> findProductByPrice(int price) {
        List<Product> foundedProducts = new LinkedList<>();
        for (Product product : products) {
            if (product.getPrice() == price) {
                foundedProducts.add(product);
            }
        }
        return foundedProducts;
    }

    public Product findProductById(int id) throws ProductNotFoundException {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }
        throw new ProductNotFoundException();
    }

    public List<Product> getProducts() {
        return products;
    }

    public String getName() {
        return name;
    }

    public long getProfit() {
        return profit;
    }

    public StoreType getType() {
        return type;
    }

    public void reduceProfit(int reduce) {
        profit -= reduce;
    }

    public void addProfit(int add) {
        profit += add;
    }

    public void createShoppingBag(Client client) {
        client.createShoppingBag(this);
    }

    public void addProductToBag(Client client, Product product) throws ShoppingBagNotFoundException {
        client.addProductToBag(this, product);
    }

    public void removeProductFromBag(Client client, Product product) throws ShoppingBagNotFoundException {
        client.removeProductFromBag(this, product);
    }

    public int countProducts(String name) {
        int count = 0;
        for (Product product : products) {
            if (product.getName().equals(name)) {
                count++;
            }
        }
        return count;
    }

    public List<Product> getProductsByName(String name) {
        List<Product> namedProducts = new LinkedList<>();
        for (Product product : products) {
            if (product.getName().equals(name)) {
                namedProducts.add(product);
            }
        }
        return namedProducts;
    }
}
