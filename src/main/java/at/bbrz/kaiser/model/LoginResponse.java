package at.bbrz.kaiser.model;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class LoginResponse extends AbstractResponse {
    private String token;
    private Boolean success;
    private String path;
}
