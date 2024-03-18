package api.utils;

import api.domains.Proficiency;
import api.domains.dtos.ProficiencyDTO;

public abstract class ProficiencyCreator {

    private static final long ID = 1L;
    protected static final String NAME = "Proficiency";

    public static Proficiency proficiency() {
        return Proficiency.builder()
                .id(ID)
                .name(NAME)
                .build();
    }

    public static ProficiencyDTO proficiencyToSave() {
        return new ProficiencyDTO(NAME.concat("_Save"));
    }

    public static Proficiency proficiencyToUpdate() {
        return proficiency().withName(NAME.concat("_Update"));
    }

    public static Proficiency proficiencyToDelete() {
        return proficiency().withName(NAME.concat("_Delete"));
    }

    public static ProficiencyDTO proficiencyDTO() {
        return new ProficiencyDTO(NAME);
    }

    public static ProficiencyDTO invalidProficiencyDTO() {
        return new ProficiencyDTO(null);
    }

}
