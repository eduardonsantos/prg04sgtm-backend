package br.com.ifba.boleto.service;

import br.com.ifba.boleto.entity.BoletosPrefeitura;
import java.util.List;

/**
 
 * @author eduardo
 */
public interface BoletosPrefeituraIService {

    BoletosPrefeitura salvar(BoletosPrefeitura boleto);

    BoletosPrefeitura atualizar(Long id, BoletosPrefeitura boleto);

    BoletosPrefeitura buscarPorId(Long id);

    List<BoletosPrefeitura> listarAtivos();

    List<BoletosPrefeitura> listarPorLancamento(Long lancamentoId);

    List<BoletosPrefeitura> listarPendentes();

    BoletosPrefeitura atualizarStatus(Long id, String novoStatus);

    void inativar(Long id);
}
