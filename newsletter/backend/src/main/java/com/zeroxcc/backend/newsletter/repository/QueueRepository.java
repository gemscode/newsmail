package com.zeroxcc.backend.newsletter.repository;

import com.zeroxcc.backend.newsletter.model.ScheduleQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public interface QueueRepository extends JpaRepository<ScheduleQueue, UUID> {

    @Query(
            name = "ScheduleQueue.getScheduledQueue",
            nativeQuery = true,
            value = "select td.id as to_do_id, n.content_url as content_url, e.address as email, date(td.scheduled_date) as schedule_date " +
                    "      from Transfers.public.schedule_to_do td " +
                    " left join Transfers.public.schedule_done d on d.to_do_id = td.id " +
                    "inner join Transfers.public.schedule_task t on t.id = td.task_id " +
                    "inner join Transfers.public.newsletter n on n.id = t.newsletter_id " +
                    "inner join Transfers.public.subscription s on s.id = t.subscriptions_id " +
                    "inner join Transfers.public.email e on e.id = s.email_id " +
                    "where date_trunc('day', td.scheduled_date) = date_trunc('day', now() - INTERVAL '0 DAY') " +
                    " ")
    List<ScheduleQueue> getScheduledQueue();

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
            name = "ScheduleQueue.setScheduledQueue",
            nativeQuery = true,
            value= "insert into schedule_queue (to_do_id, content_url, email, schedule_date)\n" +
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
                    "      and q.to_do_id is null;")
    int setScheduledQueue();
}