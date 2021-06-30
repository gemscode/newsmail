package com.zeroxcc.backend.newsletter.model;

import com.zeroxcc.backend.newsletter.utility.NewsletterStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentMJML {


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
    @Type(type = "com.zeroxcc.backend.newsletter.utility.EnumTypePostgreSql")
    private NewsletterStatus status;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_newsletter_mjml"))
    private Newsletter newsletterId;
}
