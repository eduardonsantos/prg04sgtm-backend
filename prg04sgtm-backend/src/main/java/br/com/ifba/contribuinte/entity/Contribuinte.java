/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.contribuinte.entity;

import br.com.ifba.endereco.entity.Endereco;
import br.com.ifba.imovel.entity.Imovel;
import br.com.ifba.infrastructure.entity.PersistenceEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author eduar
 */
@Entity
@Table(name = "tb_contribuinte")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contribuinte extends PersistenceEntity {

    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String cpfCnpj;
    @Column(nullable = false)
    private String telefone;
    @Column(nullable = false)
    private String email;
    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;
    // relacionamento de 1 pra 1 para facilitar as buscas no banco
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    private Endereco endereco;
    // relacionamento de 1 pra muitos para facilitar as buscas no banco
    @OneToMany(mappedBy = "contribuinte")
    private List<Imovel> imoveis = new ArrayList<>();

    /**
     * Transição de domínio rico: inativação lógica do contribuinte.
     */
    public void inativar() {
        this.ativo = false;
    }

    /**
     * Transição de domínio rico: reativação do contribuinte.
     */
    public void ativar() {
        this.ativo = true;
    }
}
