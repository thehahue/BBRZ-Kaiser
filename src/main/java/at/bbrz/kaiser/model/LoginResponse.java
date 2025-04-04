package at.bbrz.kaiser.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class LoginResponse {
    private String token;
    private String message;
    private Boolean success;
    private String path;
}
