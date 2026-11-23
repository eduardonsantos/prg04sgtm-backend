package br.com.ifba.lancamento.controller;

import br.com.ifba.lancamento.dto.LancamentoImpostoRequestDto;
import br.com.ifba.lancamento.dto.LancamentoImpostoResponseDto;
import java.util.List;
import org.springframework.http.ResponseEntity;

/**
 * Interface do controller para o domínio de Lançamento de Imposto.
 * Define o contrato dos endpoints REST para lançar impostos e gerar boletos de cobrança.
 */
public interface LancamentoImpostoIController {

    /**
     * Realiza um novo lançamento de imposto para um imóvel, gerando seus boletos parcelados.
     *
     * @param dto dados do lançamento com ID do imóvel, ID do imposto e quantidade de parcelas
     * @return ResponseEntity com status 201 Created e o DTO do lançamento criado
     */
    ResponseEntity<LancamentoImpostoResponseDto> salvar(LancamentoImpostoRequestDto dto);

    /**
     * Lista todos os lançamentos de imposto ativos.
     *
     * @return ResponseEntity com status 200 OK e a lista de DTOs dos lançamentos
     */
    ResponseEntity<List<LancamentoImpostoResponseDto>> listarAtivos();

    /**
     * Busca um lançamento de imposto pelo seu ID.
     *
     * @param id identificador do lançamento
     * @return ResponseEntity com status 200 OK ou 404 Not Found se inexistente ou inativo
     */
    ResponseEntity<LancamentoImpostoResponseDto> buscarPorId(Long id);

    /**
     * Atualiza um lançamento de imposto existente.
     *
     * @param id identificador do lançamento
     * @param dto dados para atualização
     * @return ResponseEntity com status 200 OK e o DTO atualizado, ou 404 Not Found
     */
    ResponseEntity<LancamentoImpostoResponseDto> atualizar(Long id, LancamentoImpostoRequestDto dto);

    /**
     * Inativa logicamente o lançamento de imposto (ativo = false).
     *
     * @param id identificador do lançamento
     * @return ResponseEntity com status 204 No Content se inativado, ou 404 Not Found
     */
    ResponseEntity<Void> inativar(Long id);
}
