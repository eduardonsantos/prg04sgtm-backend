package br.com.ifba.endereco.dto;

import br.com.ifba.endereco.entity.Endereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO record para transferência de dados de Endereço.
 */
public record EnderecoDto(
    @NotBlank(message = "O logradouro é obrigatório")
    String logradouro,

    @NotBlank(message = "O número é obrigatório")
    String numero,

    String complemento,

    @NotBlank(message = "O bairro é obrigatório")
    String bairro,

    @NotBlank(message = "A cidade é obrigatória")
    String cidade,

    @NotBlank(message = "O estado deve conter 2 caracteres (UF)")
    @Size(min = 2, max = 2, message = "O estado deve ter exatamente 2 caracteres")
    String estado,

    @NotBlank(message = "O CEP é obrigatório")
    @Pattern(regexp = "\\d{8}", message = "O CEP deve conter exatamente 8 dígitos numéricos")
    String cep
) {
    public Endereco paraEntidade() {
        Endereco endereco = new Endereco();
        endereco.setLogradouro(this.logradouro);
        endereco.setNumero(this.numero);
        endereco.setComplemento(this.complemento);
        endereco.setBairro(this.bairro);
        endereco.setCidade(this.cidade);
        endereco.setEstado(this.estado);
        endereco.setCep(this.cep);
        return endereco;
    }

    public static EnderecoDto deEntidade(Endereco endereco) {
        if (endereco == null) {
            return null;
        }
        return new EnderecoDto(
            endereco.getLogradouro(),
            endereco.getNumero(),
            endereco.getComplemento(),
            endereco.getBairro(),
            endereco.getCidade(),
            endereco.getEstado(),
            endereco.getCep()
        );
    }
}
