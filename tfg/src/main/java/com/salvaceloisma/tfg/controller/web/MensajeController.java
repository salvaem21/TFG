package com.salvaceloisma.tfg.controller.web;

import com.salvaceloisma.tfg.domain.Mensaje;
import com.salvaceloisma.tfg.service.MensajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/mensajes")
public class MensajeController {

    @Autowired
    private MensajeService mensajeService;

    @PostMapping("/{id}/upload")
    public ResponseEntity<String> uploadPdf(
        @PathVariable Long id,
        @RequestParam("solicitudInicial") MultipartFile solicitudInicial,
        @RequestParam("solicitudFirmadoEmpresa") MultipartFile solicitudFirmadoEmpresa,
        @RequestParam("solicitudFinalizada") MultipartFile solicitudFinalizada) {
        try {
            mensajeService.savePdfFiles(id, solicitudInicial, solicitudFirmadoEmpresa, solicitudFinalizada);
            return ResponseEntity.ok("PDFs subidos exitosamente");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error al subir los PDFs");
        }
    }

    @GetMapping("/{id}/download/{type}")
    public ResponseEntity<ByteArrayResource> downloadPdf(@PathVariable Long id, @PathVariable String type) {
        Mensaje mensaje = mensajeService.getMensajeById(id);
        byte[] data = mensajeService.getPdfData(mensaje, type);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + type + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }
}

