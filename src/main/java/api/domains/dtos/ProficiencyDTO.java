package api.domains.dtos;

import jakarta.validation.constraints.NotEmpty;

public record ProficiencyDTO (@NotEmpty(message = "The 'name' cannot be empty or null") String name) {}
