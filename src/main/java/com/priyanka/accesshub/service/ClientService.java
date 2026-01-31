package com.priyanka.accesshub.service;

import com.priyanka.accesshub.dto.request.ClientDTO;
import com.priyanka.accesshub.dto.response.ClientResponse;
import com.priyanka.accesshub.entity.Client;
import com.priyanka.accesshub.entity.Role;
import com.priyanka.accesshub.mapper.ClientMapper;
import com.priyanka.accesshub.repository.ClientRepository;
import com.priyanka.accesshub.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final RoleRepository roleRepository;
    private final ClientMapper clientMapper;

    public ClientService(ClientRepository clientRepository,
                         RoleRepository roleRepository,
                         ClientMapper clientMapper){
        this.clientRepository = clientRepository;
        this.roleRepository = roleRepository;
        this.clientMapper = clientMapper;
    }

    public ClientResponse onboardClient(ClientDTO request) {
        Client client = clientMapper.toClientEntity(request);
        Client savedClient = clientRepository.save(client);

        // seedDefaultRoles for this client
        seedDefaultRoles(savedClient.getClientId());

        return clientMapper.toClientResponse(savedClient);

    }

    // creating default roles --> User and Admin fo that client
    private void seedDefaultRoles(String clientId) {
        if(roleRepository.findByRoleNameAndClientId("USER",clientId).isEmpty()){
            Role userRole = Role.builder().roleName("USER")
                    .clientId(clientId)
                    .build();

            roleRepository.save(userRole);
        }
        if(roleRepository.findByRoleNameAndClientId("ADMIN",clientId).isEmpty()){
            Role userRole = Role.builder().roleName("ADMIN")
                    .clientId(clientId)
                    .build();
            roleRepository.save(userRole);
        }
    }
}
