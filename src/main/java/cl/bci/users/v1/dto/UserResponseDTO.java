package cl.bci.users.v1.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import cl.bci.users.v1.model.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserResponseDTO {

    private String id;

    @JsonProperty("created")
    private LocalDateTime created;

    @JsonProperty("modified")
    private LocalDateTime modified;

    @JsonProperty("last_login")
    private LocalDateTime lastLogin;

    private String token;

    @JsonProperty("isactive")
    private boolean isActive;

    public UserResponseDTO() {
        super();
    }

    public static UserResponseDTO fromEntity(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId() == null ? null : user.getId().toString());
        dto.setCreated(user.getCreated());
        dto.setModified(user.getModified());
        dto.setLastLogin(user.getLastLogin());
        dto.setToken(user.getToken());
        dto.setActive(user.isActive());
        return dto;
    }

}
