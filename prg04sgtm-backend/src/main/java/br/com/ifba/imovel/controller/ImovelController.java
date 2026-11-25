package br.com.ifba.imovel.controller;

import br.com.ifba.imovel.dto.ImovelRequestDto;
import br.com.ifba.imovel.dto.ImovelResponseDto;
import br.com.ifba.imovel.entity.Imovel;
import br.com.ifba.imovel.service.ImovelIService;
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
 * Controller REST para o gerenciamento de Imóveis.
 * Mapeado para o caminho plural em português sem acentos: /api/v1/imoveis
 */
@RestController
@RequestMapping(path = "/api/v1/imoveis", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ImovelController {

    private final ImovelIService imovelService;

    /**
     * Responsabilidade: Cadastrar um novo imóvel associado a um contribuinte proprietário.
     * Status HTTP: Retorna 201 Created com o DTO do imóvel criado.
     *
     * @apiNote Cadastra um novo imóvel vinculado a um contribuinte.
     * @param dto dados do imóvel a ser cadastrado
     * @return ResponseEntity com status 201 Created e o DTO do imóvel criado
     * @author eduardo
     */
    @PostMapping
    public ResponseEntity<ImovelResponseDto> salvar(@Valid @RequestBody ImovelRequestDto dto) {
        Imovel entity = dto.paraEntidade();
        Imovel salvo = imovelService.salvar(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(ImovelResponseDto.deEntidade(salvo));
    }

    /**
     * Responsabilidade: Listar todos os imóveis ativos cadastrados na prefeitura.
     * Status HTTP: Retorna 200 OK com a lista de DTOs.
     *
     * @apiNote Lista todos os imóveis que estão com status ativo no sistema.
     * @return ResponseEntity com status 200 OK e a lista de DTOs dos imóveis ativos
     * @author eduardo
     */
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
     *
     * @apiNote Busca um imóvel pelo seu identificador único (ID).
     * @param id identificador do imóvel
     * @return ResponseEntity com status 200 OK ou 404 Not Found se inexistente ou inativo
     * @author eduardo
     */
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
     *
     * @apiNote Atualiza os dados de um imóvel existente.
     * @param id identificador do imóvel a ser atualizado
     * @param dto novos dados do imóvel
     * @return ResponseEntity com status 200 OK e o DTO atualizado, ou 404 Not Found
     * @author eduardo
     */
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
     *
     * @apiNote Realiza a exclusão lógica do imóvel (ativo = false).
     * @param id identificador do imóvel
     * @return ResponseEntity com status 204 No Content se inativado, ou 404 Not Found
     * @author eduardo
     */
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
