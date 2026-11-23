package br.com.ifba.imovel.dto;

import br.com.ifba.endereco.dto.EnderecoDto;
import br.com.ifba.imovel.entity.Imovel;

/**
 * DTO record de resposta para Imóvel.
 */
public record ImovelResponseDto(
    Long id,
    String inscricaoImobiliaria,
    Double areaTerreno,
    Double areaConstruida,
    Double valorVenal,
    Boolean ativo,
    Long contribuinteId,
    String contribuinteNome,
    EnderecoDto endereco
) {
    public static ImovelResponseDto deEntidade(Imovel imovel) {
        if (imovel == null) {
            return null;
        }
        Long cId = (imovel.getContribuinte() != null) ? imovel.getContribuinte().getId() : null;
        String cNome = (imovel.getContribuinte() != null) ? imovel.getContribuinte().getNome() : null;
        return new ImovelResponseDto(
            imovel.getId(),
            imovel.getInscricaoImobiliaria(),
            imovel.getAreaTerreno(),
            imovel.getAreaConstruida(),
            imovel.getValorVenal(),
            imovel.getAtivo(),
            cId,
            cNome,
            EnderecoDto.deEntidade(imovel.getEndereco())
        );
    }
}
