package api.util;

import api.domains.Ingredient;
import api.domains.dtos.IngredientDTO;

import java.util.UUID;

public abstract class IngredientCreator {

    private static final UUID ID = UUID.randomUUID();
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
