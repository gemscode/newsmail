package com.zeroxcc.backend.newsletter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {
    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type = "pg-uuid")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_user_profile_account"))
    private UserAccount account;

    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_user_profile_address"))
    private Address address;

    @OneToMany
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_user_profile_emails"))
    private Set<Email> emails = new HashSet<>();

    private String pictureUrl;
}