package com.lirezende.braavosstore.services;

import com.lirezende.braavosstore.dto.ClientDTO;
import com.lirezende.braavosstore.entities.Client;
import com.lirezende.braavosstore.repositories.ClientRepository;
import com.lirezende.braavosstore.services.exceptions.DatabaseException;
import com.lirezende.braavosstore.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    @Transactional(readOnly = true)
    public Page<ClientDTO> findAllPaged(PageRequest pageRequest) {
        Page<Client> clients = repository.findAll(pageRequest);
        return clients.map(x -> new ClientDTO(x));
    }

    @Transactional(readOnly = true)
    public ClientDTO findById(Long id) {
        Optional<Client> client = repository.findById(id);
        Client entity = client.orElseThrow(() -> new ResourceNotFoundException("Não existem resultados para esta busca."));
        return new ClientDTO(entity);
    }

    @Transactional
    public ClientDTO insert(ClientDTO client) {
        Client entity = new Client();
        entity.setName(client.getName());
        entity.setCpf(client.getCpf());
        entity.setIncome(client.getIncome());
        entity.setBirthDate(client.getBirthDate());
        entity.setChildren(client.getChildren());
        entity = repository.save(entity);
        return new ClientDTO(entity);
    }

    @Transactional
    public ClientDTO update(Long id, ClientDTO client) {
        try {
            Client entity = repository.getOne(id);
            entity.setName(client.getName());
            entity.setCpf(client.getCpf());
            entity.setIncome(client.getIncome());
            entity.setBirthDate(client.getBirthDate());
            entity.setChildren(client.getChildren());
            entity = repository.save(entity);
            return new ClientDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Cliente #" + id + " não encontrado.");
        }
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch(EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Cliente não encontrado #" + id);
        } catch(DataIntegrityViolationException e) {
            throw new DatabaseException("Violação de integridade.");

        }
    }
}
