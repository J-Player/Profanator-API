package api.util;

import api.domains.Ingredient;
import api.domains.dtos.IngredientDTO;

public abstract class IngredientCreator {

    private static final Long ID = 1L;
    private static final String PRODUCT = "Product";
    private static final String NAME = "Name";
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
        return ingredientDTO().withName(NAME.concat("_Save"));
    }

    public static Ingredient ingredientToUpdate() {
        return ingredient().withName(NAME.concat("_Update"));
    }

    public static Ingredient ingredientToDelete() {
        return ingredient().withName(NAME.concat("_Delete"));
    }

    public static IngredientDTO ingredientDTO() {
        return IngredientDTO.builder()
                .product(PRODUCT)
                .name(NAME)
                .quantity(QUANTITY)
                .build();
    }

    public static IngredientDTO invalidIngredientDTO() {
        return IngredientDTO.builder()
                .product(null)
                .name(null)
                .quantity(0)
                .build();
    }

}
