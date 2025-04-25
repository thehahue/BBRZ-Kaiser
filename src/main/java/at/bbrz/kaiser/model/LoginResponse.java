package at.bbrz.kaiser.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class LoginResponse extends AbstractResponse {
    private String token;
    private Boolean success;
    private String path;
}
