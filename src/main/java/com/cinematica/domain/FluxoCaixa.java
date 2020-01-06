package com.cinematica.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

import com.cinematica.domain.enumeration.TipoLancamento;

/**
 * A FluxoCaixa.
 */
@Entity
@Table(name = "fluxo_caixa")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "fluxocaixa")
public class FluxoCaixa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "data_lancamento")
    private Instant dataLancamento;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "valor")
    private Double valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_lancamento")
    private TipoLancamento tipoLancamento;

    @Column(name = "numero_recibo")
    private String numeroRecibo;

    @Column(name = "quantidade_parcela")
    private Integer quantidadeParcela;

    @ManyToOne
    @JsonIgnoreProperties("fluxoCaixas")
    private Especialidade especialidade;

    @ManyToOne
    @JsonIgnoreProperties("fluxoCaixas")
    private Empresa empresa;

    @ManyToOne
    @JsonIgnoreProperties("fluxoCaixas")
    private Paciente paciente;

    @ManyToOne
    @JsonIgnoreProperties("fluxoCaixas")
    private FormaPagamento formaPagamento;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDataLancamento() {
        return dataLancamento;
    }

    public FluxoCaixa dataLancamento(Instant dataLancamento) {
        this.dataLancamento = dataLancamento;
        return this;
    }

    public void setDataLancamento(Instant dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public String getDescricao() {
        return descricao;
    }

    public FluxoCaixa descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValor() {
        return valor;
    }

    public FluxoCaixa valor(Double valor) {
        this.valor = valor;
        return this;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public TipoLancamento getTipoLancamento() {
        return tipoLancamento;
    }

    public FluxoCaixa tipoLancamento(TipoLancamento tipoLancamento) {
        this.tipoLancamento = tipoLancamento;
        return this;
    }

    public void setTipoLancamento(TipoLancamento tipoLancamento) {
        this.tipoLancamento = tipoLancamento;
    }

    public String getNumeroRecibo() {
        return numeroRecibo;
    }

    public FluxoCaixa numeroRecibo(String numeroRecibo) {
        this.numeroRecibo = numeroRecibo;
        return this;
    }

    public void setNumeroRecibo(String numeroRecibo) {
        this.numeroRecibo = numeroRecibo;
    }

    public Integer getQuantidadeParcela() {
        return quantidadeParcela;
    }

    public FluxoCaixa quantidadeParcela(Integer quantidadeParcela) {
        this.quantidadeParcela = quantidadeParcela;
        return this;
    }

    public void setQuantidadeParcela(Integer quantidadeParcela) {
        this.quantidadeParcela = quantidadeParcela;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public FluxoCaixa especialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
        return this;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public FluxoCaixa empresa(Empresa empresa) {
        this.empresa = empresa;
        return this;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public FluxoCaixa paciente(Paciente paciente) {
        this.paciente = paciente;
        return this;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public FluxoCaixa formaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
        return this;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FluxoCaixa)) {
            return false;
        }
        return id != null && id.equals(((FluxoCaixa) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "FluxoCaixa{" +
            "id=" + getId() +
            ", dataLancamento='" + getDataLancamento() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", valor=" + getValor() +
            ", tipoLancamento='" + getTipoLancamento() + "'" +
            ", numeroRecibo='" + getNumeroRecibo() + "'" +
            ", quantidadeParcela=" + getQuantidadeParcela() +
            "}";
    }
}
