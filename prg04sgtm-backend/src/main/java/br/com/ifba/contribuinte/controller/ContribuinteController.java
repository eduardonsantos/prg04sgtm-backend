package br.com.ifba.contribuinte.controller;

import br.com.ifba.contribuinte.dto.ContribuinteRequestDto;
import br.com.ifba.contribuinte.dto.ContribuinteResponseDto;
import br.com.ifba.contribuinte.entity.Contribuinte;
import br.com.ifba.contribuinte.service.ContribuinteIService;
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
 * Controller REST para o gerenciamento de Contribuintes.
 * Mapeado para o caminho plural em português sem acentos: /api/v1/contribuintes
 */
@RestController
@RequestMapping("/api/v1/contribuintes")
public class ContribuinteController {

    private final ContribuinteIService contribuinteService;

    /**
     * Injeção de dependência via construtor com imports explícitos.
     */
    public ContribuinteController(ContribuinteIService contribuinteService) {
        this.contribuinteService = contribuinteService;
    }

    /**
     * Responsabilidade: Cadastrar um novo contribuinte no sistema com endereço cascateado.
     * Status HTTP: Retorna 201 Created com o DTO do contribuinte gerado.
     *
     * @apiNote Cadastra um novo contribuinte.
     * @param dto dados do contribuinte a ser cadastrado
     * @return ResponseEntity com status 201 Created e o DTO do contribuinte criado
     * @author eduardo
     */
    @PostMapping
    public ResponseEntity<ContribuinteResponseDto> salvar(@Valid @RequestBody ContribuinteRequestDto dto) {
        Contribuinte entity = dto.paraEntidade();
        Contribuinte salvo = contribuinteService.salvar(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(ContribuinteResponseDto.deEntidade(salvo));
    }

    /**
     * Responsabilidade: Listar todos os contribuintes com cadastro ativo no sistema.
     * Status HTTP: Retorna 200 OK com a lista de DTOs.
     *
     * @apiNote Lista todos os contribuintes que estão com status ativo no sistema.
     * @return ResponseEntity com status 200 OK e a lista de DTOs dos contribuintes ativos
     * @author eduardo
     */
    @GetMapping
    public ResponseEntity<List<ContribuinteResponseDto>> listarAtivos() {
        List<Contribuinte> ativos = contribuinteService.listarAtivos();
        List<ContribuinteResponseDto> response = ativos.stream()
                .map(ContribuinteResponseDto::deEntidade)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * Responsabilidade: Buscar um contribuinte específico pelo ID.
     * Status HTTP: Retorna 200 OK se encontrado e ativo; retorna 404 Not Found se inexistente ou inativo.
     *
     * @apiNote Busca um contribuinte pelo seu identificador único (ID).
     * @param id identificador do contribuinte
     * @return ResponseEntity com status 200 OK ou 404 Not Found se inexistente ou inativo
     * @author eduardo
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContribuinteResponseDto> buscarPorId(@PathVariable Long id) {
        try {
            Contribuinte entity = contribuinteService.buscarPorId(id);
            if (entity == null || Boolean.FALSE.equals(entity.getAtivo())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(ContribuinteResponseDto.deEntidade(entity));
        } catch (IllegalArgumentException e) {
            if (e.getMessage() == null || !e.getMessage().contains("não encontrado")) {
                throw e;
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Responsabilidade: Atualizar integralmente os dados de um contribuinte e seu respectivo endereço.
     * Status HTTP: Retorna 200 OK com o DTO atualizado se sucesso; retorna 404 Not Found se não existir.
     *
     * @apiNote Atualiza os dados de um contribuinte existente.
     * @param id identificador do contribuinte a ser atualizado
     * @param dto novos dados do contribuinte
     * @return ResponseEntity com status 200 OK e o DTO atualizado, ou 404 Not Found
     * @author eduardo
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContribuinteResponseDto> atualizar(@PathVariable Long id, @Valid @RequestBody ContribuinteRequestDto dto) {
        try {
            Contribuinte entity = dto.paraEntidade();
            Contribuinte atualizado = contribuinteService.atualizar(id, entity);
            return ResponseEntity.ok(ContribuinteResponseDto.deEntidade(atualizado));
        } catch (IllegalArgumentException e) {
            if (e.getMessage() == null || !e.getMessage().contains("não encontrado")) {
                throw e;
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Responsabilidade: Realizar exclusão lógica (soft-delete) marcando o contribuinte como inativo (ativo = false).
     * Status HTTP: Retorna 204 No Content se executado com sucesso; retorna 404 Not Found se id inexistente.
     *
     * @apiNote Realiza a exclusão lógica do contribuinte (ativo = false).
     * @param id identificador do contribuinte
     * @return ResponseEntity com status 204 No Content se inativado, ou 404 Not Found
     * @author eduardo
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        try {
            contribuinteService.inativar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            if (e.getMessage() == null || !e.getMessage().contains("não encontrado")) {
                throw e;
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
