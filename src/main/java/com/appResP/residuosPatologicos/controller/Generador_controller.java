package com.appResP.residuosPatologicos.controller;

import com.appResP.residuosPatologicos.DTO.GeneradorDTO;
import com.appResP.residuosPatologicos.models.Generador;
import com.appResP.residuosPatologicos.services.imp.Generador_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/Generador")

public class Generador_controller{

    @Autowired
    private Generador_service generadorService;

    @GetMapping("/infogenerador/{id}")
    public ResponseEntity<?> findByGeneradorId(@PathVariable Long id){
        Optional<Generador> generadorOptional=generadorService.findByID(id);

        if (generadorOptional.isPresent()) {
            Generador generador = generadorOptional.get();
            GeneradorDTO generadorDTO = GeneradorDTO.builder()
                    .id(generador.getId())
                    .nombre(generador.getNombre())
                    .direccion(generador.getDireccion())
                    .cuit(generador.getCuit())
                    .estado(generador.isEstado())
                    .telefono(generador.getTelefono())
                    .legajo(generador.getLegajo())
                    .estado(generador.isEstado())
                    .build();

            return ResponseEntity.ok(generadorDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/verTodos")
public ResponseEntity<?> findGeneradorAll(){
    List <GeneradorDTO> listaGeneradores= generadorService.findAll().stream()
            .map(generador -> GeneradorDTO.builder()
                    .id(generador.getId())
                    .nombre(generador.getNombre())
                    .direccion(generador.getDireccion())
                    .cuit(generador.getCuit())
                    .telefono(generador.getTelefono())
                    .legajo(generador.getLegajo())
                    .estado(generador.isEstado())
                    .build())
            .toList();
    return ResponseEntity.ok(listaGeneradores);
    }
@PostMapping("/crear")
    public ResponseEntity<?> saveGenerador(@RequestBody GeneradorDTO generadorDTO){

    try {

        if(generadorDTO.getNombre().isBlank()||
            generadorDTO.getCuit().isBlank()||
            generadorDTO.getDireccion().isBlank()){
        return ResponseEntity.badRequest().body("Faltan Datos Por Ingresar");
         }

        Generador generador=Generador.builder()
                .nombre(generadorDTO.getNombre())
                .direccion(generadorDTO.getDireccion())
                .cuit(generadorDTO.getCuit())
                .telefono(generadorDTO.getTelefono())
                .legajo(generadorDTO.getLegajo())
                .estado(generadorDTO.isEstado())
                .build();

        generadorService.save(generador);
        URI location = URI.create("/api/generador/crear"); // Construir la URL

        return ResponseEntity.created(location).body("Generador Incorporado");
    }catch (Exception e){
        return ResponseEntity.badRequest().body("Faltante de Datos,no he posible Realizar la Incorporacion del Generador");
    }

    }

@DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> deleteGenerador(@PathVariable Long id){
        if (id!=null){
            try {
                generadorService.deletebyID(id);

                return ResponseEntity.ok("El Generador con ID "+id+ " ha sido Eliminado");
            }catch (DataIntegrityViolationException e){
                String mensajeError = "No se puede eliminar el Generador con ID " + id + " ya que tiene restriccion por estar en Ticket";
                return ResponseEntity.status(HttpStatus.CONFLICT).body(mensajeError);

            }

        }
        return ResponseEntity.notFound().build();
}

@PutMapping("/update/{id}")
    public ResponseEntity<?> updateGenerador(@PathVariable Long id, @RequestBody GeneradorDTO generadorDTO){
        Optional<Generador> generadorOptional=generadorService.findByID(id);
try {
        if(generadorOptional.isPresent()){

            Generador generadorEdit=generadorOptional.get();

            generadorEdit.setNombre(generadorDTO.getNombre());
            generadorEdit.setDireccion(generadorDTO.getDireccion());
            generadorEdit.setCuit(generadorDTO.getCuit());
            generadorEdit.setTelefono(generadorDTO.getTelefono());
            generadorEdit.setLegajo(generadorDTO.getLegajo());
            generadorEdit.setEstado(generadorDTO.isEstado());
            generadorService.save(generadorEdit);

            return ResponseEntity.ok("Generador con id: " + id + " ha sido modificado");
        } else {
            return ResponseEntity.badRequest().body("El generador con ID " + id + " no existe");
        }
} catch (Exception e) {
    return ResponseEntity.badRequest().body("No ha sido posible realizar la modificación");
}
}
}



