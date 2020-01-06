package com.cinematica.repository;

import com.cinematica.domain.HorarioDisponivel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the HorarioDisponivel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HorarioDisponivelRepository extends JpaRepository<HorarioDisponivel, Long> {

}
