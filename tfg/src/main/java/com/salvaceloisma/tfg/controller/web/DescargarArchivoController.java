package com.salvaceloisma.tfg.controller.web;

import com.salvaceloisma.tfg.service.ArchivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/descargar-archivo")
public class DescargarArchivoController {

    @Autowired
    private ArchivoService archivoService;

    @Value("${archivo.directorio.descarga}")
    private String directorioDeArchivosDescarga;

    @GetMapping("descargarArchivo")
    public String descargarArchivo(
            ModelMap m) {
        m.put("view", "documento/descargarArchivo");
        return "_t/frame";
    }



    @GetMapping("/archivos-disponibles")
    @ResponseBody
    public List<String> listarArchivosDisponibles() {
        List<String> archivos = archivoService.listarArchivosEnCarpeta();
        return archivos;
    }

    @GetMapping("/{nombre}")
    public ResponseEntity<Resource> descargarArchivo(@PathVariable String nombre) {
        try {
            Resource resource = archivoService.descargarArchivo(nombre);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", nombre);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
