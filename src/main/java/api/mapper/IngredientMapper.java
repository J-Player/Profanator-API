package api.mapper;

import api.domain.Ingredient;
import api.request.post.IngredientPostRequestBody;
import api.request.put.IngredientPutRequestBody;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class IngredientMapper {

    public static final IngredientMapper INSTANCE = Mappers.getMapper(IngredientMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", source = "ingredient.product")
    @Mapping(target = "name", source = "ingredient.name")
    @Mapping(target = "quantity", source = "ingredient.quantity")
    public abstract Ingredient toIngredient(IngredientPostRequestBody ingredient);

    @Mapping(target = "id", source = "ingredient.id")
    @Mapping(target = "product", source = "ingredient.product")
    @Mapping(target = "name", source = "ingredient.name")
    @Mapping(target = "quantity", source = "ingredient.quantity")
    public abstract Ingredient toIngredient(IngredientPutRequestBody ingredient);

}
