package api.domains;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
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
@Table("Ingredient")
public class Ingredient {

    @Id
    private Long id;

    @NotEmpty(message = "The 'product' cannot be empty or null")
    private String product;

    @NotEmpty(message = "The 'name' cannot be empty or null")
    private String name;

    @NotNull(message = "The 'quantity' cannot be empty or null")
    @Min(value = 1, message = "The minimum value is 1.")
    @Builder.Default
    private int quantity = 1;

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
