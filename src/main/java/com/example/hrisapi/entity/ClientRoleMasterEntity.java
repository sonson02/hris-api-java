package com.example.hrisapi.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "client_role_master", schema = "hrisnew")
public class ClientRoleMasterEntity {

    @Id
    private UUID userClientRoleId;

    private String clientRole;

    private String resource;

    private String userId;
}
