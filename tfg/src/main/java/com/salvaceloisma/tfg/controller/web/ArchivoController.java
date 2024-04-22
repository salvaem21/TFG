package com.salvaceloisma.tfg.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.salvaceloisma.tfg.service.ArchivoService;

import java.io.IOException;


@RequestMapping("/documento")
@Controller
public class ArchivoController {

    @Autowired
    private ArchivoService archivoService;
    @GetMapping("/")
    public String crearDocumento(ModelMap m) {
        m.put("view", "documento/subirDescargarArchivos");
        return "_t/frame";
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

    @GetMapping("/{nombre}")
    public ResponseEntity<Resource> descargarArchivo(@PathVariable String nombre) {
        Resource resource = archivoService.descargarArchivo(nombre);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", resource.getFilename());
    
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}
