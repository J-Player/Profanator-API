package api.util;

import api.domains.Proficiency;
import api.domains.dtos.ProficiencyDTO;

import java.util.UUID;

public abstract class ProficiencyCreator {

    private static final UUID ID = UUID.randomUUID();
    private static final String NAME = "Proficiency";

    public static Proficiency proficiency() {
        return Proficiency.builder()
                .id(ID)
                .name(NAME)
                .build();
    }

    public static ProficiencyDTO proficiencyDTO() {
        return ProficiencyDTO.builder()
                .name(NAME)
                .build();
    }

    public static ProficiencyDTO invalidProficiencyDTO() {
        return ProficiencyDTO.builder()
                .name(null)
                .build();
    }

}
