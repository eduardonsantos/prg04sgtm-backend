package br.com.ifba.lancamento.controller;

import br.com.ifba.lancamento.dto.LancamentoImpostoRequestDto;
import br.com.ifba.lancamento.dto.LancamentoImpostoResponseDto;
import br.com.ifba.lancamento.entity.LancamentoImposto;
import br.com.ifba.lancamento.service.LancamentoImpostoIService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
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
 * Controller REST para o gerenciamento de Lançamentos de Impostos.
 * Mapeado para o caminho plural em português sem acentos: /api/v1/lancamentos
 */
@RestController
@RequestMapping("/api/v1/lancamentos")
public class LancamentoImpostoController implements LancamentoImpostoIController {

    private final LancamentoImpostoIService lancamentoService;

    /**
     * Injeção de dependência via construtor com imports explícitos.
     */
    public LancamentoImpostoController(LancamentoImpostoIService lancamentoService) {
        this.lancamentoService = lancamentoService;
    }

    /**
     * Responsabilidade: Realizar o lançamento tributário de um imóvel, calculando o valor venal e gerando boletos.
     * Status HTTP: Retorna 201 Created com o DTO do lançamento persistido.
     */
    @Override
    @PostMapping
    public ResponseEntity<LancamentoImpostoResponseDto> salvar(@Valid @RequestBody LancamentoImpostoRequestDto dto) {
        LancamentoImposto entity = dto.paraEntidade();
        LancamentoImposto salvo = lancamentoService.salvar(entity, dto.quantidadeParcelas());
        return ResponseEntity.status(HttpStatus.CREATED).body(LancamentoImpostoResponseDto.deEntidade(salvo));
    }

    /**
     * Responsabilidade: Listar todos os lançamentos de impostos ativos.
     * Status HTTP: Retorna 200 OK com a lista de DTOs.
     */
    @Override
    @GetMapping
    public ResponseEntity<List<LancamentoImpostoResponseDto>> listarAtivos() {
        List<LancamentoImposto> ativos = lancamentoService.listarAtivos();
        List<LancamentoImpostoResponseDto> response = ativos.stream()
                .map(LancamentoImpostoResponseDto::deEntidade)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * Responsabilidade: Buscar um lançamento de imposto específico pelo ID.
     * Status HTTP: Retorna 200 OK se encontrado e ativo; retorna 404 Not Found se inexistente ou inativo.
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<LancamentoImpostoResponseDto> buscarPorId(@PathVariable Long id) {
        try {
            LancamentoImposto entity = lancamentoService.buscarPorId(id);
            if (entity == null || Boolean.FALSE.equals(entity.getAtivo())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(LancamentoImpostoResponseDto.deEntidade(entity));
        } catch (IllegalArgumentException e) {
            if (e.getMessage() == null || !e.getMessage().contains("não encontrado")) {
                throw e;
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Responsabilidade: Atualizar as informações de cálculo ou tributo de um lançamento existente.
     * Status HTTP: Retorna 200 OK com o DTO atualizado; retorna 404 Not Found se não encontrado.
     */
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<LancamentoImpostoResponseDto> atualizar(@PathVariable Long id, @Valid @RequestBody LancamentoImpostoRequestDto dto) {
        try {
            LancamentoImposto entity = dto.paraEntidade();
            LancamentoImposto atualizado = lancamentoService.atualizar(id, entity);
            return ResponseEntity.ok(LancamentoImpostoResponseDto.deEntidade(atualizado));
        } catch (IllegalArgumentException e) {
            if (e.getMessage() == null || !e.getMessage().contains("não encontrado")) {
                throw e;
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Responsabilidade: Inativar logicamente um lançamento de imposto (ativo = false).
     * Status HTTP: Retorna 204 No Content se inativado; retorna 404 Not Found se id inexistente.
     */
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        try {
            lancamentoService.inativar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            if (e.getMessage() == null || !e.getMessage().contains("não encontrado")) {
                throw e;
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
