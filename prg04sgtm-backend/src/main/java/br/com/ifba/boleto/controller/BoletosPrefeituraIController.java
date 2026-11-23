package br.com.ifba.boleto.controller;

import br.com.ifba.boleto.dto.BoletoStatusRequestDto;
import br.com.ifba.boleto.dto.BoletosPrefeituraRequestDto;
import br.com.ifba.boleto.dto.BoletosPrefeituraResponseDto;
import java.util.List;
import org.springframework.http.ResponseEntity;

/**
 * Interface do controller para o domínio de Boletos da Prefeitura.
 * Define o contrato dos endpoints REST para consulta, emissão e atualização de boletos.
 */
public interface BoletosPrefeituraIController {

    /**
     * Emite/cadastra um novo boleto da prefeitura.
     *
     * @param dto dados do boleto
     * @return ResponseEntity com status 201 Created e o DTO do boleto criado
     */
    ResponseEntity<BoletosPrefeituraResponseDto> salvar(BoletosPrefeituraRequestDto dto);

    /**
     * Lista todos os boletos com status ativo no sistema.
     *
     * @return ResponseEntity com status 200 OK e a lista de DTOs dos boletos
     */
    ResponseEntity<List<BoletosPrefeituraResponseDto>> listarAtivos();

    /**
     * Busca um boleto pelo seu ID único.
     *
     * @param id identificador do boleto
     * @return ResponseEntity com status 200 OK ou 404 Not Found se não encontrado ou inativo
     */
    ResponseEntity<BoletosPrefeituraResponseDto> buscarPorId(Long id);

    /**
     * Atualiza os dados de um boleto existente.
     *
     * @param id identificador do boleto
     * @param dto novos dados do boleto
     * @return ResponseEntity com status 200 OK e o DTO atualizado, ou 404 Not Found
     */
    ResponseEntity<BoletosPrefeituraResponseDto> atualizar(Long id, BoletosPrefeituraRequestDto dto);

    /**
     * Realiza a exclusão lógica do boleto (ativo = false).
     *
     * @param id identificador do boleto
     * @return ResponseEntity com status 204 No Content se inativado, ou 404 Not Found
     */
    ResponseEntity<Void> inativar(Long id);

    /**
     * Atualiza exclusivamente o status de um boleto (ex: PENDENTE, PAGO, VENCIDO).
     *
     * @param id identificador do boleto
     * @param dto novo status desejado
     * @return ResponseEntity com status 200 OK e o DTO atualizado, ou 404 Not Found
     */
    ResponseEntity<BoletosPrefeituraResponseDto> atualizarStatus(Long id, BoletoStatusRequestDto dto);
}
