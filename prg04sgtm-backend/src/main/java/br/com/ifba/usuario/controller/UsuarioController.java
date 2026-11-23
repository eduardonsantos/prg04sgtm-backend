package br.com.ifba.usuario.controller;

import br.com.ifba.usuario.dto.LoginRequestDto;
import br.com.ifba.usuario.dto.UsuarioRequestDto;
import br.com.ifba.usuario.dto.UsuarioResponseDto;
import br.com.ifba.usuario.entity.Usuario;
import br.com.ifba.usuario.service.UsuarioIService;
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
 * Controller REST para o gerenciamento de Usuários.
 * Mapeado para o caminho plural em português sem acentos: /api/v1/usuarios
 */
@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController implements UsuarioIController {

    private final UsuarioIService usuarioService;

    /**
     * Injeção de dependência via construtor com imports explícitos.
     */
    public UsuarioController(UsuarioIService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Responsabilidade: Cadastrar um novo usuário com nível de acesso (ADMIN, FISCAL ou ATENDENTE).
     * Status HTTP: Retorna 201 Created com o DTO do usuário gerado (senha omitida no retorno).
     */
    @Override
    @PostMapping
    public ResponseEntity<UsuarioResponseDto> salvar(@Valid @RequestBody UsuarioRequestDto dto) {
        Usuario entity = dto.paraEntidade();
        Usuario salvo = usuarioService.salvar(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponseDto.deEntidade(salvo));
    }

    /**
     * Responsabilidade: Listar todos os usuários com cadastro ativo no sistema.
     * Status HTTP: Retorna 200 OK com a lista de DTOs.
     */
    @Override
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> listarAtivos() {
        List<Usuario> ativos = usuarioService.listarAtivos();
        List<UsuarioResponseDto> response = ativos.stream()
                .map(UsuarioResponseDto::deEntidade)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * Responsabilidade: Buscar um usuário específico por ID.
     * Status HTTP: Retorna 200 OK se encontrado e ativo; retorna 404 Not Found se não existir ou inativo.
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> buscarPorId(@PathVariable Long id) {
        try {
            Usuario entity = usuarioService.buscarPorId(id);
            if (entity == null || Boolean.FALSE.equals(entity.getAtivo())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(UsuarioResponseDto.deEntidade(entity));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Responsabilidade: Atualizar os dados cadastrais (nome, login, nível de acesso e opcionalmente senha).
     * Status HTTP: Retorna 200 OK com o DTO atualizado; retorna 404 Not Found se usuário inexistente.
     */
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDto dto) {
        try {
            Usuario entity = dto.paraEntidade();
            Usuario atualizado = usuarioService.atualizar(id, entity);
            return ResponseEntity.ok(UsuarioResponseDto.deEntidade(atualizado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Responsabilidade: Inativar a conta de um usuário (ativo = false). Protege a conta raiz 'admin'.
     * Status HTTP: Retorna 204 No Content se inativado com sucesso; retorna 404 Not Found se id inexistente.
     */
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        try {
            usuarioService.inativar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Responsabilidade: Validar credenciais de login e retornar dados do usuário autenticado.
     * Status HTTP: Retorna 200 OK com o DTO do usuário se válido; retorna 400 Bad Request se credenciais inválidas.
     */
    @Override
    @PostMapping("/login")
    public ResponseEntity<UsuarioResponseDto> autenticar(@Valid @RequestBody LoginRequestDto dto) {
        try {
            Usuario autenticado = usuarioService.autenticar(dto.login(), dto.senha());
            return ResponseEntity.ok(UsuarioResponseDto.deEntidade(autenticado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
