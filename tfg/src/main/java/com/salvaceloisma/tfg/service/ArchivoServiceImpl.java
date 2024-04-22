package com.salvaceloisma.tfg.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.io.File;


@Service
public class ArchivoServiceImpl implements ArchivoService {

    @Value("${archivo.directorio.carga}")
    private String directorioDeArchivosCarga;

    @Value("${archivo.directorio.descarga}")
    private String directorioDeArchivosDescarga;


    @PostConstruct
    public void init() {
        try {
            // Verificar si se trata de un sistema operativo Windows para ajustar la ruta
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                directorioDeArchivosCarga = directorioDeArchivosCarga.replace("/", "\\");
            }
            // Agregar un separador de directorios al final de la ruta base
            if (!directorioDeArchivosCarga.endsWith(File.separator)) {
                directorioDeArchivosCarga += File.separator;
            }
            Files.createDirectories(Paths.get(directorioDeArchivosCarga));
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio para almacenar archivos.");
        }
    }
    

    @Override
    public void guardarArchivo(MultipartFile archivo) throws IOException {
        String nombreArchivo = StringUtils.cleanPath(archivo.getOriginalFilename());
        String uuid = UUID.randomUUID().toString();
        String nombreArchivoConUuid = uuid + "-" + nombreArchivo;
        Path rutaArchivo = Paths.get(directorioDeArchivosCarga + nombreArchivoConUuid);
        
        Files.copy(archivo.getInputStream(), rutaArchivo);
    }

    @Override
    public Resource descargarArchivo(String nombre) {
        try {
            Path rutaArchivo = Paths.get(directorioDeArchivosDescarga).resolve(nombre).toAbsolutePath();
            Resource recurso = new UrlResource(rutaArchivo.toUri());
    
            if (recurso.exists() && recurso.isReadable()) {
                return recurso;
            } else {
                throw new RuntimeException("No se pudo leer el archivo.");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error al leer el archivo.", e);
        }
    }
    


    @Override
    public List<String> listarArchivosEnCarpeta(String rutaCarpeta) {
        File carpeta = new File(rutaCarpeta);
        List<String> archivos = new ArrayList<>();
        if (carpeta.isDirectory()) {
            File[] archivosEnCarpeta = carpeta.listFiles();
            if (archivosEnCarpeta != null) {
                for (File archivo : archivosEnCarpeta) {
                    archivos.add(archivo.getName());
                }
            }
        }
        return archivos;
    }
    
    @Override
    public void guardarArchivoEnDirectorio(MultipartFile archivo, String directorio) throws IOException {
        String nombreArchivo = StringUtils.cleanPath(archivo.getOriginalFilename());
        String uuid = UUID.randomUUID().toString();
        String nombreArchivoConUuid = uuid + "-" + nombreArchivo;
        Path rutaArchivo = Paths.get(directorio + nombreArchivoConUuid);
        
        Files.copy(archivo.getInputStream(), rutaArchivo);
    }

}
