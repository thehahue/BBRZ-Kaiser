package at.bbrz.kaiser.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class RoomResponse {
    private String uuid;
    private String name;
    private List<User> users;
}
