package com.appResP.residuosPatologicos.persistence.implementacion;

import com.appResP.residuosPatologicos.models.Ticket_control;
import com.appResP.residuosPatologicos.persistence.ITicketControlDAO;
import com.appResP.residuosPatologicos.repositories.ITicket_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
@Component
public class TicketControlDAOImp implements ITicketControlDAO {
    @Autowired
    ITicket_Repository ticket_Repositorio;
    @Override
    public Optional<Ticket_control> findByID(Long id) {
        return ticket_Repositorio.findById(id);
    }

    @Override
    public List<Ticket_control> findAll() {
        return (List<Ticket_control>) ticket_Repositorio.findAll();
    }

    @Override
    public void save(Ticket_control ticketControl) {
    ticket_Repositorio.save(ticketControl);
    }

    @Override
    public void deletebyID(Long id) {
    ticket_Repositorio.deleteById(id);
    }

   @Override
    public List<Ticket_control> findTicketsByPeriodo(int anio, int mes, Long id_transportista) {
        return ticket_Repositorio.findTicketsByPeriodo(anio,mes,id_transportista);
    }
}
