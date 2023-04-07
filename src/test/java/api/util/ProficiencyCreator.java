package api.util;

import api.domains.Proficiency;
import api.domains.dtos.ProficiencyDTO;

public abstract class ProficiencyCreator {

    private static final Long ID = 1L;
    protected static final String NAME = "Proficiency";

    public static Proficiency proficiency() {
        return Proficiency.builder()
                .id(ID)
                .name(NAME)
                .build();
    }

    public static ProficiencyDTO proficiencyToSave() {
        return ProficiencyDTO.builder().name(NAME.concat("_Save")).build();
    }

    public static Proficiency proficiencyToUpdate() {
        return proficiency().withName(NAME.concat("_Update"));
    }

    public static Proficiency proficiencyToDelete() {
        return proficiency().withName(NAME.concat("_Delete"));
    }

    public static ProficiencyDTO invalidProficiencyDTO() {
        return ProficiencyDTO.builder()
                .name(null)
                .build();
    }

}
