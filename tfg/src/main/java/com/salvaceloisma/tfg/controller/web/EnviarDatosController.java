package com.salvaceloisma.tfg.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.salvaceloisma.tfg.domain.Usuario;
import com.salvaceloisma.tfg.enumerados.RolUsuario;
import com.salvaceloisma.tfg.exception.DangerException;
import com.salvaceloisma.tfg.exception.InfoException;
import com.salvaceloisma.tfg.helper.PRG;
import com.salvaceloisma.tfg.service.EmailService;
import com.salvaceloisma.tfg.service.InicioSesionService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.time.LocalTime;
import java.util.List;

@RequestMapping("/enviarDatos")
@Controller
public class EnviarDatosController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private InicioSesionService inicioSesionService;

    @GetMapping("/enviarDatosAJefatura")
    public String crearDocumento(ModelMap m) {
        List<Usuario> usuariosJefatura = inicioSesionService.obtenerUsuariosPorRol(RolUsuario.JEFATURA);
        m.put("usuariosJefatura", usuariosJefatura);
        m.put("view", "profesor/enviarDatosAlumnos");
        return "_t/frame";
    }

    @PostMapping("/enviarDatosAJefatura")
    public String crearDocumento(HttpServletResponse response, HttpSession session, @RequestParam String numeroConvenio,
            @RequestParam String nombreEmpresa, @RequestParam String tutorEmpresa,
            @RequestParam String cifEmpresa, @RequestParam String direccionPracticas,
            @RequestParam String localidadPracticas, @RequestParam String codigoPostalPracticas,
            @RequestParam String apellidosAlumno, @RequestParam String nombreAlumno, @RequestParam String nifAlumno,
            @RequestParam String cicloFormativoAlumno,
            @RequestParam String tutorAlumno, @RequestParam String nifTutorAlumno,
            @RequestParam String fechaDeNacimientoAlumno, @RequestParam String horasTotales,
            @RequestParam String fechaInicio, @RequestParam String fechaFin, @RequestParam LocalTime lunesInicio1,
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
            @RequestParam LocalTime viernesFin2, @RequestParam String horasDia,
            @RequestParam("rolUsuario") Long usuarioEnvio, ModelMap m)
            throws DangerException, InfoException {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String nombre = usuario.getNombre();

        String correo = inicioSesionService.findById(usuarioEnvio).getCorreo();

        String datos = "Numero Convenio: " + numeroConvenio;
        datos += "\nNombre empresa: " + nombreEmpresa;
        datos += "\nTutor empresa: " + tutorEmpresa;
        datos += "\nCIF empresa: " + cifEmpresa;
        datos += "\nDirección prácticas: " + direccionPracticas;
        datos += "\nLocalidad prácticas: " + localidadPracticas;
        datos += "\nCódigo postal prácticas: " + codigoPostalPracticas;
        datos += "\nApellidos alumno: " + apellidosAlumno;
        datos += "\nNombre alumno: " + nombreAlumno;
        datos += "\nNIF alumno: " + nifAlumno;
        datos += "\nCiclo formativo alumno: " + cicloFormativoAlumno;
        datos += "\nTutor alumno: " + tutorAlumno;
        datos += "\nNIF tutor alumno: " + nifTutorAlumno;
        datos += "\nFecha de nacimiento alumno: " + fechaDeNacimientoAlumno;
        datos += "\nHoras totales: " + horasTotales;
        datos += "\nFecha de inicio: " + fechaInicio;
        datos += "\nFecha de fin: " + fechaFin;
        datos += "\nLunes: " + lunesInicio1 + "-" + lunesFin1 + "\t" + lunesInicio2 + "-" + lunesFin2;
        datos += "\nMartes: " + martesInicio1 + "-" + martesFin1 + "\t" + martesInicio2 + "-" + martesFin2;
        datos += "\nMiercoles: " + miercolesInicio1 + "-" + miercolesFin1 + "\t" + miercolesInicio2 + "-"
                + miercolesFin2;
        datos += "\nJueves: " + juevesInicio1 + "-" + juevesFin1 + "\t" + juevesInicio2 + "-" + juevesFin2;
        datos += "\nViernes: " + viernesInicio1 + "-" + viernesFin1 + "\t" + viernesInicio2 + "-" + viernesFin2;
        datos += "\nHoras por día: " + horasDia;

        try {
            emailService.enviarEmail(correo, "Envio de datos de " + nombre, "Estos son los datos del alumno:\n" + datos);
        } catch (Exception e) {
            PRG.error("El correo no puedo enviarse correctamente.");
        }
        
        return "redirect:../";
    }
}
