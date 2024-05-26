package com.appResP.residuosPatologicos.services.imp;

import com.appResP.residuosPatologicos.models.Tipo_residuo;
import com.appResP.residuosPatologicos.persistence.ITipo_ResiduoDAO;
import com.appResP.residuosPatologicos.repositories.ITipoResiduo_Repository;
import com.appResP.residuosPatologicos.services.ITipoResiduo_service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;

@Service
public class TipoResiduo_Service implements ITipoResiduo_service {
    @Autowired
ITipo_ResiduoDAO tipoResiduoDAO;
    @Override
    public Optional<Tipo_residuo> findByID(Long id) {
        return tipoResiduoDAO.findByID(id);
    }

    @Override
    public List<Tipo_residuo> findAll() {
        return tipoResiduoDAO.findAll();
    }

    @Override
    public void save(Tipo_residuo tipoResiduo) {
         tipoResiduoDAO.save(tipoResiduo);
    }

    @Override
    public void deletebyID(Long id) {
        tipoResiduoDAO.deletebyID(id);
    }
}
