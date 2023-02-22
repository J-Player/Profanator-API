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
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    public abstract Item toItem(ItemDTO itemDTO);

    public abstract ItemDTO toItemDTO(Item item);

}
