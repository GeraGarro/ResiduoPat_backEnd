package com.appResP.residuosPatologicos.controller;
import com.appResP.residuosPatologicos.DTO.TipoResiduoDTO;
import com.appResP.residuosPatologicos.models.Tipo_residuo;
import com.appResP.residuosPatologicos.services.imp.TipoResiduo_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/tipoResiduo")
public class TipoResiduo_controller {

    @Autowired
    TipoResiduo_Service tipoResiduoService;

    //Muestra el un unico Elemento de Tipo de Residuo
@GetMapping("/unTipoResiduo/{id}")
    public ResponseEntity <?> findTipoResiduobyId(@PathVariable Long id){
    Optional<Tipo_residuo> tipoResiduoOptional=tipoResiduoService.findByID(id);
if (tipoResiduoOptional.isPresent()){
    Tipo_residuo tipoResiduo=tipoResiduoOptional.get();
    TipoResiduoDTO tipoResiduoDTO= TipoResiduoDTO.builder()
            .id(tipoResiduo.getId())
            .nombre(tipoResiduo.getNombre())
            .codigo(tipoResiduo.getCodigo())
            .estado(tipoResiduo.isEstado()).build();

  return ResponseEntity.ok(tipoResiduoDTO);
}
return ResponseEntity.notFound().build();
}
//Muestra todos los tipos de residuos
@GetMapping("/verTodos")
    public ResponseEntity <?> findTipoAll (){
    List<TipoResiduoDTO> listaDeTipos= tipoResiduoService.findAll().stream()
            .map(tipoResiduo -> TipoResiduoDTO.builder()
                    .id(tipoResiduo.getId())
                    .nombre(tipoResiduo.getNombre())
                    .estado(tipoResiduo.isEstado())
                    .codigo(tipoResiduo.getCodigo())
                    .build()).toList();
    return ResponseEntity.ok(listaDeTipos);
    }

    //Creación de un Nuevo Tipo de Residuo
    @PostMapping("/crear")
    public ResponseEntity <?> saveTipoResiduo(@RequestBody TipoResiduoDTO tipoResiduoDTO) throws URISyntaxException {
    if(!tipoResiduoDTO.getNombre().isBlank()){
        tipoResiduoService.save(Tipo_residuo.builder()
                .nombre(tipoResiduoDTO.getNombre())
                .codigo(tipoResiduoDTO.getCodigo())
                .estado(tipoResiduoDTO.isEstado())
                .build());
        return ResponseEntity.created(new URI("/api/tipoResiduo/crear")).body("El Tipo de Residuo Ha sido Creado");
    }
    return ResponseEntity.badRequest().build();
    }

    //Eliminacion de Un Tipo de Residuo
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity <?> deleteTipoResiduo(@PathVariable Long id){
    if(id!=null){
        tipoResiduoService.deletebyID(id);
        return ResponseEntity.ok("El Tipo Residuo con "+id+" ha sido Eliminado");
    }
    return ResponseEntity.notFound().build();
    }
//Actualizacion de un Elemento de Tipo Residuo
  @PostMapping("/update/{id}")
    public ResponseEntity<?> updateTipoResiduo(@PathVariable Long id, @RequestBody TipoResiduoDTO tipoResiduoDTO){
    Optional<Tipo_residuo> tipoResiduoOptional=tipoResiduoService.findByID(id);
try {
    if(tipoResiduoOptional.isPresent()){
        Tipo_residuo tipoResiduo=tipoResiduoOptional.get();
        tipoResiduo.setNombre(tipoResiduoDTO.getNombre());
        tipoResiduo.setCodigo(tipoResiduoDTO.getCodigo());
        tipoResiduo.setEstado(tipoResiduoDTO.isEstado());

        tipoResiduoService.save(tipoResiduo);
        return  ResponseEntity.ok().body("El Tipo De residuo con el "+id+" ha sido Modificaod");
    }else {
        return ResponseEntity.badRequest().body("El generador con ID " + id + " no existe");
    }
}catch (Exception e) {
    return ResponseEntity.badRequest().body("No ha sido posible realizar la modificación");
}
  }

}
