package com.priyanka.accesshub.mapper;

import com.priyanka.accesshub.dto.request.ClientDTO;
import com.priyanka.accesshub.dto.response.ClientResponse;
import com.priyanka.accesshub.entity.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public Client toClientEntity(ClientDTO clientDTO){
        return Client.builder()
                .clientId(clientDTO.getClientId())
                .name(clientDTO.getName())
                .build();
    }

    public ClientResponse toClientResponse(Client savedClient) {

        return ClientResponse.builder()
                .id(savedClient.getId())
                .name(savedClient.getName())
                .clientId(savedClient.getClientId())
                .build();
    }
}
