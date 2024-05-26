package com.appResP.residuosPatologicos.controller;

import com.appResP.residuosPatologicos.DTO.*;
import com.appResP.residuosPatologicos.models.Generador;
import com.appResP.residuosPatologicos.models.Ticket_control;
import com.appResP.residuosPatologicos.models.Transportista;
import com.appResP.residuosPatologicos.services.imp.Generador_service;
import com.appResP.residuosPatologicos.services.imp.TicketControl_service;
import com.appResP.residuosPatologicos.services.imp.TipoResiduo_Service;
import com.appResP.residuosPatologicos.services.imp.Transportista_service;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/TicketControl")
public class TicketControl_controller {

@Autowired
    TicketControl_service ticketControlService;
@Autowired
    Generador_service generadorService;
@Autowired
    Transportista_service transportistaService;
@Autowired
    TipoResiduo_Service tipoResiduoService;
@GetMapping("UnTicket/{id}")
    public ResponseEntity<?> findTicketById(@PathVariable Long id){

    Optional<Ticket_control> ticketControlOptional=ticketControlService.findByID(id);

    if(ticketControlOptional.isPresent()){
        Ticket_control ticketControl=ticketControlOptional.get();

        Optional<Generador> generadorOptional=generadorService.findByID(ticketControl.getGenerador().getId());

        Optional<Transportista> transportistaOptional=transportistaService.findByID(ticketControl.getTransportista().getId_transportista());

      if(generadorOptional.isPresent()&&transportistaOptional.isPresent()){

          Generador generador=generadorOptional.get();
          Transportista transportista=transportistaOptional.get();

          BigDecimal pesoTotalRedondeado = ticketControlService.pesoResiduosByTicket(id).setScale(2, RoundingMode.HALF_UP);

          GeneradorDTO generadorDTO=GeneradorDTO.builder()
                  .id(generador.getId())
                  .nombre(generador.getNombre())
                  .direccion(generador.getDireccion())
                  .cuit(generador.getCuit())
                  .telefono(generador.getTelefono()).build();

          TransportistaDTO transportistaDTO=TransportistaDTO.builder()
                  .id_transportista(transportista.getId_transportista())
                  .nombre(transportista.getNombre())
                  .apellido(transportista.getApellido())
                  .cuit(transportista.getCuit())
                  .telefono(transportista.getTelefono())
                  .domicilio(transportista.getDomicilio())
                  .estado(transportista.isEstado())
                  .build();

          Ticket_controlDTO ticketControlDTO= Ticket_controlDTO.builder()
                  .id_Ticket((ticketControlService.codificacionIdTicket(id)))
                  .generador(generadorDTO)
                  .transportista(transportistaDTO)
                  .fechaEmisionTk(ticketControl.getFechaEmision())
                  .estado(ticketControl.isEstado())
                  .listaResiduos(ticketControl.getListaResiduos().stream().map(residuo -> ResiduoDTO.builder()
                          .id(residuo.getId())
                          .peso(residuo.getPeso())
                          .tipoResiduo(TipoResiduoDTO.builder()
                                  .id(residuo.getTipoResiduo().getId())
                                  .nombre(residuo.getTipoResiduo().getNombre())
                                  .estado(residuo.getTipoResiduo().isEstado())
                          .build())
                          .id_TicketControl(residuo.getTicketControl().getId_Ticket())
                          .build()).toList())
                  .pesoTotal(pesoTotalRedondeado)
                          .build();

          return ResponseEntity.ok(ticketControlDTO);
      }

    }
    return ResponseEntity.badRequest().body("No se ha podido encontrar el ticket solicitado");
}

@GetMapping("/verTodos")
    public ResponseEntity<?> findbyAllTickets(){
    List<Ticket_controlDTO> listaTickets= ticketControlService.findAll().stream()
            .map(ticketControl -> Ticket_controlDTO.builder()
                    .id_Ticket(ticketControlService.codificacionIdTicket(ticketControl.getId_Ticket()))
                    .generador(GeneradorDTO.builder()
                            .nombre(ticketControl.getGenerador().getNombre())
                            .id(ticketControl.getGenerador().getId())
                            .direccion(ticketControl.getGenerador().getDireccion())
                            .cuit(ticketControl.getGenerador().getCuit())
                            .estado(ticketControl.getGenerador().isEstado())
                            .telefono(ticketControl.getGenerador().getTelefono())
                            .build())
                    .transportista(TransportistaDTO.builder()
                            .id_transportista(ticketControl.getTransportista().getId_transportista())
                            .nombre(ticketControl.getTransportista().getNombre())
                            .apellido(ticketControl.getTransportista().getApellido())
                            .cuit(ticketControl.getTransportista().getCuit())
                            .telefono(ticketControl.getTransportista().getTelefono())
                            .domicilio(ticketControl.getTransportista().getTelefono())
                            .estado(ticketControl.getTransportista().isEstado()).build())
                    .fechaEmisionTk(ticketControl.getFechaEmision())
                    .estado(ticketControl.isEstado())
                    .listaResiduos(ticketControl.getListaResiduos().stream().map(residuo -> ResiduoDTO.builder()
                            .id(residuo.getId())
                            .peso(residuo.getPeso())
                            .tipoResiduo(TipoResiduoDTO.builder()
                                    .id(residuo.getTipoResiduo().getId())
                                    .nombre(residuo.getTipoResiduo().getNombre())
                                    .estado(residuo.getTipoResiduo().isEstado())
                                    .build())
                            .id_TicketControl(residuo.getTicketControl().getId_Ticket())
                            .build()).toList())
                    .pesoTotal(ticketControlService.pesoResiduosByTicket(ticketControl.getId_Ticket()).setScale(2, RoundingMode.HALF_UP))
                    .build())
            .toList();


    return ResponseEntity.ok(listaTickets);
}


@PostMapping("crear")
    public ResponseEntity <?> saveTicket(@RequestBody @Validated Ticket_controlDTO ticketControlDTO)  throws Exception {

    Optional<Generador> generadorOptional=generadorService.findByID(ticketControlDTO.getGenerador().getId());

    Optional<Transportista> transportistaOptional=transportistaService.findByID(ticketControlDTO.getTransportista().getId_transportista());
    try {

        if(generadorOptional.isPresent()&&transportistaOptional.isPresent()){
           Generador generador=generadorOptional.get();
           Transportista transportista=transportistaOptional.get();

           Ticket_control ticketControl=Ticket_control.builder()
                           .generador(generador)
                           .transportista(transportista)
                            .fechaEmision(ticketControlDTO.getFechaEmisionTk())
                            .estado(ticketControlDTO.isEstado()).build();

            ticketControlService.save(ticketControl);

        }


        URI location = URI.create("/api/TicketControl/crear"); // Construir la URL
        return ResponseEntity.created(location).body("Ticket creado" );
    }
   catch (IllegalArgumentException e){
return ResponseEntity.badRequest().body("Error al enviar Datos");
}
}

@DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable Long id){
    if(id!=null){
        try {
            ticketControlService.deletebyID(id);
            return ResponseEntity.ok("El Ticket con ID: "+id+ " ha sido Eliminado");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("El Ticket con ID " + id + " no existe");
        }
    }
    return ResponseEntity.notFound().build();
}
    @PutMapping("/update/{id}")
public ResponseEntity<?> updateTicket(@PathVariable Long id, @RequestBody Ticket_controlDTO ticketControlDTO){
    Optional<Ticket_control> ticketControlOptional=ticketControlService.findByID(id);
    Optional<Transportista> transportistaOptional=transportistaService.findByID(ticketControlDTO.getTransportista().getId_transportista());
    Optional<Generador> generadorOptional=generadorService.findByID(ticketControlDTO.getGenerador().getId());

    try {
        if(ticketControlOptional.isPresent()){
            Ticket_control ticketControlEdit=ticketControlOptional.get();

            if(transportistaOptional.isPresent()){
                Transportista transportista=transportistaOptional.get();
                ticketControlEdit.setTransportista(transportista);
            }

            if(generadorOptional.isPresent()){
                Generador generador=generadorOptional.get();
                ticketControlEdit.setGenerador(generador);
            }
            if(ticketControlDTO.getFechaEmisionTk()!=null){
                ticketControlEdit.setFechaEmision(ticketControlDTO.getFechaEmisionTk());
            }
            ticketControlEdit.setEstado(ticketControlDTO.isEstado());

            ticketControlService.save(ticketControlEdit);

            return ResponseEntity.ok("Ticket con id: " + id + " ha sido modificado");

        }else{
            return ResponseEntity.badRequest().body("El Ticket con ID " + id + " no existe");

        }


    }catch (Exception e){
        return ResponseEntity.badRequest().body("No ha sido posible realizar la modificación del Ticket "+id);

    }
    }

    @GetMapping("/generadorPDF/{id}")
    ResponseEntity<?> generatePdfTicket(@PathVariable Long id) throws JRException {
try {
    Optional<Ticket_control> ticketControlOptional=ticketControlService.findByID(id);
    String desktopPath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator;
    String destinatarioPatch;
    if(ticketControlOptional.isPresent()){
        String filePatch="src"+ File.separator+"main"+File.separator+"resources"+File.separator+"templates"+File.separator+ "ticketResiduo.jrxml";

        Ticket_control ticketControl=ticketControlOptional.get();


        if(ticketControl.isEstado()){
            Map<String,Object> parameters=new HashMap<>();


            parameters.put("id-Ticket",ticketControlService.codificacionIdTicket(id));
            parameters.put("Transportista.nombre",ticketControl.getTransportista().getNombre()+" "+ticketControl.getTransportista().getApellido());
            parameters.put("transportista.cuit",ticketControl.getTransportista().getCuit());
            parameters.put("transportista.domicilio",ticketControl.getTransportista().getDomicilio());
            parameters.put("transportista.telefono",ticketControl.getTransportista().getTelefono());
            parameters.put("generador.nombre",ticketControl.getGenerador().getNombre());
            parameters.put("generador.cuit",ticketControl.getGenerador().getCuit());
            parameters.put("generador.domicilio",ticketControl.getGenerador().getDireccion());
            parameters.put("imgDir","classpath/templates/");

            Date fechaEmisionDate = java.sql.Date.valueOf(ticketControl.getFechaEmision());

// Creamos un objeto SimpleDateFormat con el formato deseado
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

// Formateamos la fecha utilizando el formato y lo almacenamos en una cadena de texto
            String fechaFormateada = dateFormat.format(fechaEmisionDate);

            parameters.put("ticket.fechaEmision",fechaFormateada);


            List <residuosReportDTO> listaResiduos=ticketControl.getListaResiduos().stream().map(
                            residuo -> residuosReportDTO.builder()
                                    .id(residuo.getTipoResiduo().getCodigo())
                                    .tipoDeResiduo(residuo.getTipoResiduo().getNombre())
                                    .peso(residuo.getPeso())
                                    .build())
                    .sorted(Comparator.comparing(residuosReportDTO::getTipoDeResiduo, String.CASE_INSENSITIVE_ORDER))
                    .toList();


            JRBeanCollectionDataSource residuosDataSource=new JRBeanCollectionDataSource(listaResiduos);

            //JRBeanCollectionDataSource residuosDataSource=new JRBeanCollectionDataSource(ticketControl.getListaResiduos());
            parameters.put("residuosDateSet",residuosDataSource);

            String nombreTransportista=ticketControl.getTransportista().getNombre()+" "+ticketControl.getTransportista().getApellido();
            parameters.put("pesoTotal",ticketControlService.pesoResiduosByTicket(id).setScale(2, RoundingMode.HALF_UP));
            parameters.put("firma_transportista",nombreTransportista);

            destinatarioPatch=desktopPath +"Manifiesto-"+ticketControl.getId_Ticket()+ticketControl.getTransportista().getNombre()+ticketControl.getTransportista().getApellido()+".pdf";
            JasperReport report= JasperCompileManager.compileReport(filePatch);
            JasperPrint print= JasperFillManager.fillReport(report,parameters,new JREmptyDataSource());
            JasperExportManager.exportReportToPdfFile(print,destinatarioPatch);
            return ResponseEntity.ok("El pdf ha sido creado");
        }else{
            return ResponseEntity.badRequest().body("No es Posible generar el PDF del manifiesto mientras no este Concluido el reporte del Mismo");
        }


    }
}catch (Exception e){
    return ResponseEntity.badRequest().body(e.getMessage());
}


return ResponseEntity.notFound().build();

    }
    }

