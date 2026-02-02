package com.priyanka.accesshub.service;

import com.priyanka.accesshub.dto.request.ClientDTO;
import com.priyanka.accesshub.dto.response.ClientResponse;
import com.priyanka.accesshub.entity.Client;
import com.priyanka.accesshub.entity.Role;
import com.priyanka.accesshub.mapper.ClientMapper;
import com.priyanka.accesshub.repository.ClientRepository;
import com.priyanka.accesshub.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static com.priyanka.accesshub.constant.UserConstants.ADMIN;
import static com.priyanka.accesshub.constant.UserConstants.USER;


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

    @Transactional
    public Mono<ClientResponse> onboardClient(ClientDTO request) {
        Client client = clientMapper.toClientEntity(request);
        return clientRepository.save(client)
                .flatMap(response->
                        seedDefaultRoles(response.getClientId())
                                .thenReturn(response)
                        )
                .map(clientMapper::toClientResponse);

    }

    // creating default roles --> User and Admin fo that client
    @Transactional
    private Mono<Void> seedDefaultRoles(String clientId) {
       Mono<Role> userRoleMono = roleRepository.findByRoleNameAndClientId(USER, clientId)
               .switchIfEmpty(
                       roleRepository.save(
                               Role.builder().roleName(USER)
                                       .clientId(clientId)
                                       .build()
                       )
               );
        Mono<Role> adminRoleMono = roleRepository.findByRoleNameAndClientId(ADMIN, clientId)
                .switchIfEmpty(
                        roleRepository.save(
                                Role.builder().roleName(ADMIN)
                                        .clientId(clientId)
                                        .build()
                        )
                );

        return Mono.when(userRoleMono,adminRoleMono).then();

    }
}
