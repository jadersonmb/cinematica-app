package com.cinematica.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A HorarioDisponivel.
 */
@Entity
@Table(name = "horario_disponivel")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "horariodisponivel")
public class HorarioDisponivel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("horarioDisponivels")
    private Horario horario;

    @ManyToOne
    @JsonIgnoreProperties("horarioDisponivels")
    private DataFalta dataFalta;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Horario getHorario() {
        return horario;
    }

    public HorarioDisponivel horario(Horario horario) {
        this.horario = horario;
        return this;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public DataFalta getDataFalta() {
        return dataFalta;
    }

    public HorarioDisponivel dataFalta(DataFalta dataFalta) {
        this.dataFalta = dataFalta;
        return this;
    }

    public void setDataFalta(DataFalta dataFalta) {
        this.dataFalta = dataFalta;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HorarioDisponivel)) {
            return false;
        }
        return id != null && id.equals(((HorarioDisponivel) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "HorarioDisponivel{" +
            "id=" + getId() +
            "}";
    }
}
