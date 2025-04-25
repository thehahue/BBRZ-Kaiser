package at.bbrz.kaiser.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RoomRequest {
    private String name;
    private String roomId;
}
