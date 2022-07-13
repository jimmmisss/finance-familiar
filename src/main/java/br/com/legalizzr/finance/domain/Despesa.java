package br.com.legalizzr.finance.domain;

import br.com.legalizzr.finance.domain.enumeration.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
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
    @Column(name = "jhi_user")
    private User user;

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

    public User getUser() {
        return this.user;
    }

    public Despesa user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
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
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", valor=" + getValor() +
            ", dtVcto='" + getDtVcto() + "'" +
            ", dtPgto='" + getDtPgto() + "'" +
            ", user='" + getUser() + "'" +
            "}";
    }
}
