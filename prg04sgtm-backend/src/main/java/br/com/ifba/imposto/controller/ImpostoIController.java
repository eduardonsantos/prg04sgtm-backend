package br.com.ifba.imposto.controller;

import br.com.ifba.imposto.dto.ImpostoRequestDto;
import br.com.ifba.imposto.dto.ImpostoResponseDto;
import java.util.List;
import org.springframework.http.ResponseEntity;

/**
 * Interface do controller para o domínio de Imposto.
 * Define o contrato dos endpoints REST para gerenciamento tributário municipal.
 */
public interface ImpostoIController {

    /**
     * Cadastra uma nova configuração tributária de imposto (ex: IPTU com alíquotas).
     *
     * @param dto dados do imposto a ser cadastrado
     * @return ResponseEntity com status 201 Created e o DTO do imposto criado
     */
    ResponseEntity<ImpostoResponseDto> salvar(ImpostoRequestDto dto);

    /**
     * Lista todos os impostos que estão com status ativo no sistema.
     *
     * @return ResponseEntity com status 200 OK e a lista de DTOs dos impostos ativos
     */
    ResponseEntity<List<ImpostoResponseDto>> listarAtivos();

    /**
     * Busca um imposto pelo seu identificador único (ID).
     *
     * @param id identificador do imposto
     * @return ResponseEntity com status 200 OK ou 404 Not Found se inexistente ou inativo
     */
    ResponseEntity<ImpostoResponseDto> buscarPorId(Long id);

    /**
     * Atualiza os dados de um imposto existente (descrição, ano de exercício e alíquotas).
     *
     * @param id identificador do imposto a ser atualizado
     * @param dto novos dados do imposto
     * @return ResponseEntity com status 200 OK e o DTO atualizado, ou 404 Not Found
     */
    ResponseEntity<ImpostoResponseDto> atualizar(Long id, ImpostoRequestDto dto);

    /**
     * Realiza a exclusão lógica do imposto (ativo = false).
     *
     * @param id identificador do imposto
     * @return ResponseEntity com status 204 No Content se inativado, ou 404 Not Found
     */
    ResponseEntity<Void> inativar(Long id);
}
