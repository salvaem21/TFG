package com.salvaceloisma.tfg.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ArchivoServiceImpl implements ArchivoService {

    @Value("${archivo.directorio.carga}")
    private String directorioDeArchivosCarga;

    @Value("${archivo.directorio.descarga}")
    private String directorioDeArchivosDescarga;

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(directorioDeArchivosCarga));
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio para almacenar archivos.", e);
        }
    } //TENEMOS QUE CONTEMPLAR SI ES NECESARIO CREAR UNA PARA DESCARGAR AUNQUE EN TEORIA NADIE PODRIA TOCARLA AL USAR UN DESPLEGABLE

    @Override
    public void guardarArchivo(MultipartFile archivo) throws IOException {
        String nombreArchivo = StringUtils.cleanPath(archivo.getOriginalFilename());
        String uuid = UUID.randomUUID().toString();
        String nombreArchivoConUuid = uuid + "-" + nombreArchivo;
        Path rutaArchivo = Paths.get(directorioDeArchivosCarga, nombreArchivoConUuid);

        Files.copy(archivo.getInputStream(), rutaArchivo);
    }

    @Override
    public Resource descargarArchivo(String nombre) throws IOException {
        Path rutaArchivo = Paths.get(directorioDeArchivosDescarga).resolve(nombre).toAbsolutePath();
        Resource recurso = new UrlResource(rutaArchivo.toUri());

        if (!recurso.exists() || !recurso.isReadable()) {
            throw new RuntimeException("No se pudo leer el archivo.");
        }

        return recurso;
    }

    @Override
    public List<String> listarArchivosEnCarpeta() {
        Path carpeta = Paths.get(directorioDeArchivosDescarga);
        List<String> archivos = new ArrayList<>();
        if (Files.isDirectory(carpeta)) {
            try {
                Files.list(carpeta).forEach(path -> archivos.add(path.getFileName().toString()));
            } catch (IOException e) {
                throw new RuntimeException("Error al listar archivos en la carpeta.", e);
            }
        }
        return archivos;
    }
}
