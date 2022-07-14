package br.com.legalizzr.finance.domain;

import br.com.legalizzr.finance.domain.enumeration.FormaPgto;
import br.com.legalizzr.finance.domain.enumeration.Mes;
import br.com.legalizzr.finance.domain.enumeration.Responsavel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Despesa.
 */
@Entity
@Table(name = "despesa")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Despesa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "mes")
    private Mes mes;

    @Column(name = "nome")
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "valor")
    private Double valor;

    @Column(name = "dt_vcto")
    private LocalDate dtVcto;

    @Column(name = "dt_pgto")
    private LocalDate dtPgto;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pgto")
    private FormaPgto formaPgto;

    @Enumerated(EnumType.STRING)
    @Column(name = "responsavel")
    private Responsavel responsavel;

    @Column(name = "dt_cadastro")
    private Instant dtCadastro;

    @ManyToOne
    @JsonIgnoreProperties(value = { "despesas", "categoria" }, allowSetters = true)
    private SubCategoria subCategoria;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Despesa id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Mes getMes() {
        return this.mes;
    }

    public Despesa mes(Mes mes) {
        this.setMes(mes);
        return this;
    }

    public void setMes(Mes mes) {
        this.mes = mes;
    }

    public String getNome() {
        return this.nome;
    }

    public Despesa nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Despesa descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValor() {
        return this.valor;
    }

    public Despesa valor(Double valor) {
        this.setValor(valor);
        return this;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public LocalDate getDtVcto() {
        return this.dtVcto;
    }

    public Despesa dtVcto(LocalDate dtVcto) {
        this.setDtVcto(dtVcto);
        return this;
    }

    public void setDtVcto(LocalDate dtVcto) {
        this.dtVcto = dtVcto;
    }

    public LocalDate getDtPgto() {
        return this.dtPgto;
    }

    public Despesa dtPgto(LocalDate dtPgto) {
        this.setDtPgto(dtPgto);
        return this;
    }

    public void setDtPgto(LocalDate dtPgto) {
        this.dtPgto = dtPgto;
    }

    public FormaPgto getFormaPgto() {
        return this.formaPgto;
    }

    public Despesa formaPgto(FormaPgto formaPgto) {
        this.setFormaPgto(formaPgto);
        return this;
    }

    public void setFormaPgto(FormaPgto formaPgto) {
        this.formaPgto = formaPgto;
    }

    public Responsavel getResponsavel() {
        return this.responsavel;
    }

    public Despesa responsavel(Responsavel responsavel) {
        this.setResponsavel(responsavel);
        return this;
    }

    public void setResponsavel(Responsavel responsavel) {
        this.responsavel = responsavel;
    }

    public Instant getDtCadastro() {
        return this.dtCadastro;
    }

    public Despesa dtCadastro(Instant dtCadastro) {
        this.setDtCadastro(dtCadastro);
        return this;
    }

    public void setDtCadastro(Instant dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    public SubCategoria getSubCategoria() {
        return this.subCategoria;
    }

    public void setSubCategoria(SubCategoria subCategoria) {
        this.subCategoria = subCategoria;
    }

    public Despesa subCategoria(SubCategoria subCategoria) {
        this.setSubCategoria(subCategoria);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Despesa)) {
            return false;
        }
        return id != null && id.equals(((Despesa) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Despesa{" +
            "id=" + getId() +
            ", mes='" + getMes() + "'" +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", valor=" + getValor() +
            ", dtVcto='" + getDtVcto() + "'" +
            ", dtPgto='" + getDtPgto() + "'" +
            ", formaPgto='" + getFormaPgto() + "'" +
            ", responsavel='" + getResponsavel() + "'" +
            ", dtCadastro='" + getDtCadastro() + "'" +
            "}";
    }
}
