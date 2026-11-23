package br.com.ifba.pagamento.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO record de requisição para registro de Pagamento (baixa manual de boleto).
 */
public record PagamentoRequestDto(
    @NotNull(message = "O ID do boleto é obrigatório")
    Long boletoId,

    @NotNull(message = "O ID do usuário operador é obrigatório")
    Long usuarioId
) {}
