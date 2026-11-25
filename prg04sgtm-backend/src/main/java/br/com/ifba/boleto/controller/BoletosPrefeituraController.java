package br.com.ifba.boleto.controller;

import br.com.ifba.boleto.dto.BoletoStatusRequestDto;
import br.com.ifba.boleto.dto.BoletosPrefeituraRequestDto;
import br.com.ifba.boleto.dto.BoletosPrefeituraResponseDto;
import br.com.ifba.boleto.entity.BoletosPrefeitura;
import br.com.ifba.boleto.service.BoletosPrefeituraIService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller REST para o gerenciamento de Boletos da Prefeitura.
 * Mapeado para o caminho plural em português sem acentos: /api/v1/boletos
 */
@RestController
@RequestMapping("/api/v1/boletos")
public class BoletosPrefeituraController {

    private final BoletosPrefeituraIService boletoService;

    /**
     * Injeção de dependência via construtor com imports explícitos.
     */
    public BoletosPrefeituraController(BoletosPrefeituraIService boletoService) {
        this.boletoService = boletoService;
    }

    /**
     * Responsabilidade: Emitir ou cadastrar manualmente um boleto da prefeitura.
     * Status HTTP: Retorna 201 Created com o DTO do boleto criado.
     *
     * @apiNote Emite/cadastra um novo boleto da prefeitura.
     * @param dto dados do boleto
     * @return ResponseEntity com status 201 Created e o DTO do boleto criado
     * @author eduardo
     */
    @PostMapping
    public ResponseEntity<BoletosPrefeituraResponseDto> salvar(@Valid @RequestBody BoletosPrefeituraRequestDto dto) {
        BoletosPrefeitura entity = dto.paraEntidade();
        BoletosPrefeitura salvo = boletoService.salvar(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(BoletosPrefeituraResponseDto.deEntidade(salvo));
    }

    /**
     * Responsabilidade: Listar todos os boletos ativos gerados pela prefeitura.
     * Status HTTP: Retorna 200 OK com a lista de DTOs.
     *
     * @apiNote Lista todos os boletos com status ativo no sistema.
     * @return ResponseEntity com status 200 OK e a lista de DTOs dos boletos
     * @author eduardo
     */
    @GetMapping
    public ResponseEntity<List<BoletosPrefeituraResponseDto>> listarAtivos() {
        List<BoletosPrefeitura> ativos = boletoService.listarAtivos();
        List<BoletosPrefeituraResponseDto> response = ativos.stream()
                .map(BoletosPrefeituraResponseDto::deEntidade)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * Responsabilidade: Buscar um boleto específico pelo ID.
     * Status HTTP: Retorna 200 OK se encontrado e ativo; retorna 404 Not Found se não encontrado ou inativo.
     *
     * @apiNote Busca um boleto pelo seu ID único.
     * @param id identificador do boleto
     * @return ResponseEntity com status 200 OK ou 404 Not Found se não encontrado ou inativo
     * @author eduardo
     */
    @GetMapping("/{id}")
    public ResponseEntity<BoletosPrefeituraResponseDto> buscarPorId(@PathVariable Long id) {
        try {
            BoletosPrefeitura entity = boletoService.buscarPorId(id);
            if (entity == null || Boolean.FALSE.equals(entity.getAtivo())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(BoletosPrefeituraResponseDto.deEntidade(entity));
        } catch (IllegalArgumentException e) {
            if (e.getMessage() == null || !e.getMessage().contains("não encontrado")) {
                throw e;
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Responsabilidade: Atualizar integralmente os dados de vencimento e valor de um boleto.
     * Status HTTP: Retorna 200 OK com o DTO atualizado; retorna 404 Not Found se não encontrado.
     *
     * @apiNote Atualiza os dados de um boleto existente.
     * @param id identificador do boleto
     * @param dto novos dados do boleto
     * @return ResponseEntity com status 200 OK e o DTO atualizado, ou 404 Not Found
     * @author eduardo
     */
    @PutMapping("/{id}")
    public ResponseEntity<BoletosPrefeituraResponseDto> atualizar(@PathVariable Long id, @Valid @RequestBody BoletosPrefeituraRequestDto dto) {
        try {
            BoletosPrefeitura entity = dto.paraEntidade();
            BoletosPrefeitura atualizado = boletoService.atualizar(id, entity);
            return ResponseEntity.ok(BoletosPrefeituraResponseDto.deEntidade(atualizado));
        } catch (IllegalArgumentException e) {
            if (e.getMessage() == null || !e.getMessage().contains("não encontrado")) {
                throw e;
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Responsabilidade: Inativar logicamente um boleto da prefeitura (ativo = false).
     * Status HTTP: Retorna 204 No Content se inativado; retorna 404 Not Found se id inexistente.
     *
     * @apiNote Realiza a exclusão lógica do boleto (ativo = false).
     * @param id identificador do boleto
     * @return ResponseEntity com status 204 No Content se inativado, ou 404 Not Found
     * @author eduardo
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        try {
            boletoService.inativar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            if (e.getMessage() == null || !e.getMessage().contains("não encontrado")) {
                throw e;
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Responsabilidade: Atualizar o status do boleto (PENDENTE, PAGO, VENCIDO).
     * Status HTTP: Retorna 200 OK com o DTO atualizado; retorna 404 Not Found se inexistente.
     *
     * @apiNote Atualiza exclusivamente o status de um boleto (ex: PENDENTE, PAGO, VENCIDO).
     * @param id identificador do boleto
     * @param dto novo status desejado
     * @return ResponseEntity com status 200 OK e o DTO atualizado, ou 404 Not Found
     * @author eduardo
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<BoletosPrefeituraResponseDto> atualizarStatus(@PathVariable Long id, @Valid @RequestBody BoletoStatusRequestDto dto) {
        try {
            BoletosPrefeitura atualizado = boletoService.atualizarStatus(id, dto.status());
            return ResponseEntity.ok(BoletosPrefeituraResponseDto.deEntidade(atualizado));
        } catch (IllegalArgumentException e) {
            if (e.getMessage() == null || !e.getMessage().contains("não encontrado")) {
                throw e;
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
