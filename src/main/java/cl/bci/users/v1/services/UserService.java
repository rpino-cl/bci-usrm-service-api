package cl.bci.users.v1.services;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import cl.bci.users.v1.dao.UserRepository;
import cl.bci.users.v1.dto.PhoneDTO;
import cl.bci.users.v1.dto.UserRequestDTO;
import cl.bci.users.v1.dto.UserResponseDTO;
import cl.bci.users.v1.exception.UserException;
import cl.bci.users.v1.model.User;
import cl.bci.users.v1.security.JwtTokenProvider;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    static final String DUPLICATE_EMAIL_MESSAGE = "El correo ya registrado";

    private final UserRepository userRepository;

    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public UserResponseDTO register(UserRequestDTO request) {
        if (this.userRepository.existsByEmail(request.getEmail())) {
            LOG.warn("Intento de registro con correo duplicado: {}", request.getEmail());
            throw new UserException(DUPLICATE_EMAIL_MESSAGE, HttpStatus.CONFLICT);
        }

        User user = new User(request.getName(), request.getEmail(), request.getPassword());
        for (PhoneDTO phoneDto : request.getPhones()) {
            user.addPhone(phoneDto.toEntity());
        }

        LocalDateTime now = LocalDateTime.now();
        user.setActive(true);
        user.setCreated(now);
        user.setModified(now);
        user.setLastLogin(now);

        User saved;
        try {
            saved = this.userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException ex) {
            LOG.warn("Constraint de correo unico violado en carrera: {}",
                    request.getEmail());
            throw new UserException(DUPLICATE_EMAIL_MESSAGE, HttpStatus.CONFLICT, ex);
        }

        String token = this.jwtTokenProvider.generateToken(saved);
        saved.setToken(token);

        LOG.info("Usuario registrado id={} email={}", saved.getId(), saved.getEmail());
        return UserResponseDTO.fromEntity(saved);
    }

}
