package com.zeroxcc.backend.newsletter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDone {
    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type = "pg-uuid")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_schedule_todo_done"))
    private  ScheduleToDo toDo;

    @CreationTimestamp
    private LocalDateTime createdDate;
}
