package br.com.ifba.imovel.controller;

import br.com.ifba.imovel.dto.ImovelRequestDto;
import br.com.ifba.imovel.dto.ImovelResponseDto;
import br.com.ifba.imovel.entity.Imovel;
import br.com.ifba.imovel.service.ImovelIService;
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
 * Controller REST para o gerenciamento de Imóveis.
 * Mapeado para o caminho plural em português sem acentos: /api/v1/imoveis
 */
@RestController
@RequestMapping("/api/v1/imoveis")
public class ImovelController implements ImovelIController {

    private final ImovelIService imovelService;

    /**
     * Injeção de dependência via construtor com imports explícitos.
     */
    public ImovelController(ImovelIService imovelService) {
        this.imovelService = imovelService;
    }

    /**
     * Responsabilidade: Cadastrar um novo imóvel associado a um contribuinte proprietário.
     * Status HTTP: Retorna 201 Created com o DTO do imóvel criado.
     */
    @Override
    @PostMapping
    public ResponseEntity<ImovelResponseDto> salvar(@Valid @RequestBody ImovelRequestDto dto) {
        Imovel entity = dto.paraEntidade();
        Imovel salvo = imovelService.salvar(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(ImovelResponseDto.deEntidade(salvo));
    }

    /**
     * Responsabilidade: Listar todos os imóveis ativos cadastrados na prefeitura.
     * Status HTTP: Retorna 200 OK com a lista de DTOs.
     */
    @Override
    @GetMapping
    public ResponseEntity<List<ImovelResponseDto>> listarAtivos() {
        List<Imovel> ativos = imovelService.listarAtivos();
        List<ImovelResponseDto> response = ativos.stream()
                .map(ImovelResponseDto::deEntidade)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * Responsabilidade: Buscar um imóvel específico pelo ID.
     * Status HTTP: Retorna 200 OK se encontrado e ativo; retorna 404 Not Found se inexistente ou inativo.
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ImovelResponseDto> buscarPorId(@PathVariable Long id) {
        try {
            Imovel entity = imovelService.buscarPorId(id);
            if (entity == null || Boolean.FALSE.equals(entity.getAtivo())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(ImovelResponseDto.deEntidade(entity));
        } catch (IllegalArgumentException e) {
            if (e.getMessage() == null || !e.getMessage().contains("não encontrado")) {
                throw e;
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Responsabilidade: Atualizar os dados de metragem, valor venal e endereço de um imóvel.
     * Status HTTP: Retorna 200 OK com o DTO atualizado; retorna 404 Not Found se não encontrado.
     */
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<ImovelResponseDto> atualizar(@PathVariable Long id, @Valid @RequestBody ImovelRequestDto dto) {
        try {
            Imovel entity = dto.paraEntidade();
            Imovel atualizado = imovelService.atualizar(id, entity);
            return ResponseEntity.ok(ImovelResponseDto.deEntidade(atualizado));
        } catch (IllegalArgumentException e) {
            if (e.getMessage() == null || !e.getMessage().contains("não encontrado")) {
                throw e;
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Responsabilidade: Realizar exclusão lógica do imóvel marcando-o como inativo (ativo = false).
     * Status HTTP: Retorna 204 No Content se inativado; retorna 404 Not Found se id inexistente.
     */
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        try {
            imovelService.inativar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            if (e.getMessage() == null || !e.getMessage().contains("não encontrado")) {
                throw e;
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
