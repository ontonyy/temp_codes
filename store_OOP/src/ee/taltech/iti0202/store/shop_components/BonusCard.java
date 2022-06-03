package ee.taltech.iti0202.store.shop_components;

import ee.taltech.iti0202.store.stores.StoreType;
import static ee.taltech.iti0202.store.Constants.BONUS_COEFFICIENT;

public class BonusCard {
    private int bonuses;
    private StoreType type;

    public BonusCard(StoreType type) {
        this.type = type;
    }

    /*
    Get some product price and multiply to coefficient
     */
    public void addBonuses(int price) {
        this.bonuses += (int) Math.ceil((double) price / BONUS_COEFFICIENT);
    }

    public void addVipBonuses(int bonuses) {
        this.bonuses += bonuses;
    }

    public void reduceBonuses(int reduce) {
        bonuses -= reduce;
    }

    public int getBonuses() {
        return bonuses;
    }
}
