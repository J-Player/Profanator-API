package api.util;

import api.domain.Ingredient;

public class IngredientCreator {

    public static Ingredient createValidIngredient() {
        return Ingredient.builder()
                .id(1)
                .item("Iron Ingot")
                .ingredient("Rough Iron")
                .quantity(2)
                .build();
    }


    public static Ingredient createIngredientToBeSaved() {
        return Ingredient.builder()
                .item("Iron Ingot")
                .ingredient("Rough Iron")
                .quantity(2)
                .build();
    }


    public static Ingredient createIngredientToBeUpdated() {
        return Ingredient.builder()
                .id(1)
                .item("Iron Ingot")
                .ingredient("Rough Iron")
                .quantity(1)
                .build();
    }

}
