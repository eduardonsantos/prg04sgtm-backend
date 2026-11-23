package br.com.ifba.pagamento.service;

import br.com.ifba.pagamento.entity.Pagamento;
import java.util.List;

/**
 * Contrato de negócio para as operações de Pagamento.
 *
 * @author Sistema de Tributos
 */
public interface PagamentoIService {

    Pagamento salvar(Long boletoId, Long usuarioId);

    Pagamento atualizar(Long id, Pagamento pagamento);

    Pagamento buscarPorId(Long id);

    List<Pagamento> listarAtivos();

    List<Pagamento> listarPorUsuario(Long usuarioId);

    void estornar(Long id);
}
