package cl.bci.users.v1.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import cl.bci.users.v1.model.Phone;
import cl.bci.users.v1.model.User;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhoneRepository phoneRepository;

    private User buildJuan() {
        User user = new User("Juan Rodriguez", "juan@rodriguez.org", "hunter2");
        user.setToken("eyJhbGciOiJIUzI1NiJ0.prueba.firma");
        user.addPhone(new Phone("1234567", "1", "57"));
        return user;
    }

    @Test
    void shouldPersistUserWithUuidPhonesAndAuditFields() {
        User saved = userRepository.saveAndFlush(buildJuan());

        assertNotNull(saved.getId(), "el UUID debe autogenerarse");
        assertNotNull(saved.getCreated(), "created via @PrePersist");
        assertEquals(saved.getCreated(), saved.getLastLogin(),
                "PDF: last_login de usuario nuevo coincide con created");
        assertTrue(saved.isActive(), "usuario nuevo queda habilitado");

        assertTrue(phoneRepository.existsById(
                saved.getPhones().get(0).getId()));

        Optional<User> found = userRepository.findByEmail("juan@rodriguez.org");
        assertTrue(found.isPresent());
        assertEquals(1, found.get().getPhones().size());
        assertEquals("1234567", found.get().getPhones().get(0).getNumber());
    }

    @Test
    void existsByEmailShouldSupportDuplicateRegistrationError() {
        userRepository.saveAndFlush(buildJuan());

        assertTrue(userRepository.existsByEmail("juan@rodriguez.org"));
        assertFalse(userRepository.existsByEmail("otro@dominio.cl"));
    }

    @Test
    void shouldRejectDuplicatedEmailAtConstraintLevel() {
        userRepository.saveAndFlush(buildJuan());

        User duplicate = new User("Otro Juan", "juan@rodriguez.org", "hunter3");
        // anonima de la interfaz funcional Executable (soportada desde Java 8).
        assertThrows(DataIntegrityViolationException.class,
                new org.junit.jupiter.api.function.Executable() {
                    @Override
                    public void execute() throws Throwable {
                        userRepository.saveAndFlush(duplicate);
                    }
                }, "el unique de email debe bloquear el duplicado");
    }

}
