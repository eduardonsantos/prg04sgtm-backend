package br.com.ifba.usuario.dto;

import br.com.ifba.usuario.entity.Usuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO record de requisição para cadastro e atualização de Usuário.
 */
public record UsuarioRequestDto(
    @NotBlank(message = "O nome do usuário é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    String nome,

    @NotBlank(message = "O login de acesso é obrigatório")
    @Size(min = 4, max = 30, message = "O login deve ter entre 4 e 30 caracteres")
    String login,

    @NotBlank(message = "A senha de acesso é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    String senha,

    @NotBlank(message = "O nível de acesso é obrigatório")
    @Pattern(regexp = "ADMIN|FISCAL|ATENDENTE", message = "O nível de acesso deve ser: ADMIN, FISCAL ou ATENDENTE")
    String nivelAcesso
) {
    public Usuario paraEntidade() {
        Usuario usuario = new Usuario();
        usuario.setNome(this.nome);
        usuario.setLogin(this.login);
        usuario.setSenha(this.senha);
        usuario.setNivelAcesso(this.nivelAcesso);
        usuario.setAtivo(true);
        return usuario;
    }
}
