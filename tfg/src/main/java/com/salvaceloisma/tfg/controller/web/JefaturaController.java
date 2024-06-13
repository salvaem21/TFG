package com.salvaceloisma.tfg.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.salvaceloisma.tfg.domain.Alumno;
import com.salvaceloisma.tfg.domain.Mensaje;
import com.salvaceloisma.tfg.domain.Solicitud;
import com.salvaceloisma.tfg.domain.Usuario;
import com.salvaceloisma.tfg.enumerados.EstadoSolicitud;
import com.salvaceloisma.tfg.enumerados.RolUsuario;
import com.salvaceloisma.tfg.exception.DangerException;
import com.salvaceloisma.tfg.helper.PRG;
import com.salvaceloisma.tfg.service.AlumnoService;
import com.salvaceloisma.tfg.service.ArchivoServiceImpl;
import com.salvaceloisma.tfg.service.EmailService;
import com.salvaceloisma.tfg.service.InicioSesionService;
import com.salvaceloisma.tfg.service.MensajeService;
import com.salvaceloisma.tfg.service.SolicitudService;
import com.salvaceloisma.tfg.enumerados.Grados;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/jefatura")
@Controller
public class JefaturaController {
    @Autowired
    private EmailService emailService;

    @Autowired
    private InicioSesionService inicioSesionService;

    @Autowired
    private AlumnoService alumnoService;

    @Autowired
    private SolicitudService solicitudService;

    @Autowired
    private MensajeService mensajeService;

    @Autowired
    private ArchivoServiceImpl archivoServiceImpl;

    // VISTA DE SOLICITUDES PENDIENTES DE ACEPTAR POR JEFATURA
    @GetMapping("/solicitudesPendientesJefatura")
    public String solicitudesPendientesJefatura(Model model, HttpSession session,
            @RequestParam(name = "sort", required = false, defaultValue = "idSolicitud") String sortField,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") String sortDir,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "5") int size) throws DangerException {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        // SI NO ESTAS LOGEADO Y CON EL ROL CORRECTO SALTA EXCEPCION
        if (usuario == null || usuario.getRol() != RolUsuario.JEFATURA) {
            PRG.error("No tienes los privilegios necesarios para realizar esta accion.");
        } else {
            model.addAttribute("nombreUsuario", usuario.getNombre());
        }

        List<Mensaje> mensajes = mensajeService.recibirMensajes(usuario);

        List<Solicitud> solicitudes = new ArrayList<>();
        for (Mensaje mensaje : mensajes) {
            Solicitud solicitud = mensaje.getSolicitud();
            if (solicitud != null) {
                solicitud.setAlumnos(alumnoService.findBySolicitudIdSolicitud(solicitud.getIdSolicitud()));
                solicitudes.add(solicitud);
            }
        }

        solicitudes.sort((s1, s2) -> {
            int result;
            switch (sortField) {
                case "empresa":
                    result = s1.getEmpresa().compareTo(s2.getEmpresa());
                    break;
                case "numeroConvenio":
                    result = s1.getNumeroConvenio().compareTo(s2.getNumeroConvenio());
                    break;
                case "cicloFormativo":
                    result = s1.getCicloFormativo().compareTo(s2.getCicloFormativo());
                    break;
                case "fechaInicio":
                    result = s1.getFechaInicio().compareTo(s2.getFechaInicio());
                    break;
                default:
                    result = s1.getIdSolicitud().compareTo(s2.getIdSolicitud());
                    break;
            }
            return "asc".equals(sortDir) ? result : -result;
        });

        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), solicitudes.size());
        Page<Solicitud> pageSolicitudes = new PageImpl<>(solicitudes.subList(start, end), pageable, solicitudes.size());

        model.addAttribute("solicitudes", pageSolicitudes.getContent());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageSolicitudes.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("view", "jefatura/solicitudesPendientesJefatura");

        return "_t/frame";
    }

    // VISTA DE CORRECCION DE LA SOLICITUD SELECCIONADA
    @GetMapping("/correccionSolicitudPendienteJefatura")
    public String correccionSolicitudPendienteJefatura(
            @RequestParam("id") String idSolicitud, HttpSession session,
            ModelMap m) throws DangerException {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        // SI NO ESTAS LOGEADO Y CON EL ROL CORRECTO SALTA EXCEPCION
        if (usuario == null || usuario.getRol() != RolUsuario.JEFATURA) {
            PRG.error("No tienes los privilegios necesarios para realizar esta accion.");
        }

        Solicitud solicitud = solicitudService.findById(idSolicitud);
        String horarioSinSegundoHorario = solicitud.getHorario().replace("Segundo horario:", "");
        solicitud.setHorario(horarioSinSegundoHorario);
        m.put("solicitud", solicitud);
        m.put("alumnos", alumnoService.findBySolicitudIdSolicitud(idSolicitud));
        m.put("usuariosJefatura", inicioSesionService.obtenerUsuariosPorRol(RolUsuario.JEFATURA));
        m.put("grados", Grados.values());
        m.put("view", "jefatura/correccionSolicitudPendienteJefatura");
        return "_t/frame";
    }

    // RELLENA LAS OBSERVACIONES PORQUE POR ALGUN MOTIVO ESE PDF NO ES CORRECTO O NO
    // SE PUEDE FIRMAR
    @PostMapping("/solicitudRechazadaJefatura")
    public String solicitudRechazadaJefatura(HttpServletResponse response, HttpSession session,
            @RequestParam("idSolicitud") String idSolicitud,
            @RequestParam("observaciones") String observaciones) throws Exception {
        if (observaciones == null || observaciones.isEmpty()) {
            PRG.error("Las observaciones estan vacias", "../");
        }
        Mensaje mensaje = mensajeService.findBySolicitudIdSolicitud(idSolicitud);
        Usuario destinatario = mensaje.getRemitente();
        Usuario remitente = mensaje.getDestinatario();
        String destinatarioCorreo = destinatario.getCorreo();
        EstadoSolicitud estadoSolicitud = EstadoSolicitud.RECHAZADO_JEFATURA;

        try {
            mensajeService.actualizarMensaje(idSolicitud, destinatario, remitente, observaciones);
            solicitudService.cambiarEstadoSolicitud(idSolicitud, estadoSolicitud, remitente);
            emailService.enviarEmail(destinatarioCorreo, "(FCT'S) Datos pendientes de ser corregidos.",
                    "Jefatura ha enviado datos para corregir. Revisa tu bandeja de entrada.");

            PRG.info("Datos enviados a corregir correctamente.");
        } catch (IOException e) {
            PRG.error("Error al subir el archivo.", "/jefatura/correccionSolicitudPendienteJefatura");

        }

        return "redirect: ../";
    }

    // INCLUYE EL PDF DE JEFATURA PARA INICIAR ASI LA SOLICITUD
    @PostMapping("/solicitudAceptadaJefatura")
    public String solicitudAceptadaJefatura(HttpServletResponse response, HttpSession session,
            @RequestParam("idSolicitud") String idSolicitud,
            @RequestParam("archivoPDF") MultipartFile archivo,
            @RequestParam String numeroConvenio,
            @RequestParam String nombreEmpresa, @RequestParam String tutorEmpresa,
            @RequestParam String cifEmpresa, @RequestParam String direccionPracticas,
            @RequestParam String localidadPracticas, @RequestParam String codigoPostalPracticas,
            @RequestParam Grados cicloFormativoAlumno, @RequestParam int horasTotales,
            @RequestParam LocalDate fechaInicio, @RequestParam LocalDate fechaFin,
            @RequestParam LocalTime lunesInicio1, @RequestParam LocalTime martesInicio1,
            @RequestParam LocalTime lunesFin1, @RequestParam LocalTime martesFin1,
            @RequestParam LocalTime miercolesInicio1, @RequestParam LocalTime juevesInicio1,
            @RequestParam LocalTime viernesInicio1, @RequestParam LocalTime miercolesFin1,
            @RequestParam LocalTime juevesFin1, @RequestParam LocalTime viernesFin1,
            @RequestParam LocalTime lunesInicio2, @RequestParam LocalTime miercolesInicio2,
            @RequestParam LocalTime martesInicio2, @RequestParam LocalTime juevesInicio2,
            @RequestParam LocalTime viernesInicio2, @RequestParam LocalTime lunesFin2,
            @RequestParam LocalTime martesFin2, @RequestParam LocalTime miercolesFin2,
            @RequestParam LocalTime juevesFin2, @RequestParam LocalTime viernesFin2,
            @RequestParam int horasDia,
            @RequestParam String[] apellidosAlumno, @RequestParam String[] nombreAlumno,
            @RequestParam String[] nifAlumno) throws Exception {

        if (archivo == null || archivo.isEmpty()) {
            PRG.error("El archivo no ha sido seleccionado", "../");
        }

        try {
            Solicitud solicitud = solicitudService.findById(idSolicitud);

            StringBuilder horarioBuilder = new StringBuilder();
            horarioBuilder.append("Lunes: ").append(lunesInicio1).append(" - ").append(lunesFin1).append(".\n")
                    .append("Martes: ").append(martesInicio1).append(" - ").append(martesFin1).append(".\n")
                    .append("Miércoles: ").append(miercolesInicio1).append(" - ").append(miercolesFin1).append(".\n")
                    .append("Jueves: ").append(juevesInicio1).append(" - ").append(juevesFin1).append(".\n")
                    .append("Viernes: ").append(viernesInicio1).append(" - ").append(viernesFin1).append(".\n")
                    .append("Segundo horario: \n")
                    .append("Lunes: ").append(lunesInicio2).append(" - ").append(lunesFin2).append(".\n")
                    .append("Martes: ").append(martesInicio2).append(" - ").append(martesFin2).append(".\n")
                    .append("Miércoles: ").append(miercolesInicio2).append(" - ").append(miercolesFin2).append(".\n")
                    .append("Jueves: ").append(juevesInicio2).append(" - ").append(juevesFin2).append(".\n")
                    .append("Viernes: ").append(viernesInicio2).append(" - ").append(viernesFin2).append(".");
            String horario = horarioBuilder.toString();

            solicitudService.update(idSolicitud, numeroConvenio, nombreEmpresa, cifEmpresa, tutorEmpresa,
                    direccionPracticas,
                    localidadPracticas, codigoPostalPracticas, cicloFormativoAlumno, solicitud.getUsuario(),
                    fechaInicio, fechaFin,
                    horasDia, horasTotales, horario, null, solicitud.getEstado());

            for (int i = 0; i < apellidosAlumno.length; i++) {
                String dniAlumno = nifAlumno[i];
                Alumno alumnoExistente = alumnoService.findByDniAndSolicitudIdSolicitud(dniAlumno, idSolicitud);
                if (alumnoExistente != null) {
                    alumnoService.updateByDni(dniAlumno, nombreAlumno[i], apellidosAlumno[i], idSolicitud);
                } else {
                    alumnoService.save(nifAlumno[i], nombreAlumno[i], apellidosAlumno[i], idSolicitud);
                }
            }
            solicitud = solicitudService.findById(idSolicitud);
            String rutaUsuario = solicitud.getUsuario().getRutaCarpeta();
            String rutaSolicitud = rutaUsuario + "/" + solicitud.getEmpresa() + "/" + idSolicitud;
            solicitud.setRutaSolicitud(rutaSolicitud);
            solicitudService.save(solicitud);
            File carpetaSolicitud = new File(rutaSolicitud);
            if (!carpetaSolicitud.exists()) {
                carpetaSolicitud.mkdirs();
            }
            String nombreArchivo = "APROBADO_POR_JEFATURA " + idSolicitud + ".pdf";

            archivoServiceImpl.guardarArchivo(archivo, solicitud.getRutaSolicitud(), nombreArchivo);

            EstadoSolicitud estadoSolicitud = EstadoSolicitud.APROBADO_JEFATURA_PDF;

            Usuario remitente = (Usuario) session.getAttribute("usuario");
            Usuario destinatario = mensajeService.findBySolicitudIdSolicitud(idSolicitud).getRemitente();
            mensajeService.actualizarNotificacion(idSolicitud, destinatario, remitente);
            solicitudService.cambiarEstadoSolicitud(idSolicitud, estadoSolicitud, remitente);

            emailService.enviarEmail(destinatario.getCorreo(),
                    "(FCT'S) Solicitud aceptada por Jefatura.",
                    "Jefatura ha aceptado tu solicitud y podras descargar el PDF. Revisa tu bandeja de entrada.");

            PRG.info("Archivo enviado correctamente.");
        } catch (IOException e) {
            PRG.error("Error al subir el archivo.", "/jefatura/correccionSolicitudPendienteJefatura");
        }

        return "redirect: ../";
    }

    // VISTA DE TODAS LAS SOLICITUDES INVOLUCRADAS DEL USUARIO DE JEFATURA INICIADO
    @GetMapping("/todasSolicitudesJefatura")
    public String todasSolicitudesJefatura(ModelMap model, HttpSession session,
            @RequestParam(name = "sort", required = false, defaultValue = "idSolicitud") String sortField,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") String sortDir,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "5") int size) throws DangerException {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        // SI NO ESTAS LOGEADO Y CON EL ROL CORRECTO SALTA EXCEPCION
        if (usuario == null || usuario.getRol() != RolUsuario.JEFATURA) {
            PRG.error("No tienes los privilegios necesarios para realizar esta accion.");
        }
        List<Solicitud> allSolicitudesJefatura = solicitudService.findAllByUsuarioJefatura(usuario);

        for (Solicitud solicitud : allSolicitudesJefatura) {
            String horarioProcesado = solicitud.getHorario().replace("Segundo horario:",
                    "<br><strong>Segundo horario:</strong><br><br>");
            horarioProcesado = horarioProcesado.replace(".", "<br>");
            solicitud.setHorario(horarioProcesado);
            solicitud.setAlumnos(alumnoService.findBySolicitudIdSolicitud(solicitud.getIdSolicitud()));
        }

        allSolicitudesJefatura.sort((s1, s2) -> {
            int result;
            switch (sortField) {
                case "empresa":
                    result = s1.getEmpresa().compareTo(s2.getEmpresa());
                    break;
                case "numeroConvenio":
                    result = s1.getNumeroConvenio().compareTo(s2.getNumeroConvenio());
                    break;
                case "cicloFormativo":
                    result = s1.getCicloFormativo().compareTo(s2.getCicloFormativo());
                    break;
                case "fechaInicio":
                    result = s1.getFechaInicio().compareTo(s2.getFechaInicio());
                    break;
                case "estado":
                    result = s1.getEstado().compareTo(s2.getEstado());
                    break;
                default:
                    result = s1.getIdSolicitud().compareTo(s2.getIdSolicitud());
                    break;
            }
            return "asc".equals(sortDir) ? result : -result;
        });

        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allSolicitudesJefatura.size());
        Page<Solicitud> pageSolicitudes = new PageImpl<>(allSolicitudesJefatura.subList(start, end), pageable,
                allSolicitudesJefatura.size());

        model.put("solicitudes", pageSolicitudes.getContent());
        model.put("sortField", sortField);
        model.put("sortDir", sortDir);
        model.put("currentPage", page);
        model.put("totalPages", pageSolicitudes.getTotalPages());
        model.put("pageSize", size);
        model.put("view", "jefatura/todasSolicitudesJefatura");

        return "_t/frame";
    }

    // ERROR SI NO ANCLAS PDF NI RELLENAS OBSERVACIONES
    @PostMapping("/errorSinSeleccionarJeftura")
    public void errorSinSeleccionarJefatura()
            throws IOException, DangerException {
        PRG.error("Tienes que seleccionar una opción.", "/jefatura/solicitudesPendientesJefatura");
    }

}
