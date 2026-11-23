package br.com.ifba.contribuinte.dto;

import br.com.ifba.contribuinte.entity.Contribuinte;
import br.com.ifba.endereco.dto.EnderecoDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO record de requisição para cadastro e atualização de Contribuinte.
 */
public record ContribuinteRequestDto(
    @NotBlank(message = "O nome do contribuinte é obrigatório")
    @Size(min = 3, max = 150, message = "O nome deve ter entre 3 e 150 caracteres")
    String nome,

    @NotBlank(message = "O CPF ou CNPJ é obrigatório")
    String cpfCnpj,

    @NotBlank(message = "O telefone é obrigatório")
    String telefone,

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Informe um endereço de e-mail válido")
    String email,

    @NotNull(message = "Os dados de endereço são obrigatórios")
    @Valid
    EnderecoDto endereco
) {
    public Contribuinte paraEntidade() {
        Contribuinte entity = new Contribuinte();
        entity.setNome(this.nome);
        entity.setCpfCnpj(this.cpfCnpj);
        entity.setTelefone(this.telefone);
        entity.setEmail(this.email);
        entity.setAtivo(true);
        if (this.endereco != null) {
            entity.setEndereco(this.endereco.paraEntidade());
        }
        return entity;
    }
}
