package com.appResP.residuosPatologicos.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Ticket_controlDTO {

    private String id_Ticket;

    private TransportistaDTO transportista;

    private LocalDate fechaEmisionTk;

    private boolean estado;

    private GeneradorDTO generador;

    private BigDecimal pesoTotal;

    List<ResiduoDTO> listaResiduos;



}
