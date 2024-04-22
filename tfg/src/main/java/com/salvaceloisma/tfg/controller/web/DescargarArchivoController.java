package com.salvaceloisma.tfg.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.salvaceloisma.tfg.service.ArchivoService;

@RequestMapping("/descargar-archivo")
@Controller
public class DescargarArchivoController {

    @Autowired
    private ArchivoService archivoService;

    @GetMapping("/")
    public String mostrarFormularioDescargarArchivo() {
        return "documento/descargarArchivo";
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

