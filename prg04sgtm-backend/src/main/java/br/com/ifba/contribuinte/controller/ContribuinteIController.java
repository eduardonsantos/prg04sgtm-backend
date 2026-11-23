package br.com.ifba.contribuinte.controller;

import br.com.ifba.contribuinte.dto.ContribuinteRequestDto;
import br.com.ifba.contribuinte.dto.ContribuinteResponseDto;
import java.util.List;
import org.springframework.http.ResponseEntity;

/**
 * Interface do controller para o domínio de Contribuinte.
 * Define o contrato dos endpoints REST para a gestão de contribuintes.
 */
public interface ContribuinteIController {

    /**
     * Cadastra um novo contribuinte.
     *
     * @param dto dados do contribuinte a ser cadastrado
     * @return ResponseEntity com status 201 Created e o DTO do contribuinte criado
     */
    ResponseEntity<ContribuinteResponseDto> salvar(ContribuinteRequestDto dto);

    /**
     * Lista todos os contribuintes que estão com status ativo no sistema.
     *
     * @return ResponseEntity com status 200 OK e a lista de DTOs dos contribuintes ativos
     */
    ResponseEntity<List<ContribuinteResponseDto>> listarAtivos();

    /**
     * Busca um contribuinte pelo seu identificador único (ID).
     *
     * @param id identificador do contribuinte
     * @return ResponseEntity com status 200 OK ou 404 Not Found se inexistente ou inativo
     */
    ResponseEntity<ContribuinteResponseDto> buscarPorId(Long id);

    /**
     * Atualiza os dados de um contribuinte existente.
     *
     * @param id identificador do contribuinte a ser atualizado
     * @param dto novos dados do contribuinte
     * @return ResponseEntity com status 200 OK e o DTO atualizado, ou 404 Not Found
     */
    ResponseEntity<ContribuinteResponseDto> atualizar(Long id, ContribuinteRequestDto dto);

    /**
     * Realiza a exclusão lógica do contribuinte (ativo = false).
     *
     * @param id identificador do contribuinte
     * @return ResponseEntity com status 204 No Content se inativado, ou 404 Not Found
     */
    ResponseEntity<Void> inativar(Long id);
}
