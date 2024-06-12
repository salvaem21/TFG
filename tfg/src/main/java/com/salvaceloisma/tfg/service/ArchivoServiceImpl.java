package com.salvaceloisma.tfg.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ArchivoServiceImpl {

    public void guardarArchivo(MultipartFile archivo, String rutaSolicitud, String nombreArchivo) throws IOException {
        Path rutaArchivo = Paths.get(rutaSolicitud, nombreArchivo);

        Files.createDirectories(rutaArchivo.getParent());

        Files.write(rutaArchivo, archivo.getBytes());
    }

}
