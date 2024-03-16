package api.util;

import api.models.dtos.IngredientDTO;
import api.models.entities.Ingredient;

public abstract class IngredientCreator {

    private static final String PRODUCT = ItemCreator.NAME;
    private static final String NAME = PRODUCT.concat("_to_save");
    private static final int QUANTITY = 1;

    public static Ingredient ingredient() {
        return Ingredient.builder()
                .product(PRODUCT)
                .name(NAME)
                .quantity(QUANTITY)
                .build();
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

    public static Ingredient ingredientToRead() {
        return ingredient()
                .withName(ItemCreator.itemToRead().getName());
    }

    public static Ingredient ingredientToUpdate() {
        return ingredient()
                .withName(ItemCreator.itemToUpdate().getName());
    }

    public static Ingredient ingredientToDelete() {
        return ingredient()
                .withName(ItemCreator.itemToDelete().getName());
    }

}
