package api.util;

import api.models.dtos.ItemDTO;
import api.models.entities.Item;

public abstract class ItemCreator {

    protected static final String PROFICIENCY = ProficiencyCreator.NAME;
    protected static final String NAME = "item";
    protected static final int QTBYPRODUCTION = 1;

    public static Item item() {
        return Item.builder()
                .proficiency(PROFICIENCY)
                .name(NAME)
                .qtByProduction(QTBYPRODUCTION)
                .build();
    }

    public static ItemDTO itemDTO() {
        return ItemDTO.builder()
                .proficiency(PROFICIENCY)
                .name(NAME)
                .qtByProduction(QTBYPRODUCTION)
                .build();
    }

    public static ItemDTO invalidItemDTO() {
        return itemDTO()
                .withProficiency(null)
                .withName(null)
                .withQtByProduction(0);
    }

    public static Item itemToRead() {
        return item().withName(NAME.concat("_to_read"));
    }

    public static Item itemToUpdate() {
        return item().withName(NAME.concat("_to_update"));
    }

    public static Item itemToDelete() {
        return item().withName(NAME.concat("_to_delete"));
    }

}
