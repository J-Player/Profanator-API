package api.mapper;

import api.domain.Proficiency;
import api.request.post.ProficiencyPostRequestBody;
import api.request.put.ProficiencyPutRequestBody;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class ProficiencyMapper {

    public static final ProficiencyMapper INSTANCE = Mappers.getMapper(ProficiencyMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "proficiency.name")
    public abstract Proficiency toProficiency(ProficiencyPostRequestBody proficiency);

    @Mapping(target = "id", source = "proficiency.id")
    @Mapping(target = "name", source = "proficiency.name")
    public abstract Proficiency toProficiency(ProficiencyPutRequestBody proficiency);

}
