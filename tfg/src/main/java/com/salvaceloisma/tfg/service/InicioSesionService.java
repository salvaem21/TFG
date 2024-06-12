package com.salvaceloisma.tfg.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.salvaceloisma.tfg.domain.Mensaje;
import com.salvaceloisma.tfg.domain.Solicitud;
import com.salvaceloisma.tfg.domain.Usuario;
import com.salvaceloisma.tfg.enumerados.RolUsuario;
import com.salvaceloisma.tfg.repository.InicioSesionRepository;
import com.salvaceloisma.tfg.repository.MensajeRepository;

@Service
public class InicioSesionService {

    @Autowired
    private InicioSesionRepository inicioSesionRepository;

    @Autowired
    private MensajeRepository mensajeRepository;

    public void enviarMensaje(Usuario remitente, Usuario destinatario, String contenido, Solicitud solicitud) {
        Mensaje mensaje = new Mensaje();
        mensaje.setRemitente(remitente);
        mensaje.setDestinatario(destinatario);
        mensaje.setContenido(contenido);
        mensaje.setSolicitud(solicitud);
        mensaje.setNovedad(true);
        mensajeRepository.save(mensaje);
    }

    public List<Mensaje> obtenerMensajesConNovedadParaUsuario(Usuario usuario) {
        List<Mensaje> mensajes = mensajeRepository.findByDestinatario(usuario);
        List<Mensaje> mensajesNuevos = new ArrayList<>();
        for (Mensaje mensaje : mensajes) {
            if (mensaje.isNovedad()) {
                mensajesNuevos.add(mensaje);
            }
        }
        return mensajesNuevos;
    }

    public List<Usuario> findAll() {
        return inicioSesionRepository.findAll();
    }

    public Usuario findByCorreo(String correo) {
        return inicioSesionRepository.findByCorreo(correo);
    }

    public Usuario findById(Long idUsuario) {
        if (idUsuario == null) {
            return null;
        }
        return inicioSesionRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con id: " + idUsuario));
    }

    public List<Usuario> obtenerUsuariosPorRol(RolUsuario rol) {
        return inicioSesionRepository.findByRol(rol);
    }

    public Usuario inicioSesion(String correo, String contrasenia) throws Exception {
        Usuario usuario = inicioSesionRepository.findByCorreo(correo);
        if (usuario == null) {
            throw new Exception("El correo " + correo + " no existe");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(contrasenia, usuario.getContrasenia())) {
            throw new Exception("La contraseña no es correcta");
        }

        return usuario;
    }

    public Usuario cambiarContrasenia(Usuario usuario, String contraseniaActual, String contraseniaNueva)
            throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(contraseniaActual, usuario.getContrasenia())) {
            String contraseniaNuevaCifrada = encoder.encode(contraseniaNueva);
            usuario.setContrasenia(contraseniaNuevaCifrada);
            return inicioSesionRepository.save(usuario);
        } else {
            throw new Exception("La contraseña actual no es correcta");
        }
    }

    public void marcarMensajesComoVistos(Usuario usuario) {
        List<Mensaje> mensajes = mensajeRepository.findByDestinatario(usuario);
        for (Mensaje mensaje : mensajes) {
            mensaje.setNovedad(false);
        }
        mensajeRepository.saveAll(mensajes);
    }
}
