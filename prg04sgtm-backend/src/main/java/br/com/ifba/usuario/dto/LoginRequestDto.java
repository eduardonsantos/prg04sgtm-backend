package br.com.ifba.usuario.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO record de requisição para autenticação de Usuário.
 */
public record LoginRequestDto(
    @NotBlank(message = "O login é obrigatório")
    String login,

    @NotBlank(message = "A senha é obrigatória")
    String senha
) {}
