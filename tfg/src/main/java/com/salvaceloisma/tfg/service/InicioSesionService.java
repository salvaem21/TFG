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

    @Autowired
    private CarpetaUsuarioService carpetaUsuarioService;

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
        // Obtener los mensajes con novedad para el usuario destinatario
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

    public Usuario save(String nombre, String apellido, String correo, String contrasenia, RolUsuario rol) {
        // Cifrar la contraseña
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String contraseniaCifrada = encoder.encode(contrasenia);
        //
        Usuario usuario = new Usuario(nombre, apellido, correo, contraseniaCifrada, rol);
        usuario = inicioSesionRepository.save(usuario);

        // Crear la carpeta del usuario
        if (rol == RolUsuario.PROFESOR) {
            // Crear la carpeta del usuario
            String rutaCarpetaUsuario = carpetaUsuarioService.crearCarpetaUsuario(usuario.getCorreo());

            // Actualizar la ruta de la carpeta del usuario en la entidad Usuario
            usuario.setRutaCarpeta(rutaCarpetaUsuario);

            // Guardar los cambios en la base de datos
            return inicioSesionRepository.save(usuario);
        }

        // Si el usuario no es Profesor, no se crea la carpeta y se devuelve el usuario
        // sin cambios
        return usuario;
    }

    public Usuario findById(Long idUsuario) {
        return inicioSesionRepository.findById(idUsuario).get();
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
            // Cifrar la contraseña nueva
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
            mensaje.setNovedad(false); // Establecer el booleano en false
        }
        mensajeRepository.saveAll(mensajes); // Guardar los cambios en la base de datos
    }

    public long count() {
        return inicioSesionRepository.count();
    }
}
