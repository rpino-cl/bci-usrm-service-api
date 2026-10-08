package cl.bci.users.v1.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.bci.users.v1.model.Phone;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, UUID> {

}
