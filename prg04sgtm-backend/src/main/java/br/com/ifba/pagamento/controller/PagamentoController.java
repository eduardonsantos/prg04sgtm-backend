package br.com.ifba.pagamento.controller;

import br.com.ifba.boleto.entity.BoletosPrefeitura;
import br.com.ifba.pagamento.dto.PagamentoRequestDto;
import br.com.ifba.pagamento.dto.PagamentoResponseDto;
import br.com.ifba.pagamento.entity.Pagamento;
import br.com.ifba.pagamento.service.PagamentoIService;
import br.com.ifba.usuario.entity.Usuario;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller REST para o gerenciamento de Pagamentos.
 * Mapeado para o caminho plural em português sem acentos: /api/v1/pagamentos
 */
@RestController
@RequestMapping(path = "/api/v1/pagamentos", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PagamentoController {

    private final PagamentoIService pagamentoService;

    /**
     * Responsabilidade: Registrar baixa manual de pagamento em um boleto da prefeitura.
     * Status HTTP: Retorna 201 Created com o DTO do pagamento gerado.
     *
     * @apiNote Registra a baixa manual de pagamento de um boleto por um usuário operador.
     * @param dto dados contendo o ID do boleto e o ID do usuário operador
     * @return ResponseEntity com status 201 Created e o DTO do pagamento registrado
     * @author eduardo
     */
    @PostMapping
    public ResponseEntity<PagamentoResponseDto> salvar(@Valid @RequestBody PagamentoRequestDto dto) {
        Pagamento salvo = pagamentoService.salvar(dto.boletoId(), dto.usuarioId());
        return ResponseEntity.status(HttpStatus.CREATED).body(PagamentoResponseDto.deEntidade(salvo));
    }

    /**
     * Responsabilidade: Listar todos os pagamentos ativos registrados no sistema.
     * Status HTTP: Retorna 200 OK com a lista de DTOs.
     *
     * @apiNote Lista todos os pagamentos com status ativo.
     * @return ResponseEntity com status 200 OK e a lista de DTOs dos pagamentos
     * @author eduardo
     */
    @GetMapping
    public ResponseEntity<List<PagamentoResponseDto>> listarAtivos() {
        List<Pagamento> ativos = pagamentoService.listarAtivos();
        List<PagamentoResponseDto> response = ativos.stream()
                .map(PagamentoResponseDto::deEntidade)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * Responsabilidade: Buscar um pagamento específico por ID.
     * Status HTTP: Retorna 200 OK se encontrado e ativo; retorna 404 Not Found se não existir ou inativo.
     *
     * @apiNote Busca um pagamento pelo seu ID.
     * @param id identificador do pagamento
     * @return ResponseEntity com status 200 OK ou 404 Not Found se inexistente ou inativo
     * @author eduardo
     */
    @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponseDto> buscarPorId(@PathVariable Long id) {
        try {
            Pagamento entity = pagamentoService.buscarPorId(id);
            if (entity == null || Boolean.FALSE.equals(entity.getAtivo())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(PagamentoResponseDto.deEntidade(entity));
        } catch (IllegalArgumentException e) {
            if (e.getMessage() == null || !e.getMessage().contains("não encontrado")) {
                throw e;
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Responsabilidade: Atualizar dados de um registro de pagamento.
     * Status HTTP: Retorna 200 OK com o DTO atualizado; retorna 404 Not Found se não existir.
     *
     * @apiNote Atualiza dados de um pagamento existente.
     * @param id identificador do pagamento
     * @param dto dados do pagamento
     * @return ResponseEntity com status 200 OK e o DTO atualizado, ou 404 Not Found
     * @author eduardo
     */
    @PutMapping("/{id}")
    public ResponseEntity<PagamentoResponseDto> atualizar(@PathVariable Long id, @Valid @RequestBody PagamentoRequestDto dto) {
        try {
            Pagamento entity = new Pagamento();
            BoletosPrefeitura boleto = new BoletosPrefeitura();
            boleto.setId(dto.boletoId());
            Usuario usuario = new Usuario();
            usuario.setId(dto.usuarioId());
            entity.setBoletoPrefeitura(boleto);
            entity.setUsuario(usuario);
            Pagamento atualizado = pagamentoService.atualizar(id, entity);
            return ResponseEntity.ok(PagamentoResponseDto.deEntidade(atualizado));
        } catch (IllegalArgumentException e) {
            if (e.getMessage() == null || !e.getMessage().contains("não encontrado")) {
                throw e;
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Responsabilidade: Realizar o estorno do pagamento (inativa o pagamento e reabre o boleto como PENDENTE).
     * Status HTTP: Retorna 204 No Content após o estorno; retorna 404 Not Found se id inexistente.
     *
     * @apiNote Realiza o estorno do pagamento (inativação lógica com ativo = false e retorno do boleto para PENDENTE).
     * @param id identificador do pagamento
     * @return ResponseEntity com status 204 No Content se estornado, ou 404 Not Found
     * @author eduardo
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> estornar(@PathVariable Long id) {
        try {
            pagamentoService.estornar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            if (e.getMessage() == null || !e.getMessage().contains("não encontrado")) {
                throw e;
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
