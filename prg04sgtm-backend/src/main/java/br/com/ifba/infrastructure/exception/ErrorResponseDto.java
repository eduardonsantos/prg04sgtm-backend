package br.com.ifba.infrastructure.exception;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO record para padronização de respostas de erro na API REST.
 */
public record ErrorResponseDto(
    LocalDateTime timestamp,
    int status,
    String erro,
    String mensagem,
    List<String> detalhes
) {
    public ErrorResponseDto(int status, String erro, String mensagem) {
        this(LocalDateTime.now(), status, erro, mensagem, List.of());
    }

    public ErrorResponseDto(int status, String erro, String mensagem, List<String> detalhes) {
        this(LocalDateTime.now(), status, erro, mensagem, detalhes);
    }
}
