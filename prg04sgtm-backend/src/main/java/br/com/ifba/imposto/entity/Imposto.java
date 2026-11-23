/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.imposto.entity;

import br.com.ifba.imovel.entity.Imovel;
import br.com.ifba.infrastructure.entity.PersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author eduar
 */

@Entity
@Table(name = "tb_imposto")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Imposto extends PersistenceEntity {

    @Column(nullable = false)
    protected String descricao;

    @Column(nullable = false)
    protected Integer anoExercicio;
    
    @Column(nullable = false)
    protected Boolean ativo = true;

    // Método abstrato exigido pelo UML. 
    // Cada imposto filho (IPTU, ITBI, etc.) terá sua própria regra de cálculo.
    public abstract Double calcularImposto(Imovel imovel);

    /**
     * Transição de domínio rico: inativação lógica do imposto.
     */
    public void inativar() {
        this.ativo = false;
    }

    /**
     * Transição de domínio rico: reativação do imposto.
     */
    public void ativar() {
        this.ativo = true;
    }
}
