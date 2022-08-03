package api.util;

import api.domain.Ingredient;
import api.request.post.IngredientPostRequestBody;
import api.request.put.IngredientPutRequestBody;

public abstract class IngredientCreator {

    private static final Integer ID = 1;
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


    public static IngredientPostRequestBody ingredientToSave() {
        return IngredientPostRequestBody.builder()
                .product(PRODUCT)
                .name(NAME)
                .quantity(QUANTITY)
                .build();
    }

    public static IngredientPutRequestBody ingredientToUpdate() {
        return IngredientPutRequestBody.builder()
                .id(ID)
                .product(PRODUCT)
                .name(NAME)
                .quantity(QUANTITY)
                .build();
    }

    public static IngredientPostRequestBody invalidIngredientToSave() {
        return IngredientPostRequestBody.builder()
                .product(null)
                .name(null)
                .quantity(0)
                .build();
    }

    public static IngredientPutRequestBody invalidIngredientToUpdate() {
        return IngredientPutRequestBody.builder()
                .id(null)
                .product(null)
                .name(null)
                .quantity(0)
                .build();
    }

}
