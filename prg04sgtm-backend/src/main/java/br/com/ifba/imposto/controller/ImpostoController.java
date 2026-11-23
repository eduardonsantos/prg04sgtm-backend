package br.com.ifba.imposto.controller;

import br.com.ifba.imposto.dto.ImpostoRequestDto;
import br.com.ifba.imposto.dto.ImpostoResponseDto;
import br.com.ifba.imposto.entity.Imposto;
import br.com.ifba.imposto.service.ImpostoIService;
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
 * Controller REST para o gerenciamento de Impostos.
 * Mapeado para o caminho plural em português sem acentos: /api/v1/impostos
 */
@RestController
@RequestMapping("/api/v1/impostos")
public class ImpostoController implements ImpostoIController {

    private final ImpostoIService impostoService;

    /**
     * Injeção de dependência via construtor com imports explícitos.
     */
    public ImpostoController(ImpostoIService impostoService) {
        this.impostoService = impostoService;
    }

    /**
     * Responsabilidade: Cadastrar uma nova definição de imposto e suas alíquotas de cálculo.
     * Status HTTP: Retorna 201 Created com o DTO do imposto criado.
     */
    @Override
    @PostMapping
    public ResponseEntity<ImpostoResponseDto> salvar(@Valid @RequestBody ImpostoRequestDto dto) {
        Imposto entity = dto.paraEntidade();
        Imposto salvo = impostoService.salvar(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(ImpostoResponseDto.deEntidade(salvo));
    }

    /**
     * Responsabilidade: Listar todos os impostos ativos configurados para tributação.
     * Status HTTP: Retorna 200 OK com a lista de DTOs.
     */
    @Override
    @GetMapping
    public ResponseEntity<List<ImpostoResponseDto>> listarAtivos() {
        List<Imposto> ativos = impostoService.listarAtivos();
        List<ImpostoResponseDto> response = ativos.stream()
                .map(ImpostoResponseDto::deEntidade)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * Responsabilidade: Buscar um imposto específico pelo ID.
     * Status HTTP: Retorna 200 OK se encontrado e ativo; retorna 404 Not Found se inexistente ou inativo.
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ImpostoResponseDto> buscarPorId(@PathVariable Long id) {
        try {
            Imposto entity = impostoService.buscarPorId(id);
            if (entity == null || Boolean.FALSE.equals(entity.getAtivo())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(ImpostoResponseDto.deEntidade(entity));
        } catch (IllegalArgumentException e) {
            if (e.getMessage() == null || !e.getMessage().contains("não encontrado")) {
                throw e;
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Responsabilidade: Atualizar descrição, ano de exercício e alíquotas do imposto.
     * Status HTTP: Retorna 200 OK com o DTO atualizado; retorna 404 Not Found se inexistente.
     */
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<ImpostoResponseDto> atualizar(@PathVariable Long id, @Valid @RequestBody ImpostoRequestDto dto) {
        try {
            Imposto entity = dto.paraEntidade();
            Imposto atualizado = impostoService.atualizar(id, entity);
            return ResponseEntity.ok(ImpostoResponseDto.deEntidade(atualizado));
        } catch (IllegalArgumentException e) {
            if (e.getMessage() == null || !e.getMessage().contains("não encontrado")) {
                throw e;
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Responsabilidade: Inativar logicamente um imposto (ativo = false).
     * Status HTTP: Retorna 204 No Content se inativado; retorna 404 Not Found se id inexistente.
     */
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        try {
            impostoService.inativar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            if (e.getMessage() == null || !e.getMessage().contains("não encontrado")) {
                throw e;
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
