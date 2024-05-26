package com.appResP.residuosPatologicos.DTO;

import com.appResP.residuosPatologicos.models.Ticket_control;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class TransportistaDTO {

    private Long id_transportista;
    private String nombre;
    private String apellido;
    private String cuit;
    private boolean estado;
    private String telefono;
    private String domicilio;
}
