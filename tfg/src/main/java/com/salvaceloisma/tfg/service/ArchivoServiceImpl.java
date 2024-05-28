package com.salvaceloisma.tfg.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ArchivoServiceImpl{
    

    //AGREGAR ARCHIVO A CARPETA FUNCIONAL
    public void guardarArchivo(MultipartFile archivo, String rutaSolicitud, String nombreArchivo) throws IOException {
        // Crear la ruta completa del archivo
        Path rutaArchivo = Paths.get(rutaSolicitud, nombreArchivo);
        
        // Crear la carpeta si no existe
        Files.createDirectories(rutaArchivo.getParent());
    
        // Guardar el archivo en la ruta especificada con el nuevo nombre
        Files.write(rutaArchivo, archivo.getBytes());
    }
    
}
