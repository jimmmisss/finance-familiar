package br.com.legalizzr.finance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SubCategoria.
 */
@Entity
@Table(name = "sub_categoria")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SubCategoria implements Serializable {

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

    @Column(name = "dt_cadastro")
    private Instant dtCadastro;

    @OneToMany(mappedBy = "subCategoria")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "subCategoria" }, allowSetters = true)
    private Set<Despesa> despesas = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "subCategorias" }, allowSetters = true)
    private Categoria categoria;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SubCategoria id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public SubCategoria nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public SubCategoria descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Instant getDtCadastro() {
        return this.dtCadastro;
    }

    public SubCategoria dtCadastro(Instant dtCadastro) {
        this.setDtCadastro(dtCadastro);
        return this;
    }

    public void setDtCadastro(Instant dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    public Set<Despesa> getDespesas() {
        return this.despesas;
    }

    public void setDespesas(Set<Despesa> despesas) {
        if (this.despesas != null) {
            this.despesas.forEach(i -> i.setSubCategoria(null));
        }
        if (despesas != null) {
            despesas.forEach(i -> i.setSubCategoria(this));
        }
        this.despesas = despesas;
    }

    public SubCategoria despesas(Set<Despesa> despesas) {
        this.setDespesas(despesas);
        return this;
    }

    public SubCategoria addDespesas(Despesa despesa) {
        this.despesas.add(despesa);
        despesa.setSubCategoria(this);
        return this;
    }

    public SubCategoria removeDespesas(Despesa despesa) {
        this.despesas.remove(despesa);
        despesa.setSubCategoria(null);
        return this;
    }

    public Categoria getCategoria() {
        return this.categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public SubCategoria categoria(Categoria categoria) {
        this.setCategoria(categoria);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubCategoria)) {
            return false;
        }
        return id != null && id.equals(((SubCategoria) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubCategoria{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", dtCadastro='" + getDtCadastro() + "'" +
            "}";
    }
}
