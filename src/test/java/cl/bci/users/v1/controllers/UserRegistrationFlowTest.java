package cl.bci.users.v1.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.bci.users.v1.dao.UserRepository;
import cl.bci.users.v1.dto.UserResponseDTO;
import cl.bci.users.v1.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserRegistrationFlowTest {

    private static final String VALID_JSON = "{\"name\":\"Juan Rodriguez\","
            + "\"email\":\"juan@rodriguez.org\",\"password\":\"hunter2\","
            + "\"phones\":[{\"number\":\"1234567\",\"citycode\":\"1\","
            + "\"contrycode\":\"57\"}]}";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${custom.jwt.secret}")
    private String jwtSecretBase64;

    @Value("${custom.jwt.issuer}")
    private String jwtIssuer;

    @Test
    void registerShouldReturn201WithUuidPersistedVerifiableJwtAndPhones()
            throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/users")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(VALID_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.modified").exists())
                .andExpect(jsonPath("$.last_login").exists())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.isactive").value(true))
                .andReturn();

        UserResponseDTO dto = this.objectMapper.readValue(
                result.getResponse().getContentAsString(), UserResponseDTO.class);

        UUID persistedId = UUID.fromString(dto.getId());
        assertNotNull(persistedId);

        String[] jwtParts = dto.getToken().split("\\.");
        assertEquals(3, jwtParts.length, "el token debe ser un JWT de 3 partes");

        Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(
                        Base64.getDecoder().decode(this.jwtSecretBase64)))
                .build()
                .parseClaimsJws(dto.getToken());
        Claims claims = jws.getBody();
        assertEquals(dto.getId(), claims.getSubject(),
                "el claim sub debe ser el UUID del usuario persistido");
        assertEquals("juan@rodriguez.org", claims.get("email", String.class));
        assertEquals(this.jwtIssuer, claims.getIssuer());
        assertNotNull(claims.getExpiration(), "el token debe expirar");

        Optional<User> fromDb = this.userRepository.findByEmail("juan@rodriguez.org");
        assertTrue(fromDb.isPresent());
        assertEquals(dto.getToken(), fromDb.get().getToken(),
                "el token retornado debe ser el mismo guardado en la entidad");
        assertEquals(1, fromDb.get().getPhones().size());
        assertEquals("1234567", fromDb.get().getPhones().get(0).getNumber());
        assertEquals("57", fromDb.get().getPhones().get(0).getContrycode());

        LocalDateTime created = fromDb.get().getCreated();
        assertEquals(created, fromDb.get().getLastLogin());
        assertTrue(fromDb.get().isActive());
    }

    @Test
    void duplicatedEmailShouldReturn409WithExactPdfMessage() throws Exception {
        mockMvc.perform(post("/api/v1/users")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(VALID_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/users")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(VALID_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value("El correo ya registrado"));
    }

    @Test
    void weakPasswordOnRealEndpointShouldReturn400Mensaje() throws Exception {
        mockMvc.perform(post("/api/v1/users")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Ana\",\"email\":\"ana@dominio.cl\","
                                + "\"password\":\"sinsol\",\"phones\":[{"
                                + "\"number\":\"999\"}]}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").exists());
    }

}
