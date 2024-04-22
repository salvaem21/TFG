package com.salvaceloisma.tfg.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.salvaceloisma.tfg.service.ArchivoService;

import java.io.IOException;

@RequestMapping("/subir-archivo")
@Controller
public class SubirArchivoController {

    @Autowired
    private ArchivoService archivoService;

    @GetMapping("/")
    public String mostrarFormularioSubirArchivo() {
        return "documento/subirArchivo";
    }

    @PostMapping("/subir")
    public ResponseEntity<String> subirArchivo(@RequestParam("archivo") MultipartFile archivo) {
        try {
            archivoService.guardarArchivo(archivo);
            return ResponseEntity.ok().body("Archivo subido correctamente.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir el archivo.");
        }
    }
}

