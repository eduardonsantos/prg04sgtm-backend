package br.com.ifba.imposto.dto;

import br.com.ifba.imposto.entity.Imposto;
import br.com.ifba.imposto.entity.ImpostoIPTU;

/**
 * DTO record de resposta para Imposto.
 */
public record ImpostoResponseDto(
    Long id,
    String descricao,
    Integer anoExercicio,
    Double aliquotaTerreno,
    Double aliquotaConstrucao,
    Boolean ativo
) {
    public static ImpostoResponseDto deEntidade(Imposto imposto) {
        if (imposto == null) {
            return null;
        }
        Double terreno = null;
        Double construcao = null;
        if (imposto instanceof ImpostoIPTU iptu) {
            terreno = iptu.getAliquotaTerreno();
            construcao = iptu.getAliquotaConstrucao();
        }
        return new ImpostoResponseDto(
            imposto.getId(),
            imposto.getDescricao(),
            imposto.getAnoExercicio(),
            terreno,
            construcao,
            imposto.getAtivo()
        );
    }
}
