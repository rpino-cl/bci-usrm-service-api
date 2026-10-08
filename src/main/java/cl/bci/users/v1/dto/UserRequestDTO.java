package cl.bci.users.v1.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import cl.bci.users.v1.validation.ValidEmail;
import cl.bci.users.v1.validation.ValidPassword;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 120, message = "El nombre no puede superar los 120 caracteres")
    private String name;

    @NotBlank(message = "El correo es obligatorio")
    @ValidEmail
    @Size(max = 160, message = "El correo no puede superar los 160 caracteres")
    private String email;

    @NotBlank(message = "La clave es obligatoria")
    @ValidPassword
    private String password;

    /**
     * @Valid propaga la validacion a cada PhoneDTO de la lista.
     */
    @NotNull(message = "El listado de telefonos no puede ser nulo")
    @Size(min = 1, max = 10, message = "Se requiere al menos un telefono (maximo 10)")
    private List<PhoneDTO> phones = new ArrayList<PhoneDTO>();

    public UserRequestDTO() {
        super();
    }

}
