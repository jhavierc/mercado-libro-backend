package com.mercadolibro.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Cacheable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Cacheable(false)
public class InvoiceRequest {

    @JsonProperty("invoice")
    private Invoice invoice;

    @JsonProperty("invoice_item")
    private List<InvoiceItem> invoiceItemList;

    public Invoice getInvoice() {
        return invoice;
    }

    public List<InvoiceItem> getInvoiceItemList() {
        return invoiceItemList;
    }
}
