package com.cinematica.repository;

import com.cinematica.domain.FluxoCaixa;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the FluxoCaixa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FluxoCaixaRepository extends JpaRepository<FluxoCaixa, Long> {

}
