package br.com.ifba.imovel.service;

import br.com.ifba.imovel.entity.Imovel;
import java.util.List;

/**
 * Contrato de negócio para as operações de Imovel.
 * Espelho de ContribuinteIService e UsuarioIService.
 */
public interface ImovelIService {

    Imovel salvar(Imovel imovel);

    Imovel atualizar(Long id, Imovel imovel);

    Imovel buscarPorId(Long id);

    List<Imovel> listarAtivos();

    List<Imovel> listarPorContribuinteAtivo(Long contribuinteId);

    void inativar(Long id);
}
