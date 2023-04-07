package api.util;

import api.domains.Item;
import api.domains.dtos.ItemDTO;

public abstract class ItemCreator {

    private static final Long ID = 1L;
    protected static final String PROFICIENCY = ProficiencyCreator.NAME;
    protected static final String NAME = "Item";
    private static final Integer QTBYPRODUCTION = 1;

    public static Item item() {
        return Item.builder()
                .id(ID)
                .proficiency(PROFICIENCY)
                .name(NAME)
                .qtByProduction(QTBYPRODUCTION)
                .build();
    }

    public static ItemDTO itemToSave() {
        return ItemDTO.builder()
                .proficiency(PROFICIENCY)
                .name(NAME.concat("_Save"))
                .qtByProduction(QTBYPRODUCTION)
                .build();
    }

    public static Item itemToUpdate() {
        return item().withName(NAME.concat("_Update"));
    }

    public static Item itemToDelete() {
        return item().withName(NAME.concat("_Delete"));
    }

    public static ItemDTO invalidItemDTO() {
        return ItemDTO.builder()
                .name(null)
                .qtByProduction(0)
                .build();
    }

}
