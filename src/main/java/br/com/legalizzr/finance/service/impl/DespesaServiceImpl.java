package br.com.legalizzr.finance.service.impl;

import br.com.legalizzr.finance.domain.Despesa;
import br.com.legalizzr.finance.repository.DespesaRepository;
import br.com.legalizzr.finance.service.DespesaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Despesa}.
 */
@Service
@Transactional
public class DespesaServiceImpl implements DespesaService {

    private final Logger log = LoggerFactory.getLogger(DespesaServiceImpl.class);

    private final DespesaRepository despesaRepository;

    public DespesaServiceImpl(DespesaRepository despesaRepository) {
        this.despesaRepository = despesaRepository;
    }

    @Override
    public Despesa save(Despesa despesa) {
        log.debug("Request to save Despesa : {}", despesa);
        return despesaRepository.save(despesa);
    }

    @Override
    public Despesa update(Despesa despesa) {
        log.debug("Request to save Despesa : {}", despesa);
        return despesaRepository.save(despesa);
    }

    @Override
    public Optional<Despesa> partialUpdate(Despesa despesa) {
        log.debug("Request to partially update Despesa : {}", despesa);

        return despesaRepository
            .findById(despesa.getId())
            .map(existingDespesa -> {
                if (despesa.getMes() != null) {
                    existingDespesa.setMes(despesa.getMes());
                }
                if (despesa.getNome() != null) {
                    existingDespesa.setNome(despesa.getNome());
                }
                if (despesa.getDescricao() != null) {
                    existingDespesa.setDescricao(despesa.getDescricao());
                }
                if (despesa.getValor() != null) {
                    existingDespesa.setValor(despesa.getValor());
                }
                if (despesa.getDtVcto() != null) {
                    existingDespesa.setDtVcto(despesa.getDtVcto());
                }
                if (despesa.getDtPgto() != null) {
                    existingDespesa.setDtPgto(despesa.getDtPgto());
                }
                if (despesa.getFormaPgto() != null) {
                    existingDespesa.setFormaPgto(despesa.getFormaPgto());
                }
                if (despesa.getResponsavel() != null) {
                    existingDespesa.setResponsavel(despesa.getResponsavel());
                }
                if (despesa.getDtCadastro() != null) {
                    existingDespesa.setDtCadastro(despesa.getDtCadastro());
                }

                return existingDespesa;
            })
            .map(despesaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Despesa> findAll(Pageable pageable) {
        log.debug("Request to get all Despesas");
        return despesaRepository.findAll(pageable);
    }

    public Page<Despesa> findAllWithEagerRelationships(Pageable pageable) {
        return despesaRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Despesa> findOne(Long id) {
        log.debug("Request to get Despesa : {}", id);
        return despesaRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Despesa : {}", id);
        despesaRepository.deleteById(id);
    }
}
