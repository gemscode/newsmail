package com.zeroxcc.backend.newsletter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublisherAccount {
    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type = "pg-uuid")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_publisher_account"))
    private Account account;

    @NotEmpty(message = "User Id cannot be empty")
    @Column(name = "UserId", nullable = false, unique = true)
    private String userId;

    @NotEmpty(message = "Password cannot be empty")
    @Column(name = "Password", nullable = false, unique = true)
    private String password;

    @NotEmpty(message = "Publisher key cannot be empty")
    @Column(name = "PublisherKey", nullable = false, unique = true)
    private String publisherKey;
}
