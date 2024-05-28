package com.salvaceloisma.tfg.controller.web;

import com.salvaceloisma.tfg.service.MensajeService;
import org.springframework.beans.factory.annotation.Autowired;
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

}

