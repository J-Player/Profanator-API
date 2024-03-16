package api.util;

import api.models.dtos.ProficiencyDTO;
import api.models.entities.Proficiency;

public abstract class ProficiencyCreator {

    protected static final String NAME = "Proficiency";

    public static Proficiency proficiency() {
        return Proficiency.builder()
                .name(NAME)
                .build();
    }

    public static ProficiencyDTO proficiencyDTO() {
        return ProficiencyDTO.builder()
                .name(NAME)
                .build();
    }

    public static ProficiencyDTO invalidProficiencyDTO() {
        return proficiencyDTO().withName(null);
    }

    public static Proficiency ProficiencyToRead() {
        return proficiency().withName(NAME.concat("_to_read"));
    }

    public static Proficiency proficiencyToUpdate() {
        return proficiency().withName(NAME.concat("_to_update"));
    }

    public static Proficiency proficiencyToDelete() {
        return proficiency().withName(NAME.concat("_to_delete"));
    }

}
