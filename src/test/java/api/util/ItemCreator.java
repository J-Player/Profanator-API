package api.util;

import api.domain.Item;
import api.request.post.ItemPostRequestBody;
import api.request.put.ItemPutRequestBody;

public abstract class ItemCreator {

    private static final Integer ID = 1;
    private static final String PROFICIENCY = "proficiency";
    private static final String NAME = "name";
    private static final int QTBYPRODUCTION = 1;

    public static Item item() {
        return Item.builder()
                .id(ID)
                .proficiency(PROFICIENCY)
                .name(NAME)
                .qtByProduction(QTBYPRODUCTION)
                .build();
    }

    public static ItemPostRequestBody itemToSave() {
        return ItemPostRequestBody.builder()
                .proficiency(PROFICIENCY)
                .name(NAME)
                .qtByProduction(QTBYPRODUCTION)
                .build();
    }

    public static ItemPutRequestBody itemToUpdate() {
        return ItemPutRequestBody.builder()
                .id(ID)
                .proficiency(PROFICIENCY)
                .name(NAME)
                .qtByProduction(QTBYPRODUCTION)
                .build();
    }

    public static ItemPostRequestBody invalidItemToSave() {
        return ItemPostRequestBody.builder()
                .proficiency(null)
                .name(null)
                .qtByProduction(0)
                .build();
    }

    public static ItemPutRequestBody invalidItemToUpdate() {
        return ItemPutRequestBody.builder()
                .id(null)
                .proficiency(null)
                .name(null)
                .qtByProduction(0)
                .build();
    }

}
