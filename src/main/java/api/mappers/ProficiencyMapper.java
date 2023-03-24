package api.mappers;

import api.domains.Proficiency;
import api.domains.dtos.ProficiencyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProficiencyMapper {

    ProficiencyMapper INSTANCE = Mappers.getMapper(ProficiencyMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Proficiency toProficiency(ProficiencyDTO proficiencyDTO);

    ProficiencyDTO toProficiencyDTO(Proficiency proficiency);

}
