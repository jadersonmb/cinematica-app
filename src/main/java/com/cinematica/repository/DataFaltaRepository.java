package com.cinematica.repository;

import com.cinematica.domain.DataFalta;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DataFalta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataFaltaRepository extends JpaRepository<DataFalta, Long> {

}
