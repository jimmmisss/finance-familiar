package br.com.legalizzr.finance.service.impl;

import br.com.legalizzr.finance.domain.SubCategoria;
import br.com.legalizzr.finance.repository.SubCategoriaRepository;
import br.com.legalizzr.finance.service.SubCategoriaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SubCategoria}.
 */
@Service
@Transactional
public class SubCategoriaServiceImpl implements SubCategoriaService {

    private final Logger log = LoggerFactory.getLogger(SubCategoriaServiceImpl.class);

    private final SubCategoriaRepository subCategoriaRepository;

    public SubCategoriaServiceImpl(SubCategoriaRepository subCategoriaRepository) {
        this.subCategoriaRepository = subCategoriaRepository;
    }

    @Override
    public SubCategoria save(SubCategoria subCategoria) {
        log.debug("Request to save SubCategoria : {}", subCategoria);
        return subCategoriaRepository.save(subCategoria);
    }

    @Override
    public SubCategoria update(SubCategoria subCategoria) {
        log.debug("Request to save SubCategoria : {}", subCategoria);
        return subCategoriaRepository.save(subCategoria);
    }

    @Override
    public Optional<SubCategoria> partialUpdate(SubCategoria subCategoria) {
        log.debug("Request to partially update SubCategoria : {}", subCategoria);

        return subCategoriaRepository
            .findById(subCategoria.getId())
            .map(existingSubCategoria -> {
                if (subCategoria.getNome() != null) {
                    existingSubCategoria.setNome(subCategoria.getNome());
                }
                if (subCategoria.getDescricao() != null) {
                    existingSubCategoria.setDescricao(subCategoria.getDescricao());
                }
                if (subCategoria.getDtCadastro() != null) {
                    existingSubCategoria.setDtCadastro(subCategoria.getDtCadastro());
                }

                return existingSubCategoria;
            })
            .map(subCategoriaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubCategoria> findAll(Pageable pageable) {
        log.debug("Request to get all SubCategorias");
        return subCategoriaRepository.findAll(pageable);
    }

    public Page<SubCategoria> findAllWithEagerRelationships(Pageable pageable) {
        return subCategoriaRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubCategoria> findOne(Long id) {
        log.debug("Request to get SubCategoria : {}", id);
        return subCategoriaRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SubCategoria : {}", id);
        subCategoriaRepository.deleteById(id);
    }
}
