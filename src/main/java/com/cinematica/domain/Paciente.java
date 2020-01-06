package com.cinematica.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

import com.cinematica.domain.enumeration.Sexo;

/**
 * A Paciente.
 */
@Entity
@Table(name = "paciente")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "paciente")
public class Paciente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "nome_completo")
    private String nomeCompleto;

    @Column(name = "criado_em")
    private Instant criadoEm;

    @Column(name = "atualizado_em")
    private Instant atualizadoEm;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "rg")
    private String rg;

    @Column(name = "email")
    private String email;

    @Column(name = "telefone_celular")
    private String telefoneCelular;

    @Column(name = "foto_url")
    private String fotoUrl;

    @Column(name = "funcionario")
    private Boolean funcionario;

    @Column(name = "data_nascimento")
    private Instant dataNascimento;

    @Column(name = "crefito")
    private String crefito;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "foto_url_endereco")
    private String fotoUrlEndereco;

    @Column(name = "indicacao")
    private String indicacao;

    @Column(name = "ativo")
    private Boolean ativo;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo")
    private Sexo sexo;

    @ManyToOne
    @JsonIgnoreProperties("pacientes")
    private Empresa empresa;

    @ManyToOne
    @JsonIgnoreProperties("pacientes")
    private Endereco endereco;

    @ManyToOne
    @JsonIgnoreProperties("pacientes")
    private Medico medico;

    @ManyToOne
    @JsonIgnoreProperties("pacientes")
    private Profissao profissao;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Paciente nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public Paciente nomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
        return this;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }

    public Paciente criadoEm(Instant criadoEm) {
        this.criadoEm = criadoEm;
        return this;
    }

    public void setCriadoEm(Instant criadoEm) {
        this.criadoEm = criadoEm;
    }

    public Instant getAtualizadoEm() {
        return atualizadoEm;
    }

    public Paciente atualizadoEm(Instant atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
        return this;
    }

    public void setAtualizadoEm(Instant atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    public String getCpf() {
        return cpf;
    }

    public Paciente cpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public Paciente rg(String rg) {
        this.rg = rg;
        return this;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getEmail() {
        return email;
    }

    public Paciente email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefoneCelular() {
        return telefoneCelular;
    }

    public Paciente telefoneCelular(String telefoneCelular) {
        this.telefoneCelular = telefoneCelular;
        return this;
    }

    public void setTelefoneCelular(String telefoneCelular) {
        this.telefoneCelular = telefoneCelular;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public Paciente fotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
        return this;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public Boolean isFuncionario() {
        return funcionario;
    }

    public Paciente funcionario(Boolean funcionario) {
        this.funcionario = funcionario;
        return this;
    }

    public void setFuncionario(Boolean funcionario) {
        this.funcionario = funcionario;
    }

    public Instant getDataNascimento() {
        return dataNascimento;
    }

    public Paciente dataNascimento(Instant dataNascimento) {
        this.dataNascimento = dataNascimento;
        return this;
    }

    public void setDataNascimento(Instant dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getCrefito() {
        return crefito;
    }

    public Paciente crefito(String crefito) {
        this.crefito = crefito;
        return this;
    }

    public void setCrefito(String crefito) {
        this.crefito = crefito;
    }

    public String getTelefone() {
        return telefone;
    }

    public Paciente telefone(String telefone) {
        this.telefone = telefone;
        return this;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getFotoUrlEndereco() {
        return fotoUrlEndereco;
    }

    public Paciente fotoUrlEndereco(String fotoUrlEndereco) {
        this.fotoUrlEndereco = fotoUrlEndereco;
        return this;
    }

    public void setFotoUrlEndereco(String fotoUrlEndereco) {
        this.fotoUrlEndereco = fotoUrlEndereco;
    }

    public String getIndicacao() {
        return indicacao;
    }

    public Paciente indicacao(String indicacao) {
        this.indicacao = indicacao;
        return this;
    }

    public void setIndicacao(String indicacao) {
        this.indicacao = indicacao;
    }

    public Boolean isAtivo() {
        return ativo;
    }

    public Paciente ativo(Boolean ativo) {
        this.ativo = ativo;
        return this;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public Paciente sexo(Sexo sexo) {
        this.sexo = sexo;
        return this;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public Paciente empresa(Empresa empresa) {
        this.empresa = empresa;
        return this;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public Paciente endereco(Endereco endereco) {
        this.endereco = endereco;
        return this;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Medico getMedico() {
        return medico;
    }

    public Paciente medico(Medico medico) {
        this.medico = medico;
        return this;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Profissao getProfissao() {
        return profissao;
    }

    public Paciente profissao(Profissao profissao) {
        this.profissao = profissao;
        return this;
    }

    public void setProfissao(Profissao profissao) {
        this.profissao = profissao;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Paciente)) {
            return false;
        }
        return id != null && id.equals(((Paciente) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Paciente{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", nomeCompleto='" + getNomeCompleto() + "'" +
            ", criadoEm='" + getCriadoEm() + "'" +
            ", atualizadoEm='" + getAtualizadoEm() + "'" +
            ", cpf='" + getCpf() + "'" +
            ", rg='" + getRg() + "'" +
            ", email='" + getEmail() + "'" +
            ", telefoneCelular='" + getTelefoneCelular() + "'" +
            ", fotoUrl='" + getFotoUrl() + "'" +
            ", funcionario='" + isFuncionario() + "'" +
            ", dataNascimento='" + getDataNascimento() + "'" +
            ", crefito='" + getCrefito() + "'" +
            ", telefone='" + getTelefone() + "'" +
            ", fotoUrlEndereco='" + getFotoUrlEndereco() + "'" +
            ", indicacao='" + getIndicacao() + "'" +
            ", ativo='" + isAtivo() + "'" +
            ", sexo='" + getSexo() + "'" +
            "}";
    }
}
