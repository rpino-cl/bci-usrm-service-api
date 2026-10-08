package cl.bci.users.v1.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponseDTO {

    private String mensaje;

    public ErrorResponseDTO() {
        super();
    }

    public ErrorResponseDTO(String mensaje) {
        super();
        this.mensaje = mensaje;
    }

}
