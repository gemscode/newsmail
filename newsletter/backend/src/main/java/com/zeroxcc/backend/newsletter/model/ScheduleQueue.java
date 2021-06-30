package com.zeroxcc.backend.newsletter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SqlResultSetMapping(
        name = "queueListMapping",
        classes = {
                @ConstructorResult(
                        targetClass = ScheduleQueue.class,
                        columns = {
                                @ColumnResult(name="toDoId", type= UUID.class),
                                @ColumnResult(name="contentUrl",type=String.class),
                                @ColumnResult(name="email", type=String.class),
                                @ColumnResult(name="scheduleDate", type=LocalDate.class),
                        }
                )
        })
/*@NamedNativeQuery(
        name = "ScheduleQueue.getScheduledQueue",
        query= "select td.id as toDoId, n.content_url as contentUrl, e.address as email, date(td.scheduled_date) as scheduleDate " +
                "      from Transfers.public.schedule_to_do td " +
                " left join Transfers.public.schedule_done d on d.to_do_id = td.id " +
                "inner join Transfers.public.schedule_task t on t.id = td.task_id " +
                "inner join Transfers.public.newsletter n on n.id = t.newsletter_id " +
                "inner join Transfers.public.subscription s on s.id = t.subscriptions_id " +
                "inner join Transfers.public.email e on e.id = s.email_id " +
                "where date_trunc('day', td.scheduled_date) = date_trunc('day', now() - INTERVAL '1 DAY') " +
                "order by td.scheduled_date",
        resultSetMapping = "queueListMapping")
@NamedNativeQuery(
        name = "ScheduleQueue.setScheduledQueue",
        query= "insert into schedule_queue (to_do_id, content_url, email, schedule_date)\n" +
                "        select td.id as to_do_Id, n.content_url as content_url, e.address as email, date(td.scheduled_date) as schedule_date\n" +
                "          from Transfers.public.schedule_to_do td\n" +
                "     left join Transfers.public.schedule_done d on d.to_do_id = td.id\n" +
                "    inner join Transfers.public.schedule_task t on t.id = td.task_id\n" +
                "    inner join Transfers.public.newsletter n on n.id = t.newsletter_id\n" +
                "    inner join Transfers.public.subscription s on s.id = t.subscriptions_id\n" +
                "    inner join Transfers.public.email e on e.id = s.email_id\n" +
                "     left join Transfers.public.schedule_queue q on q.to_do_id = td.id\n" +
                "    where date_trunc('day',td.scheduled_date) = date_trunc('day',now() - INTERVAL '0 DAY')\n" +
                "      and d.to_do_id is null\n" +
                "      and q.to_do_id is null;",
        resultClass = ScheduleQueue.class)*/
public class ScheduleQueue {
    @Id
    private UUID toDoId;
    private String contentUrl;
    private String email;
    private LocalDate scheduleDate;
}
