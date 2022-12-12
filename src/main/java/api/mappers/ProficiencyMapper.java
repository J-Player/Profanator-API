package api.mappers;

import api.domains.Proficiency;
import api.domains.dtos.ProficiencyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class ProficiencyMapper {

    public static final ProficiencyMapper INSTANCE = Mappers.getMapper(ProficiencyMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "proficiencyDTO.name")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    public abstract Proficiency toProficiency(ProficiencyDTO proficiencyDTO);

}
