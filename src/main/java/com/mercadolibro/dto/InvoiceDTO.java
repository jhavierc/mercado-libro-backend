package com.mercadolibro.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class InvoiceDTO {

    public InvoiceDTO(InvoiceInfoDTO invoiceInfoDTO, List<InvoiceItemDTO> invoiceItemDTOList) {
        this.invoiceInfoDTO = invoiceInfoDTO;
        this.invoiceItemDTOList = invoiceItemDTOList;
    }

    private InvoiceInfoDTO invoiceInfoDTO;
    private List<InvoiceItemDTO> invoiceItemDTOList;

}
