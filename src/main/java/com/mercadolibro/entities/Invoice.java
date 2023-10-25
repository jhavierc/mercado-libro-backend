package com.mercadolibro.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Entity;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Invoice {

    private InvoiceInfo invoiceInfo;
    private List<InvoiceItem> invoiceItemList;

    public InvoiceInfo getInvoiceInfo() {
        return invoiceInfo;
    }

    public List<InvoiceItem> getInvoiceItemList() {
        return invoiceItemList;
    }
}
