package br.com.ifba.imovel.controller;

import br.com.ifba.imovel.dto.ImovelRequestDto;
import br.com.ifba.imovel.dto.ImovelResponseDto;
import java.util.List;
import org.springframework.http.ResponseEntity;

/**
 * Interface do controller para o domínio de Imóvel.
 * Define o contrato dos endpoints REST para a gestão imobiliária.
 */
public interface ImovelIController {

    /**
     * Cadastra um novo imóvel vinculado a um contribuinte.
     *
     * @param dto dados do imóvel a ser cadastrado
     * @return ResponseEntity com status 201 Created e o DTO do imóvel criado
     */
    ResponseEntity<ImovelResponseDto> salvar(ImovelRequestDto dto);

    /**
     * Lista todos os imóveis que estão com status ativo no sistema.
     *
     * @return ResponseEntity com status 200 OK e a lista de DTOs dos imóveis ativos
     */
    ResponseEntity<List<ImovelResponseDto>> listarAtivos();

    /**
     * Busca um imóvel pelo seu identificador único (ID).
     *
     * @param id identificador do imóvel
     * @return ResponseEntity com status 200 OK ou 404 Not Found se inexistente ou inativo
     */
    ResponseEntity<ImovelResponseDto> buscarPorId(Long id);

    /**
     * Atualiza os dados de um imóvel existente.
     *
     * @param id identificador do imóvel a ser atualizado
     * @param dto novos dados do imóvel
     * @return ResponseEntity com status 200 OK e o DTO atualizado, ou 404 Not Found
     */
    ResponseEntity<ImovelResponseDto> atualizar(Long id, ImovelRequestDto dto);

    /**
     * Realiza a exclusão lógica do imóvel (ativo = false).
     *
     * @param id identificador do imóvel
     * @return ResponseEntity com status 204 No Content se inativado, ou 404 Not Found
     */
    ResponseEntity<Void> inativar(Long id);
}
