package br.com.ifba.lancamento.dto;

import br.com.ifba.lancamento.entity.LancamentoImposto;
import java.time.LocalDate;

/**
 * DTO record de resposta para Lançamento de Imposto.
 */
public record LancamentoImpostoResponseDto(
    Long id,
    Integer ano,
    Double valorTotalCalculado,
    LocalDate dataLancamento,
    Boolean ativo,
    Long imovelId,
    String imovelInscricao,
    Long impostoId,
    String impostoDescricao,
    Integer quantidadeBoletos
) {
    public static LancamentoImpostoResponseDto deEntidade(LancamentoImposto lancamento) {
        if (lancamento == null) {
            return null;
        }
        Long imovelId = (lancamento.getImovel() != null) ? lancamento.getImovel().getId() : null;
        String imovelInscricao = (lancamento.getImovel() != null) ? lancamento.getImovel().getInscricaoImobiliaria() : null;
        Long impostoId = (lancamento.getImposto() != null) ? lancamento.getImposto().getId() : null;
        String impostoDescricao = (lancamento.getImposto() != null) ? lancamento.getImposto().getDescricao() : null;
        int qtdBoletos = (lancamento.getBoletos() != null) ? lancamento.getBoletos().size() : 0;

        return new LancamentoImpostoResponseDto(
            lancamento.getId(),
            lancamento.getAno(),
            lancamento.getValorTotalCalculado(),
            lancamento.getDataLancamento(),
            lancamento.getAtivo(),
            imovelId,
            imovelInscricao,
            impostoId,
            impostoDescricao,
            qtdBoletos
        );
    }
}
