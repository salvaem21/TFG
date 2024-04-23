package com.salvaceloisma.tfg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salvaceloisma.tfg.domain.Usuario;
import com.salvaceloisma.tfg.enumerados.RolUsuario;
import com.salvaceloisma.tfg.repository.InicioSesionRepository;

@Service
public class InicioSesionService {
    
    @Autowired
    private InicioSesionRepository inicioSesionRepository;

    public List<Usuario> findAll() {
        return inicioSesionRepository.findAll();
    }

    public Usuario findByCorreo(String correo) {
        return inicioSesionRepository.findByCorreo(correo);
    }

    public Usuario save(String nombre, String correo, String contrasenia, String dni, RolUsuario rol) {
        Usuario usuario = new Usuario(nombre, correo, contrasenia, dni, rol);
        return inicioSesionRepository.save(usuario);
    }

    public Usuario findById(Long idUsuario) {
        return inicioSesionRepository.findById(idUsuario).get();
    }

    public List<Usuario> obtenerUsuariosPorRol(RolUsuario rol) {
        return inicioSesionRepository.findByRol(rol);
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
    
    public Usuario cambiarContrasenia(Usuario usuario,String contraseniaActual, String contraseniaNueva) throws Exception {
        if(contraseniaActual.equals(usuario.getContrasenia())){
            usuario.setContrasenia(contraseniaNueva);
            return inicioSesionRepository.save(usuario);
        }
        else{
            throw new Exception("La contraseña actual no es correcta");
        }
    }
}
