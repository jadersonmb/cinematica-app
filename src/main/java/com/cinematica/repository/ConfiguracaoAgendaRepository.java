package com.cinematica.repository;

import com.cinematica.domain.ConfiguracaoAgenda;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ConfiguracaoAgenda entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfiguracaoAgendaRepository extends JpaRepository<ConfiguracaoAgenda, Long> {

}
