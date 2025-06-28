package at.bbrz.kaiser.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @ManyToOne
    @Setter
    @JsonIgnore
    private Room room;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                '}';
    }
}
