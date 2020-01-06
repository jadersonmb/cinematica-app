package com.cinematica.repository;

import com.cinematica.domain.Profissao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Profissao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfissaoRepository extends JpaRepository<Profissao, Long> {

}
