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
    $("#nifAlumno").on('input', function () {
        validarDNI($(this));
    });

    // Validar el formulario antes de enviarlo
    $("#formularioAlumnos").submit(function (event) {
        var alumnoValido = validarDNI($("#nifAlumno"));


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
        ultimoAlumno.find("#nifAlumno").on('input', function () {
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

    // Manejador de clic en el botón "Enviar Datos FCTs"
    $("#boton").click(function (event) {
        const calcularHorasTrabajadas = (inicio1, fin1, inicio2, fin2) => {
            const horaInicio1 = parseInt(inicio1.split(":")[0]);
            const minutoInicio1 = parseInt(inicio1.split(":")[1]);
            const horaFin1 = parseInt(fin1.split(":")[0]);
            const minutoFin1 = parseInt(fin1.split(":")[1]);
            const horaInicio2 = parseInt(inicio2.split(":")[0]);
            const minutoInicio2 = parseInt(inicio2.split(":")[1]);
            const horaFin2 = parseInt(fin2.split(":")[0]);
            const minutoFin2 = parseInt(fin2.split(":")[1]);

            // Calcular las horas del primer tramo
            const horasTramo1 = horaFin1 - horaInicio1 + (minutoFin1 - minutoInicio1) / 60;

            // Calcular las horas del segundo tramo
            const horasTramo2 = horaFin2 - horaInicio2 + (minutoFin2 - minutoInicio2) / 60;

            return horasTramo1 + horasTramo2;
        };

        const horasLunes = calcularHorasTrabajadas(
            document.getElementsByName("lunesInicio1")[0].value,
            document.getElementsByName("lunesFin1")[0].value,
            document.getElementsByName("lunesInicio2")[0].value,
            document.getElementsByName("lunesFin2")[0].value
        );

        const horasMartes = calcularHorasTrabajadas(
            document.getElementsByName("martesInicio1")[0].value,
            document.getElementsByName("martesFin1")[0].value,
            document.getElementsByName("martesInicio2")[0].value,
            document.getElementsByName("martesFin2")[0].value
        );

        const horasMiercoles = calcularHorasTrabajadas(
            document.getElementsByName("miercolesInicio1")[0].value,
            document.getElementsByName("miercolesFin1")[0].value,
            document.getElementsByName("miercolesInicio2")[0].value,
            document.getElementsByName("miercolesFin2")[0].value
        );

        const horasJueves = calcularHorasTrabajadas(
            document.getElementsByName("juevesInicio1")[0].value,
            document.getElementsByName("juevesFin1")[0].value,
            document.getElementsByName("juevesInicio2")[0].value,
            document.getElementsByName("juevesFin2")[0].value
        );

        const horasViernes = calcularHorasTrabajadas(
            document.getElementsByName("viernesInicio1")[0].value,
            document.getElementsByName("viernesFin1")[0].value,
            document.getElementsByName("viernesInicio2")[0].value,
            document.getElementsByName("viernesFin2")[0].value
        );

        const horasDia = document.getElementById("horasDia").value;
        const horasSemanalesSupuestas = horasDia * 5;
        const horasTotalesSemanales = horasLunes + horasMartes + horasMiercoles + horasJueves + horasViernes;

        if (horasTotalesSemanales <= horasSemanalesSupuestas) {
            alert("Las horas están bien");
            return true;
        } else {
            alert("Está explotado");
            event.preventDefault(); // Cancelar el envío del formulario
            return false;
        }
    });

    // Función para copiar las horas del lunes a todos los días de la semana
    $("#copiarHoras").click(function () {
        // Obtener las horas del lunes
        const horasLunesInicio1 = document.getElementsByName("lunesInicio1")[0].value;
        const horasLunesFin1 = document.getElementsByName("lunesFin1")[0].value;
        const horasLunesInicio2 = document.getElementsByName("lunesInicio2")[0].value;
        const horasLunesFin2 = document.getElementsByName("lunesFin2")[0].value;

        // Copiar las horas del lunes a los demás días de la semana
        const diasSemana = ["martes", "miercoles", "jueves", "viernes"];
        diasSemana.forEach(function (dia) {
            document.getElementsByName(dia + "Inicio1")[0].value = horasLunesInicio1;
            document.getElementsByName(dia + "Fin1")[0].value = horasLunesFin1;
            document.getElementsByName(dia + "Inicio2")[0].value = horasLunesInicio2;
            document.getElementsByName(dia + "Fin2")[0].value = horasLunesFin2;
        });

        // Opcional: mostrar un mensaje o realizar alguna acción adicional
        alert("Horas del lunes copiadas a todos los días de la semana.");
    });

    const opcionPDF = document.getElementById("opcionPDF");
    const opcionObservaciones = document.getElementById("opcionObservaciones");
    const campoPDF = document.getElementById("campoPDF");
    const campoObservaciones = document.getElementById("campoObservaciones");

    // Función para habilitar el campo PDF y deshabilitar Observaciones
    function habilitarPDF() {
        campoPDF.style.display = "block";
        campoObservaciones.style.display = "none";
    }

    // Función para habilitar el campo Observaciones y deshabilitar PDF
    function habilitarObservaciones() {
        campoPDF.style.display = "none";
        campoObservaciones.style.display = "block";
    }

    // Event listener para detectar el cambio en la selección de opción
    opcionPDF.addEventListener("change", function() {
        if (opcionPDF.checked) {
            habilitarPDF();
        }
    });

    opcionObservaciones.addEventListener("change", function() {
        if (opcionObservaciones.checked) {
            habilitarObservaciones();
        }
    });

    //------------------Selección de uno u otro endpoints--------------------
    document.getElementById('formularioAlumnos').addEventListener('submit', function(event) {
        var opcionSeleccionada = document.querySelector('input[name="opcion"]:checked').value;
        if (opcionSeleccionada === 'pdf') {
            this.action = '/enviarDatos/corregirDatosJefaturaArchivo';
            this.enctype = 'multipart/form-data'; // Añadir el tipo de codificación para archivos
        } else if (opcionSeleccionada === 'observaciones') {
            this.action = '/enviarDatos/corregirDatosJefaturaObservaciones';
        }
    });
    //------------Corregir Datos Dirección------------------------------------ 
    //------------------Selección de uno u otro endpoints---------------------
    document.getElementById('solicitudFCT').addEventListener('submit', function(event) {
        var opcionSeleccionada = document.querySelector('input[name="opcion"]:checked').value;
        if (opcionSeleccionada === 'pdf') {
            this.action = '/enviarDatos/solicitudAceptadaDireccion';
            this.enctype = 'multipart/form-data'; 
        } else if (opcionSeleccionada === 'observaciones') {
            this.action = '/enviarDatos/corregirDatosJefaturaObservaciones';
        }
    });

    //-------------------------------ARCHIVO CARGA Y DESCARGA-----------------
            
});