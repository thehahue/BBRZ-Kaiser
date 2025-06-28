package at.bbrz.kaiser.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class Room {

    @Id
    private String uuid;
    private String name;

    @OneToMany(mappedBy = "room")
    private List<User> users;


}
