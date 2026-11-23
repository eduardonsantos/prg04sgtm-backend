package br.com.ifba.boleto.entity;

import br.com.ifba.infrastructure.entity.PersistenceEntity;
import br.com.ifba.infrastructure.util.GeradorDocumentos;
import br.com.ifba.lancamento.entity.LancamentoImposto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 
 * @author eduardo
 */
@Entity
@Table(name = "tb_boleto_prefeitura")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoletosPrefeitura extends PersistenceEntity implements GeradorDocumentos {

    @Column(nullable = false, unique = true, length = 44)
    private String numeroCodigoBarras;

    @Column(nullable = false)
    private Double valorBoleto;

    @Column(nullable = false)
    private LocalDate dataVencimento;

    // Valores possíveis: "PENDENTE" | "PAGO" | "VENCIDO"
    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Boolean ativo = true;

    @ManyToOne
    @JoinColumn(name = "lancamento_imposto_id", nullable = false)
    private LancamentoImposto lancamentoImposto;

    public Boolean verificarVencimento() {
        return LocalDate.now().isAfter(this.dataVencimento);
    }

    /**
     * Transição de domínio rico: liquidação do boleto (quitar).
     */
    public void quitar() {
        this.status = "PAGO";
    }

    /**
     * Transição de domínio rico: estorno/reabertura do boleto para pendente.
     */
    public void estornar() {
        this.status = "PENDENTE";
    }

    /**
     * Transição de domínio rico: inativação lógica do boleto.
     */
    public void inativar() {
        this.ativo = false;
    }

    public void atualizarStatusParaPago() {
        quitar();
    }

    @Override
    public void imprimir() {
        System.out.printf(
            "========== BOLETO PREFEITURA ==========%n" +
            "Cód. Barras : %s%n"                        +
            "Valor       : R$ %.2f%n"                   +
            "Vencimento  : %s%n"                        +
            "Status      : %s%n"                        +
            "=======================================%n",
            numeroCodigoBarras, valorBoleto, dataVencimento, status
        );
    }
}
