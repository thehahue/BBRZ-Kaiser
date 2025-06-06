package at.bbrz.kaiser.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;


@SuperBuilder
@Getter
@Setter
public class RoomResponse extends AbstractResponse {
    private String uuid;
    private String name;
    private List<User> users;
}
