package api.models.entities;

<<<<<<< HEAD:src/main/java/api/models/entities/Proficiency.java
import api.configs.web.deserializers.InstantJsonDeserializer;
import api.configs.web.serializers.InstantJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotEmpty;
=======
import jakarta.validation.constraints.NotBlank;
>>>>>>> main:src/main/java/api/domains/Proficiency.java
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@With
@Builder
<<<<<<< HEAD:src/main/java/api/models/entities/Proficiency.java
@Table("Proficiencies")
@NoArgsConstructor
@AllArgsConstructor
public class Proficiency {

    @Id
    private Integer id;
=======
@NoArgsConstructor
@AllArgsConstructor
@Table("Proficiencies")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Proficiency {

    @Id
    @EqualsAndHashCode.Include
    private Long id;
>>>>>>> main:src/main/java/api/domains/Proficiency.java

    @EqualsAndHashCode.Include
    @NotBlank(message = "The 'name' cannot be empty or null")
    private String name;

    @Column("created_at")
    @CreatedDate
    @JsonDeserialize(using = InstantJsonDeserializer.class)
    @JsonSerialize(using = InstantJsonSerializer.class)
    private Instant createdAt;

    @Column("updated_at")
    @LastModifiedDate
    @JsonDeserialize(using = InstantJsonDeserializer.class)
    @JsonSerialize(using = InstantJsonSerializer.class)
    private Instant updatedAt;

    @Column("version")
    @Version
    private Long version;

}
