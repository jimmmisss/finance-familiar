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
 * A Categoria.
 */
@Entity
@Table(name = "categoria")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Categoria implements Serializable {

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

    @OneToMany(mappedBy = "categoria")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "despesas", "categoria" }, allowSetters = true)
    private Set<SubCategoria> subCategorias = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Categoria id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Categoria nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Categoria descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Instant getDtCadastro() {
        return this.dtCadastro;
    }

    public Categoria dtCadastro(Instant dtCadastro) {
        this.setDtCadastro(dtCadastro);
        return this;
    }

    public void setDtCadastro(Instant dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    public Set<SubCategoria> getSubCategorias() {
        return this.subCategorias;
    }

    public void setSubCategorias(Set<SubCategoria> subCategorias) {
        if (this.subCategorias != null) {
            this.subCategorias.forEach(i -> i.setCategoria(null));
        }
        if (subCategorias != null) {
            subCategorias.forEach(i -> i.setCategoria(this));
        }
        this.subCategorias = subCategorias;
    }

    public Categoria subCategorias(Set<SubCategoria> subCategorias) {
        this.setSubCategorias(subCategorias);
        return this;
    }

    public Categoria addSubCategorias(SubCategoria subCategoria) {
        this.subCategorias.add(subCategoria);
        subCategoria.setCategoria(this);
        return this;
    }

    public Categoria removeSubCategorias(SubCategoria subCategoria) {
        this.subCategorias.remove(subCategoria);
        subCategoria.setCategoria(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Categoria)) {
            return false;
        }
        return id != null && id.equals(((Categoria) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Categoria{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", dtCadastro='" + getDtCadastro() + "'" +
            "}";
    }
}
