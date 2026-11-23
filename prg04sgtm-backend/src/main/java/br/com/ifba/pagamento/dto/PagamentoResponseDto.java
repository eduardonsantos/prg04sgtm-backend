package br.com.ifba.pagamento.dto;

import br.com.ifba.pagamento.entity.Pagamento;
import java.time.LocalDate;

/**
 * DTO record de resposta para Pagamento.
 */
public record PagamentoResponseDto(
    Long id,
    LocalDate dataPagamento,
    Double valorPago,
    String status,
    Boolean ativo,
    Long boletoId,
    Long usuarioId,
    String usuarioNome
) {
    public static PagamentoResponseDto deEntidade(Pagamento pagamento) {
        if (pagamento == null) {
            return null;
        }
        Long boletoId = (pagamento.getBoletoPrefeitura() != null) ? pagamento.getBoletoPrefeitura().getId() : null;
        Long usuarioId = (pagamento.getUsuario() != null) ? pagamento.getUsuario().getId() : null;
        String usuarioNome = (pagamento.getUsuario() != null) ? pagamento.getUsuario().getNome() : null;

        return new PagamentoResponseDto(
            pagamento.getId(),
            pagamento.getDataPagamento(),
            pagamento.getValorPago(),
            pagamento.getStatus(),
            pagamento.getAtivo(),
            boletoId,
            usuarioId,
            usuarioNome
        );
    }
}
