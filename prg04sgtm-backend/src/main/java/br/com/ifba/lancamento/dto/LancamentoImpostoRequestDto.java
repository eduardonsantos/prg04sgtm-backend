package br.com.ifba.lancamento.dto;

import br.com.ifba.imovel.entity.Imovel;
import br.com.ifba.imposto.entity.ImpostoIPTU;
import br.com.ifba.lancamento.entity.LancamentoImposto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO record de requisição para cadastro de Lançamento de Imposto.
 */
public record LancamentoImpostoRequestDto(
    @NotNull(message = "O ID do imóvel é obrigatório")
    Long imovelId,

    @NotNull(message = "O ID do imposto é obrigatório")
    Long impostoId,

    @NotNull(message = "A quantidade de parcelas é obrigatória")
    @Min(value = 1, message = "A quantidade mínima de parcelas é 1")
    @Max(value = 12, message = "A quantidade máxima de parcelas é 12")
    Integer quantidadeParcelas
) {
    public LancamentoImposto paraEntidade() {
        LancamentoImposto lancamento = new LancamentoImposto();
        if (this.imovelId != null) {
            Imovel imovel = new Imovel();
            imovel.setId(this.imovelId);
            lancamento.setImovel(imovel);
        }
        if (this.impostoId != null) {
            ImpostoIPTU imposto = new ImpostoIPTU();
            imposto.setId(this.impostoId);
            lancamento.setImposto(imposto);
        }
        lancamento.setAtivo(true);
        return lancamento;
    }
}
