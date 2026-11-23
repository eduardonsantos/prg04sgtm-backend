package br.com.ifba.contribuinte.dto;

import br.com.ifba.contribuinte.entity.Contribuinte;
import br.com.ifba.endereco.dto.EnderecoDto;

/**
 * DTO record de resposta para Contribuinte.
 */
public record ContribuinteResponseDto(
    Long id,
    String nome,
    String cpfCnpj,
    String telefone,
    String email,
    Boolean ativo,
    EnderecoDto endereco
) {
    public static ContribuinteResponseDto deEntidade(Contribuinte entity) {
        if (entity == null) {
            return null;
        }
        return new ContribuinteResponseDto(
            entity.getId(),
            entity.getNome(),
            entity.getCpfCnpj(),
            entity.getTelefone(),
            entity.getEmail(),
            entity.getAtivo(),
            EnderecoDto.deEntidade(entity.getEndereco())
        );
    }
}
