package br.com.ifba.imposto.controller;

import br.com.ifba.imposto.dto.ImpostoRequestDto;
import br.com.ifba.imposto.dto.ImpostoResponseDto;
import br.com.ifba.imposto.entity.Imposto;
import br.com.ifba.imposto.service.ImpostoIService;
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
 * Controller REST para o gerenciamento de Impostos.
 * Mapeado para o caminho plural em português sem acentos: /api/v1/impostos
 */
@RestController
@RequestMapping(path = "/api/v1/impostos", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ImpostoController {

    private final ImpostoIService impostoService;

    /**
     * Responsabilidade: Cadastrar uma nova definição de imposto e suas alíquotas de cálculo.
     * Status HTTP: Retorna 201 Created com o DTO do imposto criado.
     *
     * @apiNote Cadastra uma nova configuração tributária de imposto (ex: IPTU com alíquotas).
     * @param dto dados do imposto a ser cadastrado
     * @return ResponseEntity com status 201 Created e o DTO do imposto criado
     * @author eduardo
     */
    @PostMapping
    public ResponseEntity<ImpostoResponseDto> salvar(@Valid @RequestBody ImpostoRequestDto dto) {
        Imposto entity = dto.paraEntidade();
        Imposto salvo = impostoService.salvar(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(ImpostoResponseDto.deEntidade(salvo));
    }

    /**
     * Responsabilidade: Listar todos os impostos ativos configurados para tributação.
     * Status HTTP: Retorna 200 OK com a lista de DTOs.
     *
     * @apiNote Lista todos os impostos que estão com status ativo no sistema.
     * @return ResponseEntity com status 200 OK e a lista de DTOs dos impostos ativos
     * @author eduardo
     */
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
     *
     * @apiNote Busca um imposto pelo seu identificador único (ID).
     * @param id identificador do imposto
     * @return ResponseEntity com status 200 OK ou 404 Not Found se inexistente ou inativo
     * @author eduardo
     */
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
     *
     * @apiNote Atualiza os dados de um imposto existente (descrição, ano de exercício e alíquotas).
     * @param id identificador do imposto a ser atualizado
     * @param dto novos dados do imposto
     * @return ResponseEntity com status 200 OK e o DTO atualizado, ou 404 Not Found
     * @author eduardo
     */
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
     *
     * @apiNote Realiza a exclusão lógica do imposto (ativo = false).
     * @param id identificador do imposto
     * @return ResponseEntity com status 204 No Content se inativado, ou 404 Not Found
     * @author eduardo
     */
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
