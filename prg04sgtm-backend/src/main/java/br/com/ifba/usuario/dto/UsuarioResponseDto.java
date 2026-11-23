package br.com.ifba.usuario.dto;

import br.com.ifba.usuario.entity.Usuario;

/**
 * DTO record de resposta para Usuário (sem expor a senha por segurança).
 */
public record UsuarioResponseDto(
    Long id,
    String nome,
    String login,
    String nivelAcesso,
    Boolean ativo
) {
    public static UsuarioResponseDto deEntidade(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return new UsuarioResponseDto(
            usuario.getId(),
            usuario.getNome(),
            usuario.getLogin(),
            usuario.getNivelAcesso(),
            usuario.getAtivo()
        );
    }
}
