package com.mercadolibro.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class InvoiceCreated extends ApplicationEvent {
    private Integer userId;
    private List<Long> bookIds;

    public InvoiceCreated(Integer userId, List<Long> bookIds) {
        super(userId);
        this.userId = userId;
        this.bookIds = bookIds;
    }
}
