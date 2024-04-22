package com.salvaceloisma.tfg.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface ArchivoService {
    void guardarArchivo(MultipartFile archivo) throws IOException;
    void guardarArchivoEnDirectorio(MultipartFile archivo, String directorio) throws IOException;
    Resource descargarArchivo(String nombre);
    List<String> listarArchivosEnCarpeta(String directorio);
}
