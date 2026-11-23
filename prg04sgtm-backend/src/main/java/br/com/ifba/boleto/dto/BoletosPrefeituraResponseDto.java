package br.com.ifba.boleto.dto;

import br.com.ifba.boleto.entity.BoletosPrefeitura;
import java.time.LocalDate;

/**
 * DTO record de resposta para Boleto da Prefeitura.
 */
public record BoletosPrefeituraResponseDto(
    Long id,
    String numeroCodigoBarras,
    Double valorBoleto,
    LocalDate dataVencimento,
    String status,
    Boolean ativo,
    Long lancamentoId
) {
    public static BoletosPrefeituraResponseDto deEntidade(BoletosPrefeitura boleto) {
        if (boleto == null) {
            return null;
        }
        Long lancamentoId = (boleto.getLancamentoImposto() != null) ? boleto.getLancamentoImposto().getId() : null;
        return new BoletosPrefeituraResponseDto(
            boleto.getId(),
            boleto.getNumeroCodigoBarras(),
            boleto.getValorBoleto(),
            boleto.getDataVencimento(),
            boleto.getStatus(),
            boleto.getAtivo(),
            lancamentoId
        );
    }
}
