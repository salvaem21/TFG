package com.salvaceloisma.tfg.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salvaceloisma.tfg.domain.Usuario;
import com.salvaceloisma.tfg.repository.inicioSesionRepository;

@Service
public class inicioSesionService {
    
    @Autowired
    private inicioSesionRepository inicioSesionRepository;

    public List<Usuario> findAll() {
        return inicioSesionRepository.findAll();
    }

    public Usuario findByCorreo(String correo) {
        return inicioSesionRepository.findByCorreo(correo);
    }

    public Usuario save(String nombre, String correo, String contrasenia, Date fechaNac) {
        return inicioSesionRepository.save(new Usuario(nombre, correo, contrasenia, fechaNac));
    }

    public Usuario findById(Long idUsuario) {
        return inicioSesionRepository.findById(idUsuario).get();
    }

    public Usuario inicioSesion(String correo, String contrasenia) throws Exception {
        Usuario usuario = inicioSesionRepository.findByCorreo(correo);
        if(usuario==null){
            throw new Exception("El correo "+ correo+ " no existe");
        }
        if(!contrasenia.matches(usuario.getContrasenia())){
            throw new Exception("La contraseña no es correcta");
        }

        return usuario;
    }
    
}
