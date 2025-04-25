package at.bbrz.kaiser.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Table(name = "users") // Changed Table-Name because H2 has already a "user" table
public class User {
    @Id
    private String name;
    private String password;
}
