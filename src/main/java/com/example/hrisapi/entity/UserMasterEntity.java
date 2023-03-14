package com.example.hrisapi.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_master", schema = "dbo")
public class UserMasterEntity {

    @Id
    private String userId;

    private Date createdAt;

    private String email;

    private String firstName;

    private Boolean isActive;

    private String lastName;
}
