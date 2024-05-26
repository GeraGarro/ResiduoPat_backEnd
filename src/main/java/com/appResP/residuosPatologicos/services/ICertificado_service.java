package com.appResP.residuosPatologicos.services;

import com.appResP.residuosPatologicos.models.Certificado;
import com.appResP.residuosPatologicos.models.Ticket_control;
import com.appResP.residuosPatologicos.models.enums.Meses;

import java.util.List;
import java.util.Optional;

public interface ICertificado_service {

    Optional<Certificado> findByID(Long Id);
    List<Certificado> findAll();

    void save(Certificado certificado);
    void deletebyID(Long id);

    public void update(Certificado certificado);

}
