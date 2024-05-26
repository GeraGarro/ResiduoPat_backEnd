package com.appResP.residuosPatologicos.services;

import com.appResP.residuosPatologicos.models.Residuo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IResiduo_service
{    Optional<Residuo> findByID(Long Id);


    List<Residuo> findAll();

    void save(Residuo residuo);
    void deletebyID(Long id);


}
