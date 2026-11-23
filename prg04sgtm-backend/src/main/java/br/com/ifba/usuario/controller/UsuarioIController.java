package br.com.ifba.usuario.controller;

import br.com.ifba.usuario.dto.LoginRequestDto;
import br.com.ifba.usuario.dto.UsuarioRequestDto;
import br.com.ifba.usuario.dto.UsuarioResponseDto;
import java.util.List;
import org.springframework.http.ResponseEntity;

/**
 * Interface do controller para o domínio de Usuário.
 * Define o contrato dos endpoints REST para gerenciamento de usuários do sistema tributário.
 */
public interface UsuarioIController {

    /**
     * Cadastra um novo usuário no sistema.
     *
     * @param dto dados do usuário a ser cadastrado
     * @return ResponseEntity com status 201 Created e o DTO do usuário criado
     */
    ResponseEntity<UsuarioResponseDto> salvar(UsuarioRequestDto dto);

    /**
     * Lista todos os usuários que estão com status ativo.
     *
     * @return ResponseEntity com status 200 OK e a lista de DTOs dos usuários
     */
    ResponseEntity<List<UsuarioResponseDto>> listarAtivos();

    /**
     * Busca um usuário pelo seu identificador único (ID).
     *
     * @param id identificador do usuário
     * @return ResponseEntity com status 200 OK ou 404 Not Found se inexistente ou inativo
     */
    ResponseEntity<UsuarioResponseDto> buscarPorId(Long id);

    /**
     * Atualiza os dados cadastrais de um usuário existente.
     *
     * @param id identificador do usuário a ser atualizado
     * @param dto novos dados do usuário
     * @return ResponseEntity com status 200 OK e o DTO atualizado, ou 404 Not Found
     */
    ResponseEntity<UsuarioResponseDto> atualizar(Long id, UsuarioRequestDto dto);

    /**
     * Inativa logicamente a conta de um usuário (ativo = false).
     *
     * @param id identificador do usuário
     * @return ResponseEntity com status 204 No Content se inativado, ou 404 Not Found
     */
    ResponseEntity<Void> inativar(Long id);

    /**
     * Autentica um usuário via login e senha.
     *
     * @param dto credenciais de login
     * @return ResponseEntity com status 200 OK e o DTO do usuário autenticado
     */
    ResponseEntity<UsuarioResponseDto> autenticar(LoginRequestDto dto);
}
