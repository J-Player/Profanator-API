package api.util;

import api.domain.Item;

public class ItemCreator {

    //VALID ITEM
    public static Item createValidItem() {
        return Item.builder()
                .id(1)
                .proficiency("Alchemy")
                .name("Health Potion")
                .qtByProduction(1)
                .build();
    }

    //SAVE
    public static Item createItemToBeSaved() {
        return Item.builder()
                .proficiency("Alchemy")
                .name("Health Potion")
                .qtByProduction(1)
                .build();
    }

    //UPDATE
    public static Item createItemToBeUpdated() {
        return Item.builder()
                .id(1)
                .proficiency("Tinkering")
                .name("Iron Nails")
                .qtByProduction(3)
                .build();
    }

}
