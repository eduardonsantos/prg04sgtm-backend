package br.com.ifba.pagamento.controller;

import br.com.ifba.pagamento.dto.PagamentoRequestDto;
import br.com.ifba.pagamento.dto.PagamentoResponseDto;
import java.util.List;
import org.springframework.http.ResponseEntity;

/**
 * Interface do controller para o domínio de Pagamento.
 * Define o contrato dos endpoints REST para liquidação e estorno de pagamentos.
 */
public interface PagamentoIController {

    /**
     * Registra a baixa manual de pagamento de um boleto por um usuário operador.
     *
     * @param dto dados contendo o ID do boleto e o ID do usuário operador
     * @return ResponseEntity com status 201 Created e o DTO do pagamento registrado
     */
    ResponseEntity<PagamentoResponseDto> salvar(PagamentoRequestDto dto);

    /**
     * Lista todos os pagamentos com status ativo.
     *
     * @return ResponseEntity com status 200 OK e a lista de DTOs dos pagamentos
     */
    ResponseEntity<List<PagamentoResponseDto>> listarAtivos();

    /**
     * Busca um pagamento pelo seu ID.
     *
     * @param id identificador do pagamento
     * @return ResponseEntity com status 200 OK ou 404 Not Found se inexistente ou inativo
     */
    ResponseEntity<PagamentoResponseDto> buscarPorId(Long id);

    /**
     * Atualiza dados de um pagamento existente.
     *
     * @param id identificador do pagamento
     * @param dto dados do pagamento
     * @return ResponseEntity com status 200 OK e o DTO atualizado, ou 404 Not Found
     */
    ResponseEntity<PagamentoResponseDto> atualizar(Long id, PagamentoRequestDto dto);

    /**
     * Realiza o estorno do pagamento (inativação lógica com ativo = false e retorno do boleto para PENDENTE).
     *
     * @param id identificador do pagamento
     * @return ResponseEntity com status 204 No Content se estornado, ou 404 Not Found
     */
    ResponseEntity<Void> estornar(Long id);
}
