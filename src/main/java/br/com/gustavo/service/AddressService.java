package br.com.gustavo.service;

import br.com.gustavo.domain.address.Address;
import br.com.gustavo.domain.event.Event;
import br.com.gustavo.domain.event.EventRequestDTO;
import br.com.gustavo.repositories.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository repository;

    public Address createAddress(EventRequestDTO data, Event event){

        Address address = new Address();
        address.setCity(data.city());
        address.setUf(data.state());
        address.setEvent(event);

        return repository.save(address);
    }
}
