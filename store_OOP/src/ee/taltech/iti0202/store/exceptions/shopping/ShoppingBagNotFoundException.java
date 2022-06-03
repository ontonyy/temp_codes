package ee.taltech.iti0202.store.exceptions.shopping;

public class ShoppingBagNotFoundException extends Exception {
    public ShoppingBagNotFoundException() {
        super("Create shopping bag before adding to it");
    }
}
