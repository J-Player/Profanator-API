package api.mappers;

import api.models.dtos.IngredientDTO;
import api.models.entities.Ingredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IngredientMapper {

    IngredientMapper INSTANCE = Mappers.getMapper(IngredientMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Ingredient toIngredient(IngredientDTO ingredientDTO);

    IngredientDTO toIngredientDTO(Ingredient ingredient);

}
