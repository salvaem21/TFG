package com.salvaceloisma.tfg.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface ArchivoService {
    void guardarArchivo(MultipartFile archivo) throws IOException;
    Resource descargarArchivo(String nombre);
  
}