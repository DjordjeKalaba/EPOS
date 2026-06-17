package com.epos.client_service.api;

import com.epos.client_service.api.dto.ClientUpsertDto;
import com.epos.client_service.domain.Client;
import com.epos.client_service.repo.ClientRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientRepository repo;

    public ClientController(ClientRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Client> list() {
        return repo.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Client create(@Valid @RequestBody ClientUpsertDto dto) {
        Client c = new Client();
        c.setName(dto.name);
        c.setEmail(dto.email);
        c.setPhone(dto.phone);
        c.setAddress(dto.address);
        return repo.save(c);
    }

    @PutMapping("/{id}")
    public Client update(@PathVariable Long id, @Valid @RequestBody ClientUpsertDto dto) {
        Client c = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        c.setName(dto.name);
        c.setEmail(dto.email);
        c.setPhone(dto.phone);
        c.setAddress(dto.address);
        return repo.save(c);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found");
        }
        repo.deleteById(id);
    }
}
