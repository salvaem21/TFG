package com.salvaceloisma.tfg.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CarpetaUsuarioService {

    @Value("${carpeta.usuarios.path}")
    private String carpetaUsuariosPath;

    public String crearCarpetaUsuario(String correo) {
        String rutaCarpetaUsuario = carpetaUsuariosPath + File.separator + correo;
        File carpetaUsuario = new File(rutaCarpetaUsuario);
        
        // Verificar si la carpeta del usuario ya existe
        if (!carpetaUsuario.exists()) {
            carpetaUsuario.mkdirs(); // Crear la carpeta del usuario
        }
        
        return rutaCarpetaUsuario; // Devolver la ruta completa de la carpeta del usuario
    }
}

