package br.com.ifba.boleto.dto;

import br.com.ifba.boleto.entity.BoletosPrefeitura;
import br.com.ifba.lancamento.entity.LancamentoImposto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

/**
 * DTO record de requisição para criação/atualização de Boleto da Prefeitura.
 */
public record BoletosPrefeituraRequestDto(
    String numeroCodigoBarras,

    @NotNull(message = "O valor do boleto é obrigatório")
    @Positive(message = "O valor do boleto deve ser maior que zero")
    Double valorBoleto,

    @NotNull(message = "A data de vencimento é obrigatória")
    LocalDate dataVencimento,

    String status,

    Long lancamentoId
) {
    public BoletosPrefeitura paraEntidade() {
        BoletosPrefeitura boleto = new BoletosPrefeitura();
        boleto.setNumeroCodigoBarras(this.numeroCodigoBarras);
        boleto.setValorBoleto(this.valorBoleto);
        boleto.setDataVencimento(this.dataVencimento);
        boleto.setStatus(this.status != null ? this.status : "PENDENTE");
        boleto.setAtivo(true);
        if (this.lancamentoId != null) {
            LancamentoImposto lancamento = new LancamentoImposto();
            lancamento.setId(this.lancamentoId);
            boleto.setLancamentoImposto(lancamento);
        }
        return boleto;
    }
}
