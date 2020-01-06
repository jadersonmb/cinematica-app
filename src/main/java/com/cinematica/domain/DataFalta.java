package com.cinematica.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A DataFalta.
 */
@Entity
@Table(name = "data_falta")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "datafalta")
public class DataFalta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "data_falta")
    private Instant dataFalta;

    @OneToMany(mappedBy = "dataFalta")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<HorarioDisponivel> horarioDisponivels = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("dataFaltas")
    private Empresa empresa;

    @ManyToOne
    @JsonIgnoreProperties("dataFaltas")
    private ConfiguracaoAgenda configuracaoAgenda;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDataFalta() {
        return dataFalta;
    }

    public DataFalta dataFalta(Instant dataFalta) {
        this.dataFalta = dataFalta;
        return this;
    }

    public void setDataFalta(Instant dataFalta) {
        this.dataFalta = dataFalta;
    }

    public Set<HorarioDisponivel> getHorarioDisponivels() {
        return horarioDisponivels;
    }

    public DataFalta horarioDisponivels(Set<HorarioDisponivel> horarioDisponivels) {
        this.horarioDisponivels = horarioDisponivels;
        return this;
    }

    public DataFalta addHorarioDisponivel(HorarioDisponivel horarioDisponivel) {
        this.horarioDisponivels.add(horarioDisponivel);
        horarioDisponivel.setDataFalta(this);
        return this;
    }

    public DataFalta removeHorarioDisponivel(HorarioDisponivel horarioDisponivel) {
        this.horarioDisponivels.remove(horarioDisponivel);
        horarioDisponivel.setDataFalta(null);
        return this;
    }

    public void setHorarioDisponivels(Set<HorarioDisponivel> horarioDisponivels) {
        this.horarioDisponivels = horarioDisponivels;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public DataFalta empresa(Empresa empresa) {
        this.empresa = empresa;
        return this;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public ConfiguracaoAgenda getConfiguracaoAgenda() {
        return configuracaoAgenda;
    }

    public DataFalta configuracaoAgenda(ConfiguracaoAgenda configuracaoAgenda) {
        this.configuracaoAgenda = configuracaoAgenda;
        return this;
    }

    public void setConfiguracaoAgenda(ConfiguracaoAgenda configuracaoAgenda) {
        this.configuracaoAgenda = configuracaoAgenda;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DataFalta)) {
            return false;
        }
        return id != null && id.equals(((DataFalta) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "DataFalta{" +
            "id=" + getId() +
            ", dataFalta='" + getDataFalta() + "'" +
            "}";
    }
}
