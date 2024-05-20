package com.salvaceloisma.tfg.service;

import java.io.IOException;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.salvaceloisma.tfg.domain.Mensaje;
import com.salvaceloisma.tfg.domain.Solicitud;
import com.salvaceloisma.tfg.domain.Usuario;
import com.salvaceloisma.tfg.repository.MensajeRepository;

import java.io.IOException;

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

     public void savePdfFiles(Long id, MultipartFile solicitudInicial, MultipartFile solicitudFirmadoEmpresa, MultipartFile solicitudFinalizada) throws IOException {
        Mensaje mensaje = mensajeRepository.findById(id).orElseThrow(() -> new RuntimeException("Mensaje no encontrado"));

        if (solicitudInicial != null && !solicitudInicial.isEmpty()) {
            mensaje.setSolicitudInicialPdf(solicitudInicial.getBytes());
        }

        if (solicitudFirmadoEmpresa != null && !solicitudFirmadoEmpresa.isEmpty()) {
            mensaje.setSolicitudFirmadoEmpresaPdf(solicitudFirmadoEmpresa.getBytes());
        }

        if (solicitudFinalizada != null && !solicitudFinalizada.isEmpty()) {
            mensaje.setSolicitudFinalizadaPdf(solicitudFinalizada.getBytes());
        }

        mensajeRepository.save(mensaje);
    }

    public Mensaje getMensajeById(Long id) {
        return mensajeRepository.findById(id).orElseThrow(() -> new RuntimeException("Mensaje no encontrado"));
    }

    public byte[] getPdfData(Mensaje mensaje, String type) {
        switch (type) {
            case "solicitudInicial":
                return mensaje.getSolicitudInicialPdf();
            case "solicitudFirmadoEmpresa":
                return mensaje.getSolicitudFirmadoEmpresaPdf();
            case "solicitudFinalizada":
                return mensaje.getSolicitudFinalizadaPdf();
            default:
                throw new IllegalArgumentException("Tipo de archivo no válido");
        }
    }
}
