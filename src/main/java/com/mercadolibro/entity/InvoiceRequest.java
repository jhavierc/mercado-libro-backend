package com.mercadolibro.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class InvoiceRequest {

    private Invoice invoice;
    private List<InvoiceItem> invoiceItemList;

    public Invoice getInvoice() {
        return invoice;
    }

    public List<InvoiceItem> getInvoiceItemList() {
        return invoiceItemList;
    }
}
