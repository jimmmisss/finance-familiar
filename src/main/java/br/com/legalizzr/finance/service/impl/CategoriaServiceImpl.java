package br.com.legalizzr.finance.service.impl;

import br.com.legalizzr.finance.domain.Categoria;
import br.com.legalizzr.finance.repository.CategoriaRepository;
import br.com.legalizzr.finance.service.CategoriaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Categoria}.
 */
@Service
@Transactional
public class CategoriaServiceImpl implements CategoriaService {

    private final Logger log = LoggerFactory.getLogger(CategoriaServiceImpl.class);

    private final CategoriaRepository categoriaRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public Categoria save(Categoria categoria) {
        log.debug("Request to save Categoria : {}", categoria);
        return categoriaRepository.save(categoria);
    }

    @Override
    public Categoria update(Categoria categoria) {
        log.debug("Request to save Categoria : {}", categoria);
        return categoriaRepository.save(categoria);
    }

    @Override
    public Optional<Categoria> partialUpdate(Categoria categoria) {
        log.debug("Request to partially update Categoria : {}", categoria);

        return categoriaRepository
            .findById(categoria.getId())
            .map(existingCategoria -> {
                if (categoria.getNome() != null) {
                    existingCategoria.setNome(categoria.getNome());
                }
                if (categoria.getDescricao() != null) {
                    existingCategoria.setDescricao(categoria.getDescricao());
                }
                if (categoria.getDtCadastro() != null) {
                    existingCategoria.setDtCadastro(categoria.getDtCadastro());
                }

                return existingCategoria;
            })
            .map(categoriaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Categoria> findAll(Pageable pageable) {
        log.debug("Request to get all Categorias");
        return categoriaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Categoria> findOne(Long id) {
        log.debug("Request to get Categoria : {}", id);
        return categoriaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Categoria : {}", id);
        categoriaRepository.deleteById(id);
    }
}
