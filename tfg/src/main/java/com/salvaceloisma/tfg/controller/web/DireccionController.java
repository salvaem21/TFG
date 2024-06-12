package com.salvaceloisma.tfg.controller.web;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.salvaceloisma.tfg.domain.Mensaje;
import com.salvaceloisma.tfg.domain.Solicitud;
import com.salvaceloisma.tfg.domain.Usuario;
import com.salvaceloisma.tfg.enumerados.EstadoSolicitud;
import com.salvaceloisma.tfg.enumerados.RolUsuario;
import com.salvaceloisma.tfg.exception.DangerException;
import com.salvaceloisma.tfg.helper.PRG;
import com.salvaceloisma.tfg.service.ArchivoServiceImpl;
import com.salvaceloisma.tfg.service.EmailService;
import com.salvaceloisma.tfg.service.MensajeService;
import com.salvaceloisma.tfg.service.SolicitudService;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/direccion")
@Controller
public class DireccionController {
    @Autowired
    private EmailService emailService;

    @Autowired
    private SolicitudService solicitudService;

    @Autowired
    private MensajeService mensajeService;

    @Autowired
    private ArchivoServiceImpl archivoServiceImpl;

    @GetMapping("/solicitudesPendientesDireccion")
    public String solicitudesPendientesDireccion(ModelMap model, HttpSession session,
            @RequestParam(name = "sort", required = false, defaultValue = "idSolicitud") String sortField,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") String sortDir,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "5") int size) throws DangerException {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getRol() != RolUsuario.DIRECTOR) {
            PRG.error("No tienes los privilegios necesarios para realizar esta accion.");
        } else {
            model.addAttribute("nombreUsuario", usuario.getNombre());
        }
        EstadoSolicitud estadoPendienteDireccion = EstadoSolicitud.PENDIENTE_FIRMA_DIRECCION;
        List<Mensaje> mensajes = mensajeService.recibirMensajes(usuario);

        mensajes.sort((m1, m2) -> {
            Solicitud solicitud1 = m1.getSolicitud();
            Solicitud solicitud2 = m2.getSolicitud();
            switch (sortField) {
                case "empresa":
                    return sortDir.equals("asc") ? solicitud1.getEmpresa().compareTo(solicitud2.getEmpresa())
                            : solicitud2.getEmpresa().compareTo(solicitud1.getEmpresa());
                case "numeroConvenio":
                    return sortDir.equals("asc")
                            ? solicitud1.getNumeroConvenio().compareTo(solicitud2.getNumeroConvenio())
                            : solicitud2.getNumeroConvenio().compareTo(solicitud1.getNumeroConvenio());
                case "cicloFormativo":
                    return sortDir.equals("asc")
                            ? solicitud1.getCicloFormativo().compareTo(solicitud2.getCicloFormativo())
                            : solicitud2.getCicloFormativo().compareTo(solicitud1.getCicloFormativo());
                case "fechaInicio":
                    return sortDir.equals("asc") ? solicitud1.getFechaInicio().compareTo(solicitud2.getFechaInicio())
                            : solicitud2.getFechaInicio().compareTo(solicitud1.getFechaInicio());
                default:
                    return sortDir.equals("asc") ? solicitud1.getIdSolicitud().compareTo(solicitud2.getIdSolicitud())
                            : solicitud2.getIdSolicitud().compareTo(solicitud1.getIdSolicitud());
            }
        });

        int start = page * size;
        int end = Math.min((start + size), mensajes.size());
        List<Mensaje> mensajesPaginados = mensajes.subList(start, end);

        model.put("estadoPendiente", estadoPendienteDireccion);
        model.put("mensajes", mensajesPaginados);
        model.put("sortField", sortField);
        model.put("sortDir", sortDir);
        model.put("currentPage", page);
        model.put("totalPages", (int) Math.ceil((double) mensajes.size() / size));
        model.put("pageSize", size);
        model.put("view", "direccion/solicitudesPendientesDireccion");

        return "_t/frame";
    }

    @GetMapping("/solicitudPendienteDireccionIndividual")
    public String solicitudPendienteDireccionIndividual(ModelMap m, HttpSession session,
            @RequestParam("id") String idSolicitud)
            throws DangerException {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getRol() != RolUsuario.DIRECTOR) {
            PRG.error("No tienes los privilegios necesarios para realizar esta accion.");
        }

        Solicitud solicitud = solicitudService.findById(idSolicitud);
        m.put("solicitud", solicitud);
        m.put("view", "direccion/solicitudPendienteDireccionIndividual");

        return "_t/frame";
    }

    @PostMapping("/solicitudAceptadaDireccion")
    public String solicitudAceptadaDireccion(
            @RequestParam("idSolicitud") String idSolicitud,
            @RequestParam("archivoPDF") MultipartFile archivo) throws Exception {

        if (archivo == null || archivo.isEmpty()) {
            PRG.error("El archivo no ha sido seleccionado", "../");
        }

        Mensaje mensaje = mensajeService.findBySolicitudIdSolicitud(idSolicitud);
        Usuario destinatario = mensaje.getRemitente();
        Usuario remitente = mensaje.getDestinatario();
        String destinatarioCorreo = destinatario.getCorreo();
        EstadoSolicitud estadoSolicitud = EstadoSolicitud.SOLICITUD_FINALIZADA;

        try {
            Solicitud solicitud = solicitudService.findById(idSolicitud);
            String rutaSolicitud = solicitud.getRutaSolicitud();

            String nombreArchivo = "SOLICITUD_FINALIZADA " + idSolicitud + ".pdf";

            archivoServiceImpl.guardarArchivo(archivo, rutaSolicitud, nombreArchivo);

            mensajeService.actualizarNotificacion(idSolicitud, destinatario, remitente);
            solicitudService.cambiarEstadoSolicitud(idSolicitud, estadoSolicitud, remitente);

            emailService.enviarEmail(destinatarioCorreo,
                    "(FCT'S) Solicitud firmada por Direccion y finalizada.",
                    "Direccion ha firmado tu solicitud y podras descargar todos los PDFs.Revisa tu bandeja de entrada.");

            PRG.info("Archivo enviado correctamente. La solicitud ha finalizado.");
        } catch (IOException e) {
            PRG.error("Error al subir el archivo.", "/direccion/solicitudesPendientesDireccion");
        }

        return "redirect: ../";
    }

    @PostMapping("/solicitudRechazadaDireccion")
    public String solicitudRechazadaDireccion(
            @RequestParam("idSolicitud") String idSolicitud,
            @RequestParam("observaciones") String observaciones) throws Exception {

        if (observaciones == null || observaciones.isEmpty()) {
            PRG.error("Las observaciones estan vacias", "../");
        }
        Mensaje mensaje = mensajeService.findBySolicitudIdSolicitud(idSolicitud);
        Usuario destinatario = mensaje.getRemitente();
        Usuario remitente = mensaje.getDestinatario();
        String destinatarioCorreo = destinatario.getCorreo();
        EstadoSolicitud estadoSolicitud = EstadoSolicitud.RECHAZADO_DIRECCION;

        try {
            mensajeService.actualizarMensaje(idSolicitud, destinatario, remitente, observaciones);
            solicitudService.cambiarEstadoSolicitud(idSolicitud, estadoSolicitud, remitente);
            emailService.enviarEmail(destinatarioCorreo, "(FCT'S) Solicitud denegada por Direccion.",
                    "Direccion ha rechazado tu solicitud. Envia de nuevo el documento. Revisa tu bandeja de entrada.");
            PRG.info("Datos enviados a corregir correctamente.");
        } catch (IOException e) {
            PRG.error("Error al subir el archivo.", "/direccion/solicitudesPendientesDireccion");
        }
        return "redirect:/";
    }

    @PostMapping("/errorSinSeleccionarDireccion")
    public void errorSinSeleccionarDireccion()
            throws IOException, DangerException {
        PRG.error("Tienes que seleccionar una opción.", "/direccion/solicitudesPendientesDireccion");
    }
}
