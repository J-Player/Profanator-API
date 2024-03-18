package api.models.entities;

import api.configs.web.deserializers.InstantJsonDeserializer;
import api.configs.web.serializers.InstantJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@With
@Builder
@Table("Trades")
@NoArgsConstructor
@AllArgsConstructor
public class Trade {

    @Id
    private Integer id;

    @NotEmpty(message = "The 'item' cannot be empty or null")
    private String item;

    @NotNull(message = "The 'quantity' cannot be empty or null")
    @Min(value = 1, message = "The minimum value is 1.")
    private Integer quantity;

    @NotNull(message = "The 'price' cannot be empty or null")
    @Min(value = 1, message = "The minimum value is 1.")
    private Integer price;

    @NotEmpty(message = "The 'seller' cannot be empty or null")
    private String seller;

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

}
