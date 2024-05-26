package com.appResP.residuosPatologicos.repositories;


import com.appResP.residuosPatologicos.models.Residuo;
import com.appResP.residuosPatologicos.models.Ticket_control;
import com.appResP.residuosPatologicos.models.enums.Meses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository

public interface ITicket_Repository extends CrudRepository<Ticket_control,Long> {

    @Query("SELECT t FROM Ticket_control t WHERE YEAR(t.fechaEmision) = ?1 AND MONTH(t.fechaEmision) = ?2 AND t.transportista.id = ?3")
    List<Ticket_control> findTicketsByPeriodo(int anio, int mes, Long idTransportista);
}


