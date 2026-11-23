package br.com.ifba.imposto.dto;

import br.com.ifba.imposto.entity.Imposto;
import br.com.ifba.imposto.entity.ImpostoIPTU;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * DTO record de requisição para cadastro e atualização de Imposto.
 */
public record ImpostoRequestDto(
    @NotBlank(message = "A descrição do imposto é obrigatória")
    String descricao,

    @NotNull(message = "O ano de exercício é obrigatório")
    @Min(value = 2000, message = "O ano de exercício deve ser a partir de 2000")
    Integer anoExercicio,

    @NotNull(message = "A alíquota do terreno é obrigatória")
    @PositiveOrZero(message = "A alíquota do terreno deve ser maior ou igual a zero")
    Double aliquotaTerreno,

    @NotNull(message = "A alíquota da construção é obrigatória")
    @PositiveOrZero(message = "A alíquota da construção deve ser maior ou igual a zero")
    Double aliquotaConstrucao
) {
    public Imposto paraEntidade() {
        ImpostoIPTU iptu = new ImpostoIPTU();
        iptu.setDescricao(this.descricao);
        iptu.setAnoExercicio(this.anoExercicio);
        iptu.setAliquotaTerreno(this.aliquotaTerreno);
        iptu.setAliquotaConstrucao(this.aliquotaConstrucao);
        iptu.setAtivo(true);
        return iptu;
    }
}
