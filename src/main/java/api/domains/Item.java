package api.domains;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@NoArgsConstructor
@AllArgsConstructor
@Table("Items")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Item {

    @Id
    @EqualsAndHashCode.Include
    private Long id;

    @EqualsAndHashCode.Include
    private String proficiency;

    @NotBlank(message = "The 'name' cannot be empty or null")
    @EqualsAndHashCode.Include
    private String name;

    @Column("qtbyproduction")
    @NotNull(message = "The 'qtByProduction' cannot be empty or null")
    @Min(value = 1, message = "The minimum value is 1.")
    @Builder.Default
    @EqualsAndHashCode.Include
    private int qtByProduction = 1;

    @Column("created_at")
    @CreatedDate
    private Instant createdAt;

    @Column("updated_at")
    @LastModifiedDate
    private Instant updatedAt;

    @Column("version")
    @Version
    private Long version;

}
