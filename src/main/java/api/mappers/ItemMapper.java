package api.mappers;

import api.domains.Item;
import api.domains.dtos.ItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class ItemMapper {

    public static final ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "proficiency", source = "itemDTO.proficiency")
    @Mapping(target = "name", source = "itemDTO.name")
    @Mapping(target = "qtByProduction", source = "itemDTO.qtByProduction")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    public abstract Item toItem(ItemDTO itemDTO);

    @Mapping(target = "proficiency", source = "item.proficiency")
    @Mapping(target = "name", source = "item.name")
    @Mapping(target = "qtByProduction", source = "item.qtByProduction")
    public abstract ItemDTO toItemDTO(Item item);

}
