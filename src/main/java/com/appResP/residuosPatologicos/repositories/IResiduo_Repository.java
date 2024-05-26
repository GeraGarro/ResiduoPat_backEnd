package com.appResP.residuosPatologicos.repositories;

import com.appResP.residuosPatologicos.models.Residuo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface IResiduo_Repository extends CrudRepository<Residuo,Long> {

/*@Query("SELECT ticket_control, SUM(peso) AS total_pesos\n" +
        "FROM residuo\n" +
        "WHERE ticket_control = ?\n" +
        "GROUP BY ticket_control;")

    BigDecimal findPesoByTicket(Long id_Ticket);*/
}
