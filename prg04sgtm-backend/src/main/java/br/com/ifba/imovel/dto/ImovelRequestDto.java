package br.com.ifba.imovel.dto;

import br.com.ifba.contribuinte.entity.Contribuinte;
import br.com.ifba.endereco.dto.EnderecoDto;
import br.com.ifba.imovel.entity.Imovel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO record de requisição para cadastro e atualização de Imóvel.
 */
public record ImovelRequestDto(
    @NotBlank(message = "A inscrição imobiliária é obrigatória")
    String inscricaoImobiliaria,

    @NotNull(message = "A área do terreno é obrigatória")
    @Positive(message = "A área do terreno deve ser maior que zero")
    Double areaTerreno,

    @NotNull(message = "A área construída é obrigatória")
    Double areaConstruida,

    @NotNull(message = "O valor venal é obrigatório")
    @Positive(message = "O valor venal deve ser maior que zero")
    Double valorVenal,

    @NotNull(message = "O ID do contribuinte proprietário é obrigatório")
    Long contribuinteId,

    @NotNull(message = "Os dados de endereço do imóvel são obrigatórios")
    @Valid
    EnderecoDto endereco
) {
    public Imovel paraEntidade() {
        Imovel imovel = new Imovel();
        imovel.setInscricaoImobiliaria(this.inscricaoImobiliaria);
        imovel.setAreaTerreno(this.areaTerreno);
        imovel.setAreaConstruida(this.areaConstruida);
        imovel.setValorVenal(this.valorVenal);
        imovel.setAtivo(true);
        if (this.contribuinteId != null) {
            Contribuinte c = new Contribuinte();
            c.setId(this.contribuinteId);
            imovel.setContribuinte(c);
        }
        if (this.endereco != null) {
            imovel.setEndereco(this.endereco.paraEntidade());
        }
        return imovel;
    }
}
