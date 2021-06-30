package com.zeroxcc.backend.newsletter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email {
    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type = "pg-uuid")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_user_account_email"))
    private UserAccount userAccount;

    @Column(unique = true)
    private String address;
    private Boolean isPrimary;

    public Email(UserAccount userAccount, String address, Boolean isPrimary) {
        this.userAccount = userAccount;
        this.address = address;
        this.isPrimary = isPrimary;
    }
}
