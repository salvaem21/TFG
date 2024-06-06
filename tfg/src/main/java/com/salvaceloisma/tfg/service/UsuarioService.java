package com.salvaceloisma.tfg.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salvaceloisma.tfg.domain.Usuario;
import com.salvaceloisma.tfg.repository.UsuarioRepository;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;


    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario findById(Long idUsuario) {
        return usuarioRepository.findById(idUsuario).get();
    }
    
    public void update(Long idAlumno,String nombre, String apellido) {
        Usuario usuario = usuarioRepository.findById(idAlumno).get();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuarioRepository.save(usuario);
    }

    public void delete(Long idAlumno) {
        usuarioRepository.delete(usuarioRepository.getReferenceById(idAlumno));
    }

}
