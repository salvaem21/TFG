// Utilizar jQuery en modo noConflict para evitar conflictos con otras bibliotecas
var $ = jQuery.noConflict();

$(document).ready(function () {
    // Función para calcular la letra del DNI
    function calcularLetraDNI(numero) {
        var letras = 'TRWAGMYFPDXBNJZSQVHLCKET';
        return letras.charAt(numero % 23);
    }

    // Validar el DNI introducido
    function validarDNI(dniInput) {
        var dni = dniInput.val();
        // Expresión regular para validar el formato del DNI
        var dniRegex = /^\d{8}[a-zA-Z]$/;
        if (!dniRegex.test(dni)) {
            dniInput.css('border-color', 'red');
            return false;
        }

        var numerosDNI = dni.substring(0, 8);
        var letraDNI = dni.substring(8).toUpperCase();

        // Calcular la letra correcta del DNI
        var letraCorrecta = calcularLetraDNI(parseInt(numerosDNI, 10));

        // Comprobar si la letra introducida coincide con la letra calculada
        if (letraDNI !== letraCorrecta) {
            dniInput.css('border-color', 'red');
            return false;
        }

        // Si el DNI es válido, eliminar el resaltado rojo
        dniInput.css('border-color', '');

        return true;
    }

    // Validar el DNI cuando se modifica el campo de entrada
    $("#nifTutorAlumno, #nifAlumno").on('input', function () {
        validarDNI($(this));
    });

    // Validar el DNI cuando se envíe el formulario
    $("#formularioAlumnos").submit(function (event) {
        var tutorValido = validarDNI($("#nifTutorAlumno"));
        var alumnoValido = validarDNI($("#nifAlumno"));

        // Si tanto el NIF del Tutor como del Alumno son inválidos
        if (!tutorValido && !alumnoValido) {
            alert("El NIF del Tutor y del Alumno introducidos no son válidos.");
            event.preventDefault(); // Evitar que se envíe el formulario si ambos NIF no son válidos
            return;
        }
        // Si solo el NIF del Tutor es inválido
        if (!tutorValido) {
            alert("El NIF del Tutor introducido no es válido.");
            event.preventDefault(); // Evitar que se envíe el formulario si el NIF del Tutor no es válido
            return;
        }
        // Si solo el NIF del Alumno es inválido
        if (!alumnoValido) {
            alert("El NIF del Alumno introducido no es válido.");
            event.preventDefault(); // Evitar que se envíe el formulario si el NIF del Alumno no es válido
            return;
        }
        // Validar el NIF de los alumnos extra
        var alumnosExtraValidos = true;
        $(".datos-alumno").not(":first").find("#nifAlumno").each(function () {
            if (!validarDNI($(this))) {
                alumnosExtraValidos = false;
                return false; // Romper el bucle si se encuentra un NIF inválido
            }
        });
        if (!alumnosExtraValidos) {
            alert("El NIF de uno o más alumnos introducidos no es válido.");
            event.preventDefault(); // Evitar que se envíe el formulario si uno o más NIF de alumnos son inválidos
        }
    });

    // Manejador de clic en el botón para agregar otro alumno
    $("#agregarAlumno").click(function () {
        // Clonar el último conjunto de campos de datos del alumno y agregarlo al formulario
        var ultimoAlumno = $(".datos-alumno").last().clone();
        // Limpiar los valores de los campos clonados para evitar confusiones
        ultimoAlumno.find("input[type=text], input[type=date]").val("");
        // Aplicar la validación del DNI a los nuevos campos de DNI
        ultimoAlumno.find("#nifTutorAlumno, #nifAlumno").on('input', function () {
            validarDNI($(this));
        });
        // Agregar un botón "Eliminar alumno" solo si no existe ya uno en este conjunto de campos de datos del alumno
        if (!ultimoAlumno.find(".eliminar-alumno").length) {
            var botonEliminar = $("<button class='btn btn-danger eliminar-alumno'>Eliminar alumno</button>");
            botonEliminar.click(function () {
                // Eliminar el conjunto de campos de datos del alumno correspondiente al hacer clic en el botón "Eliminar alumno"
                $(this).closest(".datos-alumno").remove();
            });
            ultimoAlumno.append(botonEliminar);
        }
        $("#contenedorAlumnos").append(ultimoAlumno);
    });

    // Manejador de clic en el botón "Eliminar alumno" existente
    $(document).on("click", ".eliminar-alumno", function () {
        // Eliminar el conjunto de campos de datos del alumno correspondiente al hacer clic en el botón "Eliminar alumno"
        $(this).closest(".datos-alumno").remove();
    });

    // Manejador de clic en el botón "Copiar horas del lunes"
    $("#copiarHoras").click(function () {
        // Obtener las horas introducidas para el lunes
        const horasLunesInicio1 = document.getElementsByName("lunesInicio1")[0].value;
        const horasLunesFin1 = document.getElementsByName("lunesFin1")[0].value;
        const horasLunesInicio2 = document.getElementsByName("lunesInicio2")[0].value;
        const horasLunesFin2 = document.getElementsByName("lunesFin2")[0].value;

        // Copiar las horas del lunes a los otros días de la semana
        document.getElementsByName("martesInicio1")[0].value = horasLunesInicio1;
        document.getElementsByName("martesFin1")[0].value = horasLunesFin1;
        document.getElementsByName("martesInicio2")[0].value = horasLunesInicio2;
        document.getElementsByName("martesFin2")[0].value = horasLunesFin2;

        document.getElementsByName("miercolesInicio1")[0].value = horasLunesInicio1;
        document.getElementsByName("miercolesFin1")[0].value = horasLunesFin1;
        document.getElementsByName("miercolesInicio2")[0].value = horasLunesInicio2;
        document.getElementsByName("miercolesFin2")[0].value = horasLunesFin2;

        document.getElementsByName("juevesInicio1")[0].value = horasLunesInicio1;
        document.getElementsByName("juevesFin1")[0].value = horasLunesFin1;
        document.getElementsByName("juevesInicio2")[0].value = horasLunesInicio2;
        document.getElementsByName("juevesFin2")[0].value = horasLunesFin2;

        document.getElementsByName("viernesInicio1")[0].value = horasLunesInicio1;
        document.getElementsByName("viernesFin1")[0].value = horasLunesFin1;
        document.getElementsByName("viernesInicio2")[0].value = horasLunesInicio2;
        document.getElementsByName("viernesFin2")[0].value = horasLunesFin2;

        // Opcional: mostrar un mensaje o realizar alguna acción adicional
        alert("Horas del lunes copiadas a todos los días de la semana.");
    });
});
