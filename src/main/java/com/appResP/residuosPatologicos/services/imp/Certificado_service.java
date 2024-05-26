package com.appResP.residuosPatologicos.services.imp;

import com.appResP.residuosPatologicos.exceptions.UniqueConstraintViolationException;
import com.appResP.residuosPatologicos.models.Certificado;
import com.appResP.residuosPatologicos.models.Ticket_control;
import com.appResP.residuosPatologicos.models.enums.Meses;
import com.appResP.residuosPatologicos.persistence.ICertificadoDAO;
import com.appResP.residuosPatologicos.persistence.ITicketControlDAO;
import com.appResP.residuosPatologicos.services.ICertificado_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class Certificado_service implements ICertificado_service {
    @Autowired
    ICertificadoDAO certificadoDAO;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Certificado> findByID(Long id) {
        return certificadoDAO.findByID(id);
    }

    @Override
    public List<Certificado> findAll() {
        return certificadoDAO.findAll();
    }

    @Override
    public void save(Certificado certificado) {
        certificadoDAO.save(certificado);
    }

    @Override
    public void deletebyID(Long id) {
        certificadoDAO.deletebyID(id);
    }

    @Override
    public void update(Certificado certificado) {

        // Valida la restricción antes de guardar:
        if (wouldViolateConstraint(certificado.getAño(), certificado.getMes().getId(), certificado.getTransportista().getId_transportista(), certificado.getId())) {
            throw new UniqueConstraintViolationException("Cambiar el año y el mes violaría una restricción única.");
        }

        certificadoDAO.save(certificado);
    }
    public boolean wouldViolateConstraint(int año, int mesId, Long transportistaId, Long currentCertificadoId) {
        String sql = "SELECT COUNT(*) FROM certificado WHERE año = ? AND mes = ? AND id_transportista = ? AND id != ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, año, mesId, transportistaId, currentCertificadoId) > 0;
    }
}


