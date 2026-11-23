package br.com.ifba.boleto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * DTO record de requisição para atualização de status de Boleto.
 */
public record BoletoStatusRequestDto(
    @NotBlank(message = "O status é obrigatório")
    @Pattern(regexp = "PENDENTE|PAGO|VENCIDO", message = "O status deve ser: PENDENTE, PAGO ou VENCIDO")
    String status
) {}
