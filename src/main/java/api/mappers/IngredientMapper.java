package api.mappers;

import api.domains.Ingredient;
import api.domains.dtos.IngredientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class IngredientMapper {

    public static final IngredientMapper INSTANCE = Mappers.getMapper(IngredientMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", source = "ingredientDTO.product")
    @Mapping(target = "name", source = "ingredientDTO.name")
    @Mapping(target = "quantity", source = "ingredientDTO.quantity")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    public abstract Ingredient toIngredient(IngredientDTO ingredientDTO);

    @Mapping(target = "product", source = "ingredient.product")
    @Mapping(target = "name", source = "ingredient.name")
    @Mapping(target = "quantity", source = "ingredient.quantity")
    public abstract IngredientDTO toIngredientDTO(Ingredient ingredient);

}
