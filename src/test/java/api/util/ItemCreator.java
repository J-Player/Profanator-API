package api.util;

import api.domains.Item;
import api.domains.dtos.ItemDTO;

import java.util.UUID;

import static java.util.UUID.randomUUID;

public abstract class ItemCreator {

    private static final UUID ID = randomUUID();
    protected static final String PROFICIENCY = ProficiencyCreator.NAME;
    protected static final String NAME = "item";
    protected static final int QTBYPRODUCTION = 1;

    public static Item item() {
        return Item.builder()
                .id(ID)
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
        return ItemDTO.builder()
                .proficiency(null)
                .name(null)
                .qtByProduction(0)
                .build();
    }

}
