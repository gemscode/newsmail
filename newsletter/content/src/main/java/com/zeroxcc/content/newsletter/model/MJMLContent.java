package com.zeroxcc.content.newsletter.model;

import com.zeroxcc.content.newsletter.utility.NewsletterStatus;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="content_mjml")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MJMLContent {


    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type = "pg-uuid")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    @Type(type = "com.zeroxcc.content.newsletter.utility.EnumTypePostgreSql")
    private NewsletterStatus status;

    @CreationTimestamp
    @Column(name="created_date")
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name="updated_date")
    private LocalDateTime updatedDate;

    //@OneToOne
    //@JoinColumn(foreignKey = @ForeignKey(name = "fk_newsletter_mjml"))
    @Column(name="newsletter_id")
    private UUID newsletterId;
}
