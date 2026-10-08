package cl.bci.users.v1.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import cl.bci.users.v1.model.Phone;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PhoneDTO {

    @NotBlank(message = "El numero de telefono es obligatorio")
    @Size(max = 30, message = "El numero no puede superar los 30 caracteres")
    private String number;

    @Size(max = 10, message = "El citycode no puede superar los 10 caracteres")
    private String citycode;

    @Size(max = 10, message = "El contrycode no puede superar los 10 caracteres")
    private String contrycode;

    public PhoneDTO() {
        super();
    }

    /** Mapper de vuelta a la entidad (usado por el futuro servicio). */
    public Phone toEntity() {
        return new Phone(this.number, this.citycode, this.contrycode);
    }

}
