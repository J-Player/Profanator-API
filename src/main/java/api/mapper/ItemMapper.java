package api.mapper;

import api.domain.Item;
import api.request.post.ItemPostRequestBody;
import api.request.put.ItemPutRequestBody;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class ItemMapper {

    public static final ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "proficiency", source = "item.proficiency")
    @Mapping(target = "name", source = "item.name")
    @Mapping(target = "qtByProduction", source = "item.qtByProduction")
    public abstract Item toItem(ItemPostRequestBody item);

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "proficiency", source = "item.proficiency")
    @Mapping(target = "name", source = "item.name")
    @Mapping(target = "qtByProduction", source = "item.qtByProduction")
    public abstract Item toItem(ItemPutRequestBody item);

}
