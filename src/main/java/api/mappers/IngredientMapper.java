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
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    public abstract Ingredient toIngredient(IngredientDTO ingredientDTO);

    public abstract IngredientDTO toIngredientDTO(Ingredient ingredient);

}
