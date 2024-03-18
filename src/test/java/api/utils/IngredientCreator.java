package api.utils;

<<<<<<< HEAD:src/test/java/api/util/IngredientCreator.java
import api.models.dtos.IngredientDTO;
import api.models.entities.Ingredient;

public abstract class IngredientCreator {

    private static final String PRODUCT = ItemCreator.NAME;
    private static final String NAME = PRODUCT.concat("_to_save");
=======
import api.domains.Ingredient;
import api.domains.dtos.IngredientDTO;

public abstract class IngredientCreator {

    private static final long ID = 1L;
    private static final String PRODUCT = ItemCreator.NAME.concat("_Product");
    private static final String NAME = ItemCreator.NAME.concat("_Name");
>>>>>>> main:src/test/java/api/utils/IngredientCreator.java
    private static final int QUANTITY = 1;

    public static Ingredient ingredient() {
        return Ingredient.builder()
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
