package api.models.entities;

import api.configs.web.deserializers.InstantJsonDeserializer;
import api.configs.web.serializers.InstantJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotEmpty;
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
@Table("Proficiencies")
@NoArgsConstructor
@AllArgsConstructor
public class Proficiency {

    @Id
    private Integer id;

    @NotEmpty(message = "The 'name' cannot be empty or null")
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
