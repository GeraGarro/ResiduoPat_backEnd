package com.appResP.residuosPatologicos.repositories;

import com.appResP.residuosPatologicos.models.Certificado;
import com.appResP.residuosPatologicos.models.Ticket_control;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICertificado_Repository extends CrudRepository<Certificado,Long> {


}
