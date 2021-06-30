package com.zeroxcc.backend.newsletter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type = "pg-uuid")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @NotEmpty(message = "Street line cannot be empty")
    @Column(name = "StreetLine", nullable = false)
    private String streetLine;

    private String streetLine1;

    @NotEmpty(message = "City cannot be empty")
    @Column(name = "City", nullable = false, unique = true)
    private String city;

    @NotEmpty(message = "State cannot be empty")
    @Column(name = "State", nullable = false, unique = true)
    private String state;

    @NotEmpty(message = "Postal code cannot be empty")
    @Column(name = "PostalCode", nullable = false, unique = true)
    private String postalCode;

    @NotEmpty(message = "Country cannot be empty")
    @Column(name = "Country", nullable = false, unique = true)
    private String country;
}