package com.cinematica.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A ConfiguracaoAgenda.
 */
@Entity
@Table(name = "configuracao_agenda")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "configuracaoagenda")
public class ConfiguracaoAgenda implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "segunda")
    private Boolean segunda;

    @Column(name = "terca")
    private Boolean terca;

    @Column(name = "quarta")
    private Boolean quarta;

    @Column(name = "quinta")
    private Boolean quinta;

    @Column(name = "sexta")
    private Boolean sexta;

    @Column(name = "sabado")
    private Boolean sabado;

    @Column(name = "domingo")
    private Boolean domingo;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isSegunda() {
        return segunda;
    }

    public ConfiguracaoAgenda segunda(Boolean segunda) {
        this.segunda = segunda;
        return this;
    }

    public void setSegunda(Boolean segunda) {
        this.segunda = segunda;
    }

    public Boolean isTerca() {
        return terca;
    }

    public ConfiguracaoAgenda terca(Boolean terca) {
        this.terca = terca;
        return this;
    }

    public void setTerca(Boolean terca) {
        this.terca = terca;
    }

    public Boolean isQuarta() {
        return quarta;
    }

    public ConfiguracaoAgenda quarta(Boolean quarta) {
        this.quarta = quarta;
        return this;
    }

    public void setQuarta(Boolean quarta) {
        this.quarta = quarta;
    }

    public Boolean isQuinta() {
        return quinta;
    }

    public ConfiguracaoAgenda quinta(Boolean quinta) {
        this.quinta = quinta;
        return this;
    }

    public void setQuinta(Boolean quinta) {
        this.quinta = quinta;
    }

    public Boolean isSexta() {
        return sexta;
    }

    public ConfiguracaoAgenda sexta(Boolean sexta) {
        this.sexta = sexta;
        return this;
    }

    public void setSexta(Boolean sexta) {
        this.sexta = sexta;
    }

    public Boolean isSabado() {
        return sabado;
    }

    public ConfiguracaoAgenda sabado(Boolean sabado) {
        this.sabado = sabado;
        return this;
    }

    public void setSabado(Boolean sabado) {
        this.sabado = sabado;
    }

    public Boolean isDomingo() {
        return domingo;
    }

    public ConfiguracaoAgenda domingo(Boolean domingo) {
        this.domingo = domingo;
        return this;
    }

    public void setDomingo(Boolean domingo) {
        this.domingo = domingo;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfiguracaoAgenda)) {
            return false;
        }
        return id != null && id.equals(((ConfiguracaoAgenda) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ConfiguracaoAgenda{" +
            "id=" + getId() +
            ", segunda='" + isSegunda() + "'" +
            ", terca='" + isTerca() + "'" +
            ", quarta='" + isQuarta() + "'" +
            ", quinta='" + isQuinta() + "'" +
            ", sexta='" + isSexta() + "'" +
            ", sabado='" + isSabado() + "'" +
            ", domingo='" + isDomingo() + "'" +
            "}";
    }
}
