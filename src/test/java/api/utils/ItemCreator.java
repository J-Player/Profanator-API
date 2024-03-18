package api.utils;

import api.domains.Item;
import api.domains.dtos.ItemDTO;

public abstract class ItemCreator {

    private static final long ID = 1L;
    protected static final String PROFICIENCY = ProficiencyCreator.NAME;
    protected static final String NAME = "Item";
    private static final int QTBYPRODUCTION = 1;

    public static Item item() {
        return Item.builder()
                .id(ID)
                .proficiency(PROFICIENCY)
                .name(NAME)
                .qtByProduction(QTBYPRODUCTION)
                .build();
    }

    public static ItemDTO itemToSave() {
        return new ItemDTO(PROFICIENCY, NAME.concat("_Save"), QTBYPRODUCTION);
    }

    public static Item itemToUpdate() {
        return item().withName(NAME.concat("_Update"));
    }

    public static Item itemToDelete() {
        return item().withName(NAME.concat("_Delete"));
    }

    public static ItemDTO itemDTO() {
        return new ItemDTO(PROFICIENCY, NAME, QTBYPRODUCTION);
    }

    public static ItemDTO invalidItemDTO() {
        return new ItemDTO(null, null, 0);
    }

}
