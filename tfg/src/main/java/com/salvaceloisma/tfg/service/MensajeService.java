package com.salvaceloisma.tfg.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salvaceloisma.tfg.domain.Mensaje;
import com.salvaceloisma.tfg.domain.Solicitud;
import com.salvaceloisma.tfg.domain.Usuario;
import com.salvaceloisma.tfg.repository.MensajeRepository;

@Service
public class MensajeService {

    @Autowired
    private MensajeRepository mensajeRepository;

    public List<Mensaje> findAll() {
        return mensajeRepository.findAll();
    }

    public List<Mensaje> recibirMensajes(Usuario destinatario) {
        return mensajeRepository.findByDestinatario(destinatario);
    }

    public Mensaje findBySolicitudIdSolicitud(String solicitud) {
        return mensajeRepository.findBySolicitudIdSolicitud(solicitud);
    }

    public Mensaje save(String contenido) {
        return mensajeRepository.save(new Mensaje(contenido));
    }

    public Mensaje findById(Long idMensaje) {
        return mensajeRepository.findById(idMensaje).get();
    }

    public void enviarMensaje(Usuario remitente, Usuario destinatario, String contenido, Solicitud solicitud) {
    Mensaje mensaje = mensajeRepository.findBySolicitud(solicitud);

    if (mensaje == null) {
        // El mensaje no existe, crear uno nuevo
        mensaje = new Mensaje();
        mensaje.setRemitente(remitente);
        mensaje.setDestinatario(destinatario);
        mensaje.setSolicitud(solicitud);
    } else {
        // El mensaje ya existe, actualizar su contenido y establecer novedad a true
        mensaje.setContenido(contenido);
        mensaje.setRemitente(remitente);
        mensaje.setDestinatario(destinatario);
        mensaje.setNovedad(true);
    }
    
    // Guardar el mensaje (ya sea nuevo o actualizado)
    mensajeRepository.save(mensaje);
}


    //Con este necesitamos CONTENIDO
    public void actualizarMensaje(String solicitud, Usuario destinatario, Usuario remitente, String contenido) {
        Mensaje mensaje = mensajeRepository.findBySolicitudIdSolicitud(solicitud);
        mensaje.setDestinatario(destinatario);
        mensaje.setRemitente(remitente);
        mensaje.setContenido(contenido);
        mensaje.setNovedad(true);
        mensajeRepository.save(mensaje);
    }

    //Sin CONTENIDO
    public void actualizarNotificacion(String idSolicitud, Usuario destinatario, Usuario remitente) {
        Mensaje mensaje = mensajeRepository.findBySolicitudIdSolicitud(idSolicitud);
        mensaje.setDestinatario(destinatario);
        mensaje.setRemitente(remitente);
        mensaje.setNovedad(true);
        mensajeRepository.save(mensaje);
    }

    public void delete(Long idMensaje) {
        mensajeRepository.delete(mensajeRepository.getReferenceById(idMensaje));
    }
}
