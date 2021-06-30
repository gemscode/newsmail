package com.zeroxcc.delivery.newsletter.utility;

import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.Assert;

public class JpaRepositoryItemReader<T> extends AbstractPagingItemReader<T> {

    private JpaRepository<T, ?> jpaRepository;

    private Sort sort;

    public JpaRepositoryItemReader(JpaRepository<T, ?> jpaRepository) {
        Assert.notNull(jpaRepository, "JpaRepository cannot be null");
        this.jpaRepository = jpaRepository;
    }

    public JpaRepositoryItemReader(JpaRepository<T, ?> jpaRepository, int pageSize) {
        this(jpaRepository);
        setPageSize(pageSize);
    }

    public JpaRepositoryItemReader(JpaRepository<T, ?> jpaRepository, int pageSize, final Sort sort) {
        this(jpaRepository, pageSize);
        this.sort = sort;
    }

    @Override
    protected void doReadPage() {
    }

    @Override
    protected void doJumpToPage(int itemIndex) {
    }
}
