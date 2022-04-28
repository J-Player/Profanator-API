package api.util;

import api.domain.Proficiency;

public class ProficiencyCreator {

    //VALID PROFICIENCY
    public static Proficiency createValidProficiency() {
        return Proficiency.builder()
                .id(1)
                .name("Alchemy")
                .build();
    }

    //SAVE
    public static Proficiency createProficiencyToBeSaved() {
        return Proficiency.builder()
                .name("Alchemy")
                .build();
    }

    //UPDATE
    public static Proficiency createProficiencyToBeUpdated() {
        return Proficiency.builder()
                .id(1)
                .name("Blacksmith")
                .build();
    }

}
