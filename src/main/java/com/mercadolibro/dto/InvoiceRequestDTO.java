package com.mercadolibro.dto;

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

    private InvoiceDTO invoiceDTO;
    private List<InvoiceItemDTO> invoiceItemDTOList;

}
