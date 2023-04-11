package api.utils;

import api.domains.Ingredient;
import api.domains.dtos.IngredientDTO;

public abstract class IngredientCreator {

    private static final long ID = 1L;
    private static final String PRODUCT = ItemCreator.NAME.concat("_Product");
    private static final String NAME = ItemCreator.NAME.concat("_Name");
    private static final int QUANTITY = 1;

    public static Ingredient ingredient() {
        return Ingredient.builder()
                .id(ID)
                .product(PRODUCT)
                .name(NAME)
                .quantity(QUANTITY)
                .build();
    }

    public static IngredientDTO ingredientToSave() {
        return new IngredientDTO(PRODUCT, NAME.concat("_Save"), QUANTITY);
    }

    public static Ingredient ingredientToUpdate() {
        return ingredient().withName(NAME.concat("_Update"));
    }

    public static Ingredient ingredientToDelete() {
        return ingredient().withName(NAME.concat("_Delete"));
    }

    public static IngredientDTO ingredientDTO() {
        return new IngredientDTO(PRODUCT, NAME, QUANTITY);
    }

    public static IngredientDTO invalidIngredientDTO() {
        return new IngredientDTO(null, null, 0);
    }

}
