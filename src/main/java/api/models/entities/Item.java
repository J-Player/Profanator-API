package api.models.entities;

import api.configs.web.deserializers.InstantJsonDeserializer;
import api.configs.web.serializers.InstantJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.With;
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
@Table("Items")
public class Item {

    @Id
    private Integer id;

    private String proficiency;

    @NotEmpty(message = "The 'name' cannot be empty or null")
    private String name;

    @Column("qtbyproduction")
    @NotNull(message = "The 'qtByProduction' cannot be empty or null")
    @Min(value = 1, message = "The minimum value is 1.")
    @Builder.Default
    private int qtByProduction = 1;

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
