package at.bbrz.kaiser.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class RegistrationResponse extends AbstractResponse {
    private Boolean success;
}
