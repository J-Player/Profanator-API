package api.util;

import api.domain.Proficiency;
import api.request.post.ProficiencyPostRequestBody;
import api.request.put.ProficiencyPutRequestBody;

public abstract class ProficiencyCreator {

    private static final Integer ID = 1;
    private static final String NAME = "Proficiency";

    public static Proficiency proficiency() {
        return Proficiency.builder()
                .id(ID)
                .name(NAME)
                .build();
    }

    public static ProficiencyPostRequestBody proficiencyToSave() {
        return ProficiencyPostRequestBody.builder()
                .name(NAME)
                .build();
    }

    public static ProficiencyPutRequestBody proficiencyToUpdate() {
        return ProficiencyPutRequestBody.builder()
                .id(ID)
                .name(NAME)
                .build();
    }

    public static ProficiencyPostRequestBody invalidProficiencyToSave() {
        return ProficiencyPostRequestBody.builder()
                .name(null)
                .build();
    }

    public static ProficiencyPutRequestBody invalidProficiencyToUpdate() {
        return ProficiencyPutRequestBody.builder()
                .id(null)
                .name(null)
                .build();
    }

}
