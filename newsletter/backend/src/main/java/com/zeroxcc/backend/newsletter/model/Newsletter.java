package com.zeroxcc.backend.newsletter.model;

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
public class Newsletter {
    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type = "pg-uuid")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_publisher_newsletter"))
    private PublisherAccount publisher;

    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_newsletter_topic"))
    private SubscriptionTopic topic;

    private String contentUrl;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;
}
