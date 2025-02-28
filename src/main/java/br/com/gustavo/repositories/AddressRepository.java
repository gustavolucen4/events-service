package br.com.gustavo.repositories;

import br.com.gustavo.domain.address.Address;
import br.com.gustavo.domain.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
}
