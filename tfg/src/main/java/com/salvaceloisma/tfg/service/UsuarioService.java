package com.salvaceloisma.tfg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.salvaceloisma.tfg.domain.Usuario;
import com.salvaceloisma.tfg.enumerados.RolUsuario;
import com.salvaceloisma.tfg.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CarpetaUsuarioService carpetaUsuarioService;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario findById(Long idUsuario) {
        return usuarioRepository.findById(idUsuario).get();
    }

    public Usuario save(String nombre, String apellido, String correo, String contrasenia, RolUsuario rol) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String contraseniaCifrada = encoder.encode(contrasenia);

        Usuario usuario = new Usuario(nombre, apellido, correo, contraseniaCifrada, rol);
        usuario = usuarioRepository.save(usuario);

        if (rol == RolUsuario.PROFESOR) {
            String rutaCarpetaUsuario = carpetaUsuarioService.crearCarpetaUsuario(usuario.getCorreo());

            usuario.setRutaCarpeta(rutaCarpetaUsuario);

            return usuarioRepository.save(usuario);
        }
        return usuario;
    }

    public void update(Long idAlumno, String nombre, String apellido) {
        Usuario usuario = usuarioRepository.findById(idAlumno).get();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuarioRepository.save(usuario);
    }

    public void delete(Long idAlumno) {
        usuarioRepository.delete(usuarioRepository.getReferenceById(idAlumno));
    }

    public long count() {
        return usuarioRepository.count();
    }
}
