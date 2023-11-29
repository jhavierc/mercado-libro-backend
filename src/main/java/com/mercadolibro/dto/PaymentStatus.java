package com.mercadolibro.dto;


import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@ApiModel(description = "Payment Statuses", value = "PaymentStatus")
public enum PaymentStatus {
    APRO("APRO","Pago aprobado", "El pago ha sido aprobado exitosamente."),
    OTHE("OTHE","Rechazado por error general", "El pago ha sido rechazado debido a un error general."),
    CONT("CONT","Pendiente de pago", "El pago está pendiente."),
    CALL("CALL","Rechazado con validación para autorizar", "El pago ha sido rechazado y necesita validación para autorizar."),
    FUND("FUND","Rechazado por importe insuficiente", "El pago ha sido rechazado debido a fondos insuficientes."),
    SECU("SECU","Rechazado por código de seguridad inválido", "El pago ha sido rechazado debido a un código de seguridad inválido."),
    EXPI("EXPI","Rechazado debido a un problema de fecha de vencimiento", "El pago ha sido rechazado debido a un problema con la fecha de vencimiento."),
    FORM("FORM","Rechazado debido a un error de formulario", "El pago ha sido rechazado debido a un error en el formulario de pago.");

    private final String id;
    private final String status;
    private final String details;

    public static PaymentStatus getById(String id) {
        for(PaymentStatus e : values()) {
            if(e.id.equals(id)) return e;
        }
        throw new IllegalArgumentException("Invalid PaymentStatus ID: " + id);
    }
}
