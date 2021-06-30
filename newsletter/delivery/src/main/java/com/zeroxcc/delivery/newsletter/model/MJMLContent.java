package com.zeroxcc.delivery.newsletter.model;

import com.zeroxcc.delivery.newsletter.utility.NewsletterStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
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
    @Type(type = "org.hibernate.type.TextType")
    private String content;

    @Enumerated(EnumType.STRING)
    @Type(type = "com.zeroxcc.delivery.newsletter.utility.EnumTypePostgreSql")
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
