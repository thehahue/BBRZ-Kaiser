package at.bbrz.kaiser.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;


@SuperBuilder
@Getter
public class RoomResponse extends AbstractResponse {
    private String uuid;
    private String name;
    private List<User> users;
}
