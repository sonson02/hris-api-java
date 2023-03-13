package com.example.hrisapi.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "authorization", schema = "hrisnew")
public class AuthorizationEntity {

    @Id
    private String endpoint;

    private String modulName;

    private String setAuthorize;
}
