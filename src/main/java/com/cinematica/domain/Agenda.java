package com.cinematica.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Agenda.
 */
@Entity
@Table(name = "agenda")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "agenda")
public class Agenda implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "data_inicio")
    private Instant dataInicio;

    @Column(name = "data_fim")
    private Instant dataFim;

    @Column(name = "dia_todo")
    private Boolean diaTodo;

    @Column(name = "falta")
    private Boolean falta;

    @Column(name = "cancelou")
    private Boolean cancelou;

    @ManyToOne
    @JsonIgnoreProperties("agenda")
    private Horario horario;

    @ManyToOne
    @JsonIgnoreProperties("agenda")
    private Empresa empresa;

    @ManyToOne
    @JsonIgnoreProperties("agenda")
    private Paciente paciente;

    @ManyToOne
    @JsonIgnoreProperties("agenda")
    private Paciente funcionario;

    @ManyToOne
    @JsonIgnoreProperties("agenda")
    private Especialidade especialidade;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDataInicio() {
        return dataInicio;
    }

    public Agenda dataInicio(Instant dataInicio) {
        this.dataInicio = dataInicio;
        return this;
    }

    public void setDataInicio(Instant dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Instant getDataFim() {
        return dataFim;
    }

    public Agenda dataFim(Instant dataFim) {
        this.dataFim = dataFim;
        return this;
    }

    public void setDataFim(Instant dataFim) {
        this.dataFim = dataFim;
    }

    public Boolean isDiaTodo() {
        return diaTodo;
    }

    public Agenda diaTodo(Boolean diaTodo) {
        this.diaTodo = diaTodo;
        return this;
    }

    public void setDiaTodo(Boolean diaTodo) {
        this.diaTodo = diaTodo;
    }

    public Boolean isFalta() {
        return falta;
    }

    public Agenda falta(Boolean falta) {
        this.falta = falta;
        return this;
    }

    public void setFalta(Boolean falta) {
        this.falta = falta;
    }

    public Boolean isCancelou() {
        return cancelou;
    }

    public Agenda cancelou(Boolean cancelou) {
        this.cancelou = cancelou;
        return this;
    }

    public void setCancelou(Boolean cancelou) {
        this.cancelou = cancelou;
    }

    public Horario getHorario() {
        return horario;
    }

    public Agenda horario(Horario horario) {
        this.horario = horario;
        return this;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public Agenda empresa(Empresa empresa) {
        this.empresa = empresa;
        return this;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Agenda paciente(Paciente paciente) {
        this.paciente = paciente;
        return this;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Paciente getFuncionario() {
        return funcionario;
    }

    public Agenda funcionario(Paciente paciente) {
        this.funcionario = paciente;
        return this;
    }

    public void setFuncionario(Paciente paciente) {
        this.funcionario = paciente;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public Agenda especialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
        return this;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Agenda)) {
            return false;
        }
        return id != null && id.equals(((Agenda) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Agenda{" +
            "id=" + getId() +
            ", dataInicio='" + getDataInicio() + "'" +
            ", dataFim='" + getDataFim() + "'" +
            ", diaTodo='" + isDiaTodo() + "'" +
            ", falta='" + isFalta() + "'" +
            ", cancelou='" + isCancelou() + "'" +
            "}";
    }
}
