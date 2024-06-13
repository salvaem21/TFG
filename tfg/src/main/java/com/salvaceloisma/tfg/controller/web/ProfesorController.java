package com.salvaceloisma.tfg.controller.web;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import com.salvaceloisma.tfg.enumerados.Grados;
import com.salvaceloisma.tfg.enumerados.RolUsuario;
import com.salvaceloisma.tfg.exception.DangerException;
import com.salvaceloisma.tfg.exception.InfoException;
import com.salvaceloisma.tfg.helper.PRG;
import com.salvaceloisma.tfg.service.AlumnoService;
import com.salvaceloisma.tfg.service.ArchivoServiceImpl;
import com.salvaceloisma.tfg.service.EmailService;
import com.salvaceloisma.tfg.service.InicioSesionService;
import com.salvaceloisma.tfg.service.MensajeService;
import com.salvaceloisma.tfg.service.SolicitudService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/profesor")
@Controller
public class ProfesorController {
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

    // VISTA PARA RELLENAR LOS DATOS PARA LA SOLICITUD
    @GetMapping("/datosProfesorAJefatura")
    public String datosProfesorAJefatura(ModelMap m, HttpSession session) throws DangerException {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        // SI NO ESTAS LOGEADO Y CON EL ROL CORRECTO SALTA EXCEPCION
        if (usuario == null || usuario.getRol() != RolUsuario.PROFESOR) {
            PRG.error("No tienes los privilegios necesarios para realizar esta accion.");
        }
        m.put("usuariosJefatura", inicioSesionService.obtenerUsuariosPorRol(RolUsuario.JEFATURA));
        m.put("grados", Grados.values());
        m.put("view", "profesor/datosProfesorAJefatura");
        return "_t/frame";
    }

    // ENVIO DE DATOS AL USUARIO DE JEFATURA CORRESPONDIENTE (CREA LA SOLICITUD,
    // USUARIOS, MENSAJE EN LA BBDD)
    @PostMapping("/datosProfesorAJefatura")
    public String datosProfesorAJefatura(HttpServletResponse response, HttpSession session,
            @RequestParam(required = false) String idSolicitud,
            @RequestParam String numeroConvenio,
            @RequestParam String nombreEmpresa, @RequestParam String tutorEmpresa,
            @RequestParam String cifEmpresa, @RequestParam String direccionPracticas,
            @RequestParam String localidadPracticas, @RequestParam String codigoPostalPracticas,
            @RequestParam String[] apellidosAlumno, @RequestParam String[] nombreAlumno,
            @RequestParam String[] nifAlumno, @RequestParam Grados cicloFormativoAlumno,
            @RequestParam Integer horasTotales, @RequestParam LocalDate fechaInicio,
            @RequestParam LocalDate fechaFin, @RequestParam LocalTime lunesInicio1,
            @RequestParam LocalTime martesInicio1, @RequestParam LocalTime lunesFin1,
            @RequestParam LocalTime martesFin1, @RequestParam LocalTime miercolesInicio1,
            @RequestParam LocalTime juevesInicio1, @RequestParam LocalTime viernesInicio1,
            @RequestParam LocalTime miercolesFin1, @RequestParam LocalTime juevesFin1,
            @RequestParam LocalTime viernesFin1, @RequestParam LocalTime lunesInicio2,
            @RequestParam LocalTime miercolesInicio2,
            @RequestParam LocalTime martesInicio2, @RequestParam LocalTime juevesInicio2,
            @RequestParam LocalTime viernesInicio2, @RequestParam LocalTime lunesFin2,
            @RequestParam LocalTime martesFin2,
            @RequestParam LocalTime miercolesFin2, @RequestParam LocalTime juevesFin2,
            @RequestParam LocalTime viernesFin2, @RequestParam Integer horasDia,
            @RequestParam(name = "rolUsuario", required = false) Long usuarioEnvio, ModelMap m)
            throws DangerException, InfoException {

        boolean creandoNuevaSolicitud = (idSolicitud == null || idSolicitud.isEmpty());
        Usuario usuario = (Usuario) session.getAttribute("usuario");

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

        StringBuilder datos = new StringBuilder();

        datos.append("Datos comunes:\n");
        datos.append("Numero Convenio: ").append(numeroConvenio).append("\n");
        datos.append("Nombre empresa: ").append(nombreEmpresa).append("\n");
        datos.append("Tutor empresa: ").append(tutorEmpresa).append("\n");
        datos.append("CIF empresa: ").append(cifEmpresa).append("\n");
        datos.append("Dirección prácticas: ").append(direccionPracticas).append("\n");
        datos.append("Localidad prácticas: ").append(localidadPracticas).append("\n");
        datos.append("Código postal prácticas: ").append(codigoPostalPracticas).append("\n");
        datos.append("Horas totales: ").append(horasTotales).append("\n");
        datos.append("Fecha de inicio: ").append(fechaInicio).append("\n");
        datos.append("Fecha de fin: ").append(fechaFin).append("\n");
        datos.append("Horas por día: ").append(horasDia).append("\n");

        Usuario remitente = (Usuario) session.getAttribute("usuario");
        Usuario destinatario = inicioSesionService.findById(usuarioEnvio);
        Mensaje mensaje = mensajeService.findBySolicitudIdSolicitud(idSolicitud);
        if (destinatario == null) {
            if (mensaje == null) {
                PRG.error("No se encontró ningún mensaje asociado con la solicitud ID " + idSolicitud);
                return "redirect:../";
            }
            destinatario = mensaje.getRemitente();
        }
        EstadoSolicitud estado = EstadoSolicitud.PENDIENTE_FIRMA_JEFATURA;
        String observaciones = "";
        Solicitud solicitud = null;

        try {

            if (creandoNuevaSolicitud) {
                solicitud = solicitudService.save(numeroConvenio, nombreEmpresa, cifEmpresa, tutorEmpresa,
                        direccionPracticas, localidadPracticas, codigoPostalPracticas, cicloFormativoAlumno, usuario,
                        fechaInicio, fechaFin, horasDia, horasTotales, horario, observaciones, estado);
                idSolicitud = solicitud.getIdSolicitud();
                String empresa = solicitud.getEmpresa();

                String rutaUsuario = usuario.getRutaCarpeta();
                String rutaSolicitud = rutaUsuario + "/" + empresa + "/" + idSolicitud;
                File carpetaSolicitud = new File(rutaUsuario);
                if (!carpetaSolicitud.exists()) {
                    carpetaSolicitud.mkdirs();
                }
                solicitud.setRutaSolicitud(rutaSolicitud);
                solicitud.setUsuarioJefatura(inicioSesionService.findById(usuarioEnvio));
                solicitudService.save(solicitud);
            } else {
                solicitudService.update(idSolicitud, numeroConvenio, nombreEmpresa, cifEmpresa, tutorEmpresa,
                        direccionPracticas, localidadPracticas, codigoPostalPracticas, cicloFormativoAlumno, usuario,
                        fechaInicio, fechaFin, horasDia, horasTotales, horario, observaciones, estado);

                solicitud = solicitudService.findById(idSolicitud);
                idSolicitud = solicitud.getIdSolicitud();
                String empresa = solicitud.getEmpresa();

                String rutaUsuario = usuario.getRutaCarpeta();
                String rutaSolicitud = rutaUsuario + "/" + empresa + "/" + idSolicitud;
                File carpetaSolicitud = new File(rutaUsuario);
                if (!carpetaSolicitud.exists()) {
                    carpetaSolicitud.mkdirs();
                }
                solicitud.setRutaSolicitud(rutaSolicitud);
                solicitudService.save(solicitud);
            }

            alumnoService.deleteAllBySolicitud(solicitud);
            for (int i = 0; i < apellidosAlumno.length; i++) {
                datos.append("Alumno ").append(i + 1).append(":\n");
                datos.append("Apellidos alumno: ").append(apellidosAlumno[i]).append("\n");
                datos.append("Nombre alumno: ").append(nombreAlumno[i]).append("\n");
                datos.append("NIF alumno: ").append(nifAlumno[i]).append("\n");
                datos.append("Ciclo formativo: ").append(cicloFormativoAlumno).append("\n");
                datos.append("Fecha de nacimiento alumno: ").append("\n");
                datos.append("\n");
                try {
                    alumnoService.save(nifAlumno[i], nombreAlumno[i], apellidosAlumno[i], idSolicitud);
                } catch (Exception e) {
                    PRG.error("Los alumnos no pueden ser creados o actualizados.");
                }
            }

            String correo;
            if (usuarioEnvio != null) {
                Usuario usuarioDestinatario = inicioSesionService.findById(usuarioEnvio);
                correo = usuarioDestinatario != null ? usuarioDestinatario.getCorreo() : "default@example.com";
            } else {
                correo = destinatario.getCorreo();
            }
            mensajeService.enviarMensaje(remitente, destinatario, observaciones, solicitud);
            emailService.enviarEmail(correo, "(FCT'S) Datos pendientes de ser revisados.",
                    "Un profesor ha enviado unos datos. Revisa tu bandeja de entrada.");

        } catch (Exception e) {
            PRG.error("Los datos no pudieron enviarse correctamente. Error: " + e.getMessage());
        }
        PRG.info("Datos enviados correctamente.");
        return "redirect:../";
    }

    // VISTA DE SOLICITUDES RECHAZADAS POR JEFATURA
    @GetMapping("/solicitudesRechazadasPorJefatura")
    public String solicitudesRechazadasPorJefatura(Model model, HttpSession session,
            @RequestParam(name = "sort", required = false, defaultValue = "idSolicitud") String sortField,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") String sortDir,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "5") int size) throws DangerException {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        // SI NO ESTAS LOGEADO Y CON EL ROL CORRECTO SALTA EXCEPCION
        if (usuario == null || usuario.getRol() != RolUsuario.PROFESOR) {
            PRG.error("No tienes los privilegios necesarios para realizar esta accion.");
        } else {
            model.addAttribute("nombreUsuario", usuario.getNombre());
        }
        EstadoSolicitud estadoRechazado = EstadoSolicitud.RECHAZADO_JEFATURA;
        List<Mensaje> mensajes = mensajeService.recibirMensajes(usuario);
        List<Mensaje> mensajesPendientesCorreccion = mensajes.stream()
                .filter(mensaje -> mensaje.getSolicitud() != null
                        && mensaje.getSolicitud().getEstado() == estadoRechazado)
                .collect(Collectors.toList());

        mensajesPendientesCorreccion.sort((m1, m2) -> {
            Solicitud s1 = m1.getSolicitud();
            Solicitud s2 = m2.getSolicitud();
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
                case "observaciones":
                    result = m1.getContenido().compareTo(m2.getContenido());
                    break;
                default:
                    result = s1.getIdSolicitud().compareTo(s2.getIdSolicitud());
                    break;
            }
            return "asc".equals(sortDir) ? result : -result;
        });

        int start = page * size;
        int end = Math.min((start + size), mensajesPendientesCorreccion.size());
        List<Mensaje> mensajesPaginados = mensajesPendientesCorreccion.subList(start, end);

        model.addAttribute("estadoRechazado", estadoRechazado);
        model.addAttribute("mensajes", mensajesPaginados);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) mensajesPendientesCorreccion.size() / size));
        model.addAttribute("pageSize", size);
        model.addAttribute("view", "profesor/solicitudesRechazadasPorJefatura");

        return "_t/frame";
    }

    // VISTA DE CORRECCION DE LA SOLICITUD RECHAZADA POR JEFATURA SELECCIONADA
    @GetMapping("/solicitudRechazadaPorJefaturaCorreccion")
    public String solicitudRechazadaPorJefaturaCorreccion(@RequestParam("id") String idSolicitud, ModelMap m,
            HttpSession session)
            throws DangerException {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        // SI NO ESTAS LOGEADO Y CON EL ROL CORRECTO SALTA EXCEPCION
        if (usuario == null || usuario.getRol() != RolUsuario.PROFESOR) {
            PRG.error("No tienes los privilegios necesarios para realizar esta accion.");
        }
        Solicitud solicitud = solicitudService.findById(idSolicitud);
        String horarioSinSegundoHorario = solicitud.getHorario().replace("Segundo horario:", "");
        solicitud.setHorario(horarioSinSegundoHorario);
        m.put("solicitud", solicitud);
        m.put("grados", Grados.values());
        m.put("alumnos", alumnoService.findBySolicitudIdSolicitud(idSolicitud));
        m.put("usuariosJefatura", inicioSesionService.obtenerUsuariosPorRol(RolUsuario.JEFATURA));
        m.put("view", "profesor/solicitudRechazadaPorJefaturaCorreccion");
        return "_t/frame";
    }

    // VISTA DE LAS SOLICITUDES APROBADAS POR JEFATURA CON SU PDF
    @GetMapping("/solicitudesAprobadaPorJefatura")
    public String solicitudesAprobadaPorJefatura(ModelMap model, HttpSession session,
            @RequestParam(name = "sort", required = false, defaultValue = "idSolicitud") String sortField,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") String sortDir,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "5") int size) throws DangerException {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        // SI NO ESTAS LOGEADO Y CON EL ROL CORRECTO SALTA EXCEPCION
        if (usuario == null || usuario.getRol() != RolUsuario.PROFESOR) {
            PRG.error("No tienes los privilegios necesarios para realizar esta accion.");
        } else {
            model.addAttribute("nombreUsuario", usuario.getNombre());
        }
        EstadoSolicitud estadoAprobado1 = EstadoSolicitud.APROBADO_JEFATURA_PDF;
        EstadoSolicitud estadoAprobado2 = EstadoSolicitud.RECHAZADO_DIRECCION;

        List<Mensaje> mensajes = mensajeService.recibirMensajes(usuario);

        List<Mensaje> mensajesAprobados = mensajes.stream()
                .filter(mensaje -> mensaje.getSolicitud() != null &&
                        (mensaje.getSolicitud().getEstado() == estadoAprobado1
                                || mensaje.getSolicitud().getEstado() == estadoAprobado2))
                .collect(Collectors.toList());

        mensajesAprobados.sort((m1, m2) -> {
            Solicitud s1 = m1.getSolicitud();
            Solicitud s2 = m2.getSolicitud();
            int result;
            switch (sortField) {
                case "empresa":
                    result = s1.getEmpresa().compareTo(s2.getEmpresa());
                    break;
                case "cicloFormativo":
                    result = s1.getCicloFormativo().compareTo(s2.getCicloFormativo());
                    break;
                case "observaciones":
                    result = m1.getContenido().compareTo(m2.getContenido());
                    break;
                default:
                    result = s1.getIdSolicitud().compareTo(s2.getIdSolicitud());
                    break;
            }
            return "asc".equals(sortDir) ? result : -result;
        });

        int start = page * size;
        int end = Math.min((start + size), mensajesAprobados.size());
        List<Mensaje> mensajesPaginados = mensajesAprobados.subList(start, end);

        model.put("estadoAprobado1", estadoAprobado1);
        model.put("estadoAprobado2", estadoAprobado2);
        model.put("mensajes", mensajesPaginados);
        model.put("sortField", sortField);
        model.put("sortDir", sortDir);
        model.put("currentPage", page);
        model.put("totalPages", (int) Math.ceil((double) mensajesAprobados.size() / size));
        model.put("pageSize", size);
        model.put("view", "profesor/solicitudesAprobadaPorJefatura");

        return "_t/frame";
    }

    // ENVIO DEL PDF A DIRECCION UNA VEZ APROBADA POR JEFATURA
    @PostMapping("/solicitudesAprobadaPorJefatura")
    public String solicitudesAprobadaPorJefatura(HttpServletResponse response,
            HttpSession session,
            @RequestParam("idSolicitud") String idSolicitud,
            @RequestParam("archivo") MultipartFile archivo) throws Exception {

        if (archivo.isEmpty()) {
            PRG.error("Por favor, seleccione un archivo antes de enviar.",
                    "/profesor/solicitudesAprobadaPorJefatura");
            return "redirect:/profesor/solicitudesAprobadaPorJefatura";
        }

        Mensaje mensaje = mensajeService.findBySolicitudIdSolicitud(idSolicitud);

        List<Usuario> directores = inicioSesionService.obtenerUsuariosPorRol(RolUsuario.DIRECTOR);
        if (directores.isEmpty()) {
            PRG.error("No se encontraron usuarios con el rol de DIRECTOR.", "/profesor/solicitudesAprobadaPorJefatura");
            return "redirect:/profesor/solicitudesAprobadaPorJefatura";
        }
        Usuario destinatario = directores.get(0);
        Usuario remitente = mensaje.getDestinatario();
        String destinatarioCorreo = destinatario.getCorreo();
        EstadoSolicitud estadoSolicitud = EstadoSolicitud.PENDIENTE_FIRMA_DIRECCION;

        try {
            Solicitud solicitud = solicitudService.findById(idSolicitud);
            String rutaSolicitud = solicitud.getRutaSolicitud();

            String nombreArchivo = "FIRMADO_POR_EMPRESA " + idSolicitud + ".pdf";

            archivoServiceImpl.guardarArchivo(archivo, rutaSolicitud, nombreArchivo);
            mensajeService.actualizarNotificacion(idSolicitud, destinatario, remitente);
            solicitudService.cambiarEstadoSolicitud(idSolicitud, estadoSolicitud, remitente);
            emailService.enviarEmail(destinatarioCorreo,
                    "(FCT'S) Documento pendiente de ser revisado.",
                    "Un profesor ha enviado un documento para firmar. Revisa tu bandeja de entrada.");

            PRG.info("Archivo enviado correctamente.");
        } catch (IOException e) {
            PRG.error("Error al subir el archivo.", "/profesor/solicitudesAprobadaPorJefatura");
        }

        return "redirect: ../";
    }

    // SOLICITUDES DESPUES DE SER FIRMADAS POR DIRECCION Y YA ESTAR FINALIZADAS
    @GetMapping("/solicitudesFinalizadasProfesor")
    public String solicitudesFinalizadasProfesor(ModelMap model, HttpSession session,
            @RequestParam(name = "sort", required = false, defaultValue = "idSolicitud") String sortField,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") String sortDir,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "5") int size) throws DangerException {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        // SI NO ESTAS LOGEADO Y CON EL ROL CORRECTO SALTA EXCEPCION
        if (usuario == null || usuario.getRol() != RolUsuario.PROFESOR) {
            PRG.error("No tienes los privilegios necesarios para realizar esta accion.");
        }
        EstadoSolicitud estadoFinalizado = EstadoSolicitud.SOLICITUD_FINALIZADA;

        List<Solicitud> allSolicitudes = solicitudService.findAllByUsuarioAndEstado(usuario,
                EstadoSolicitud.SOLICITUD_FINALIZADA);

        for (Solicitud solicitud : allSolicitudes) {
            String horarioProcesado = solicitud.getHorario().replace("Segundo horario:",
                    "<br><strong>Segundo horario:</strong><br><br>");
            horarioProcesado = horarioProcesado.replace(".", "<br>");
            solicitud.setHorario(horarioProcesado);

            solicitud.setAlumnos(alumnoService.findBySolicitudIdSolicitud(solicitud.getIdSolicitud()));
        }

        allSolicitudes.sort((s1, s2) -> {
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

        int start = page * size;
        int end = Math.min((start + size), allSolicitudes.size());
        List<Solicitud> solicitudesPaginadas = allSolicitudes.subList(start, end);

        model.addAttribute("estadoFinalizado", estadoFinalizado);
        model.addAttribute("solicitudes", solicitudesPaginadas);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) allSolicitudes.size() / size));
        model.addAttribute("pageSize", size);
        model.addAttribute("view", "profesor/solicitudesFinalizadasProfesor");

        return "_t/frame";
    }

    // VISTA DE TODAS LAS SOLICITUDES INVOLUCRADAS DEL USUARIO DE PROFESOR INICIADO
    @GetMapping("/todasSolicitudesJefaturaProfesor")
    public String todasSolicitudesJefaturaProfesor(ModelMap model, HttpSession session,
            @RequestParam(name = "sort", required = false, defaultValue = "idSolicitud") String sortField,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") String sortDir,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "5") int size) throws DangerException {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        // SI NO ESTAS LOGEADO Y CON EL ROL CORRECTO SALTA EXCEPCION
        if (usuario == null || usuario.getRol() != RolUsuario.PROFESOR) {
            PRG.error("No tienes los privilegios necesarios para realizar esta accion.");
        }
        List<Solicitud> allSolicitudesProfesor = solicitudService.findAllByUsuario(usuario);

        for (Solicitud solicitud : allSolicitudesProfesor) {
            String horarioProcesado = solicitud.getHorario().replace("Segundo horario:",
                    "<br><strong>Segundo horario:</strong><br><br>");
            horarioProcesado = horarioProcesado.replace(".", "<br>");
            solicitud.setHorario(horarioProcesado);

            solicitud.setAlumnos(alumnoService.findBySolicitudIdSolicitud(solicitud.getIdSolicitud()));
        }

        allSolicitudesProfesor.sort((s1, s2) -> {
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

        int start = page * size;
        int end = Math.min((start + size), allSolicitudesProfesor.size());
        List<Solicitud> solicitudesPaginadas = allSolicitudesProfesor.subList(start, end);

        model.put("solicitudes", solicitudesPaginadas);
        model.put("sortField", sortField);
        model.put("sortDir", sortDir);
        model.put("currentPage", page);
        model.put("totalPages", (int) Math.ceil((double) allSolicitudesProfesor.size() / size));
        model.put("pageSize", size);
        model.put("view", "profesor/todasSolicitudesJefaturaProfesor");

        return "_t/frame";
    }

}
