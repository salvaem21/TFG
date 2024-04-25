package com.salvaceloisma.tfg.controller.web;

import com.salvaceloisma.tfg.exception.DangerException;
import com.salvaceloisma.tfg.exception.InfoException;
import com.salvaceloisma.tfg.helper.PRG;
import com.salvaceloisma.tfg.service.ArchivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Controller
@RequestMapping("/documento")
public class SubirArchivoController {

    @Autowired
    private ArchivoService archivoService;

    @GetMapping("/subirArchivo")
    public String subirrArchivo(
            ModelMap m) {
        m.put("view", "documento/subirArchivo");
        return "_t/frame";
    }

    @PostMapping("/subirArchivo")
    public String  subirArchivo(@RequestParam("archivo") MultipartFile archivo) throws InfoException, DangerException {
        try {
            archivoService.guardarArchivo(archivo);
            PRG.info("Archivo subido correctamente.","/home/home");
        } catch (IOException e) {
            PRG.error("Error al subir el archivo.","/documento/subirArchivo");
            
        }
        return "redirect: ../";
    }
}
