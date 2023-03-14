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
@Table(name = "realm_role_master", schema = "dbo")
public class RealmRoleMasterEntity {

    @Id
    private UUID userRealmRoleId;

    private String realmRole;

    private String userId;
}
