package com.salvaceloisma.tfg.service;

import java.io.File;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CarpetaUsuarioService {

    @Value("${carpeta.usuarios.path}")
    private String carpetaUsuariosPath;

    public String crearCarpetaUsuario(String correo) {
        String rutaCarpetaUsuario = carpetaUsuariosPath + File.separator + LocalDate.now().getYear() + File.separator
                + correo;
        File carpetaUsuario = new File(rutaCarpetaUsuario);

        if (!carpetaUsuario.exists()) {
            carpetaUsuario.mkdirs();
        }

        return rutaCarpetaUsuario;
    }
}
