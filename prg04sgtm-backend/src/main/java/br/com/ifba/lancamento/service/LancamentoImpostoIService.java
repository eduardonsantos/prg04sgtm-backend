package br.com.ifba.lancamento.service;

import br.com.ifba.lancamento.entity.LancamentoImposto;
import java.util.List;

/**
 * Contrato de negócio para as operações de LancamentoImposto.
 *
 * @author Sistema de Tributos
 */
public interface LancamentoImpostoIService {

    LancamentoImposto salvar(LancamentoImposto lancamento, Integer quantidadeParcelas);

    LancamentoImposto atualizar(Long id, LancamentoImposto lancamento);

    LancamentoImposto buscarPorId(Long id);

    List<LancamentoImposto> listarAtivos();

    List<LancamentoImposto> findByImovel(Long imovelId);

    void inativar(Long id);
}
