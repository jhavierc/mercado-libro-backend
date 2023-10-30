package com.mercadolibro.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class InvoiceRequestDTO {

    public InvoiceRequestDTO(InvoiceDTO invoiceDTO, List<InvoiceItemDTO> invoiceItemDTOList) {
        this.invoiceDTO = invoiceDTO;
        this.invoiceItemDTOList = invoiceItemDTOList;
    }

    @JsonProperty("invoice")
    private InvoiceDTO invoiceDTO;

    @JsonProperty("invoice_item")
    private List<InvoiceItemDTO> invoiceItemDTOList;

}
