# bci-usrm-service-api — API de Creación de Usuarios

Ejercicio JAVA — Especialista Integración BCI.
Spring Boot **2.7.18** · Maven · JPA/Hibernate · H2 en memoria · JJWT **0.11.5** · Swagger (springdoc 1.7.0).

## Descripción

API RESTful que implementa el registro de usuarios:

- `POST /api/v1/users` recibe `{ name, email, password, phones:[{ number, citycode, contrycode }] }`
   y responde **201** con `{ id, created, modified, last_login, token, isactive }`.

- Las **expresiones regulares de correo y clave son configurables** vía propiedades
  (`custom.security.email-regex` / `custom.security.password-regex`).

- Persistencia JPA sobre **H2 en memoria**.

## Compilar, probar y ejecutar

```bash
# Compilar
mvn clean package

# pruebas unitarias
mvn test

# Ejecutar (puerto por defecto 8080)
java -jar target/user-registration-api.jar

# Si 8080 esta ocupado, cambiar puerto:
java -jar target/user-registration-api.jar --server.port=8090
```

## Probar la API con curl

### Registro exitoso — esperado HTTP 201

```bash
curl -i -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Juan Rodriguez","email":"juan@rodriguez.org","password":"hunter2","phones":[{"number":"1234567","citycode":"1","contrycode":"57"}]}'
```

Salida:

```
HTTP/1.1 201
{"id":"a8206253-e705-4e74-af1b-516e584e36a1",
 "token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhODIwNjI1My0...",
 "created":"2026-10-07T13:50:09.0812313",
 "modified":"2026-10-07T13:50:09.0812313",
 "last_login":"2026-10-07T13:50:09.0812313",
 "isactive":true}
```

### Correo duplicado — esperado HTTP 409 con mensaje exacto del PDF

Repetir el mismo curl anterior:

```
HTTP/1.1 409
{"mensaje":"El correo ya registrado"}
```

## Consola H2
  **http://localhost:8080/h2-console**:

| Campo | Valor |
|---|---|
| Driver class name | `org.h2.Driver` |
| JDBC URL | `jdbc:h2:mem:userdb` |
| User Name | `sa` |
| Password | *(vacío)* |

## Swagger / OpenAPI

- UI : **http://localhost:8080/swagger-ui.html** 
