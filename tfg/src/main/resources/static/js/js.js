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

    function validarNombreTutor(nombreInput) {
        var nombre = nombreInput.val();
        // Expresión regular para validar el nombre (letras, espacios en blanco y algunos signos)
        var nombreRegex = /^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ \-'']+$/;

        if (!nombreRegex.test(nombre)) {
            nombreInput.css('border-color', 'red');
            return false;
        }

        // Si el nombre es válido, eliminar el resaltado rojo
        nombreInput.css('border-color', '');
        return true;
    }

    // Evento para validar el nombre cuando se modifica el campo de entrada
    $("#tutorAlumno").on('blur', function () {
        validarNombreTutor($(this));
    });

    function validarCicloFormativo(cicloInput) {
        var valorSeleccionado = cicloInput.val();
    
        // Verificar que la selección no sea la opción predeterminada
        if (valorSeleccionado === "") {
            cicloInput.css('border-color', 'red');
            return false;
        }
    
        // Si se selecciona una opción válida, eliminar el resaltado rojo
        cicloInput.css('border-color', '');
        return true;
    }
    
    // Evento para validar el ciclo formativo cuando se cambia la selección del campo desplegable
    $("#cicloFormativoAlumno").on('change', function () {
        validarCicloFormativo($(this));
    });

    function validarNumeroConvenio(convenioInput) {
        var numeroConvenio = convenioInput.val();
        // Expresión regular para validar que el número de convenio tenga exactamente 14 dígitos
        var convenioRegex = /^\d{14}$/;

        if (!convenioRegex.test(numeroConvenio)) {
            convenioInput.css('border-color', 'red');
            return false;
        }

        // Si el número de convenio es válido, eliminar el resaltado rojo
        convenioInput.css('border-color', '');
        return true;
    }

    // Evento para validar el número de convenio cuando se modifica el campo de entrada
    $("#numeroConvenio").on('blur', function () {
        validarNumeroConvenio($(this));
    });

    function validarNombreEmpresa(nombreInput) {
        var nombreEmpresa = nombreInput.val();
        // Expresión regular para validar el nombre (letras, números, espacios y signos de puntuación)
        var nombreRegex = /^[a-zA-Z0-9áéíóúÁÉÍÓÚüÜñÑ \s.,'-]+$/;

        if (!nombreRegex.test(nombreEmpresa)) {
            nombreInput.css('border-color', 'red');
            return false;
        }

        // Si el nombre de la empresa es válido, eliminar el resaltado rojo
        nombreInput.css('border-color', '');
        return true;
    }

    // Evento para validar el nombre de la empresa cuando se modifica el campo de entrada
    $("#nombreEmpresa").on('blur', function () {
        validarNombreEmpresa($(this));
    });

    function validarTutorEmpresa(nombreInput) {
        var nombre = nombreInput.val();
        // Expresión regular para validar el nombre (letras, espacios en blanco y guiones)
        var nombreRegex = /^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ \-'']+$/;

        if (!nombreRegex.test(nombre)) {
            nombreInput.css('border-color', 'red');
            return false;
        }

        // Si el nombre es válido, eliminar el resaltado rojo
        nombreInput.css('border-color', '');
        return true;
    }

    // Evento para validar el nombre cuando se modifica el campo de entrada
    $("#tutorEmpresa").on('blur', function () {
        validarTutorEmpresa($(this));
    });

    function validarCIFEmpresa(cifInput) {
        var cifEmpresa = cifInput.val();
        // Expresión regular para validar que el CIF tenga 1 número seguido de 8 letras
        var cifRegex = /^[a-zA-Z]\d{8}$/;

        if (!cifRegex.test(cifEmpresa)) {
            cifInput.css('border-color', 'red');
            return false;
        }

        // Si el CIF de la empresa es válido, eliminar el resaltado rojo
        cifInput.css('border-color', '');
        return true;
    }

    // Evento para validar el CIF de la empresa cuando se modifica el campo de entrada
    $("#cifEmpresa").on('blur', function () {
        validarCIFEmpresa($(this));
    });

    function validarDireccionPracticas(direccionInput) {
        var direccion = direccionInput.val();
        var direccionRegex = /^[0-9a-zA-Zº/, ]+$/;
        if (!direccionRegex.test(direccion)) {
            direccionInput.css('border-color', 'red');
            return false;
        }
        direccionInput.css('border-color', '');
        return true;
    }

    $("#direccionPracticas").on('blur', function () {
        validarDireccionPracticas($(this));
    });

    // Validación de la Localidad de las Prácticas
    function validarLocalidadPracticas(localidadInput) {
        var localidad = localidadInput.val();
        var localidadRegex = /^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ \-]+$/;
        if (!localidadRegex.test(localidad)) {
            localidadInput.css('border-color', 'red');
            return false;
        }
        localidadInput.css('border-color', '');
        return true;
    }

    $("#localidadPracticas").on('blur', function () {
        validarLocalidadPracticas($(this));
    });

    // Validación del Código Postal de las Prácticas
    function validarCodigoPostalPracticas(codigoInput) {
        var codigoPostal = codigoInput.val();
        var codigoRegex = /^\d{5}$/;
        if (!codigoRegex.test(codigoPostal)) {
            codigoInput.css('border-color', 'red');
            return false;
        }
        codigoInput.css('border-color', '');
        return true;
    }

    $("#codigoPostalPracticas").on('blur', function () {
        validarCodigoPostalPracticas($(this));
    });

    function validarNombreApellidos(input) {
        var texto = input.val();
        var regex = /^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ \-'']+$/;

        if (!regex.test(texto)) {
            input.css('border-color', 'red');
            return false;
        }
        input.css('border-color', ''); // Eliminar el resaltado si es válido
        return true;
    }

    // Evento para validar el nombre y apellidos cuando se modifica el campo de entrada
    $("#apellidosAlumno, #nombreAlumno").on('blur', function () {
        validarNombreApellidos($(this));
    });

    // Asegurarse de aplicar la validación a campos dinámicos
    $(document).on('blur', '.datos-alumno #apellidosAlumno, .datos-alumno #nombreAlumno', function () {
        validarNombreApellidos($(this));
    });

    //Validacion horas totales entre 240(practicas Fp basica) y 440(practias maximo 5 titulos LOGSE)
    function validarHorasTotales(horasInput) {
        var horas = parseInt(horasInput.val(), 10);
        if (isNaN(horas) || horas < 240 || horas > 440) {
            horasInput.css('border-color', 'red');
            return false;
        }
        horasInput.css('border-color', ''); // Limpiar el color del borde si es válido
        return true;
    }

    // Evento para validar las horas totales cuando se modifica el campo
    $("#horasTotales").on('input', function () {
        validarHorasTotales($(this));
    });

    function validarFechaInicio(fechaInicioInput) {
        var fechaInicio = new Date(fechaInicioInput.val());
        var hoy = new Date();
        hoy.setHours(0, 0, 0, 0); // Asegura comparar solo la fecha sin tiempo

        if (fechaInicio <= hoy) {
            fechaInicioInput.css('border-color', 'red');
            return false;
        }
        fechaInicioInput.css('border-color', '');
        return true;
    }

    // Función para validar la fecha de fin
    function validarFechaFin(fechaInicioInput, fechaFinInput) {
        var fechaInicio = new Date(fechaInicioInput.val());
        var fechaFin = new Date(fechaFinInput.val());

        if (fechaFin <= fechaInicio) {
            fechaFinInput.css('border-color', 'red');
            return false;
        }
        fechaFinInput.css('border-color', '');
        return true;
    }

    // Evento para validar las fechas cuando se modifica cualquiera de los campos
    $("#fechaInicio, #fechaFin").on('change', function () {
        validarFechaInicio($('#fechaInicio'));
        validarFechaFin($('#fechaInicio'), $('#fechaFin'));
    });

    
    // Validar el formulario antes de enviarlo
    $("#formularioAlumnos").submit(function (event) {
        var alumnoValido = validarDNI($("#nifAlumno"));
        var nombreTutorValido = validarNombreTutor($("#tutorAlumno"));
        var cicloValido = validarCicloFormativo($("#cicloFormativoAlumno"));        
        var numeroConvenioValido = validarNumeroConvenio($("#numeroConvenio"));
        var nombreEmpresaValido = validarNombreEmpresa($("#nombreEmpresa"));
        var tutorEmpresaValido = validarTutorEmpresa($("#tutorEmpresa"));
        var cifEmpresaValido = validarCIFEmpresa($("#cifEmpresa"));
        var direccionValida = validarDireccionPracticas($("#direccionPracticas"));
        var localidadValida = validarLocalidadPracticas($("#localidadPracticas"));
        var codigoPostalValido = validarCodigoPostalPracticas($("#codigoPostalPracticas"));
        var validarNombreAlumno=validarNombreApellidos($("#nombreAlumno"));
        var validarApellidoAlumno=validarNombreApellidos($("#apellidosAlumno"));
        var horasValidas = validarHorasTotales($("#horasTotales"));
        var fechaInicioValida = validarFechaInicio($("#fechaInicio"));
        var fechaFinValida = validarFechaFin($("#fechaInicio"), $("#fechaFin"));

        // Si solo el NIF del Alumno es inválido
        if (!alumnoValido) {
            alert("El NIF del Alumno introducido no es válido.");
            event.preventDefault(); // Evitar que se envíe el formulario si el NIF del Alumno no es válido
            return;
        }
        if (!nombreTutorValido) {
            alert("El nombre del tutor introducido no es válido.");
            event.preventDefault(); 
            return;
        }
        if (!cicloValido) {
            alert("Seleccione un ciclo formativo.");
            event.preventDefault(); 
            return;
        }
        if (!numeroConvenioValido) {
            alert("El numero de convenio introducido no es válido.");
            event.preventDefault(); 
            return;
        }
        if (!nombreEmpresaValido) {
            alert("El nombre de empresa introducido no es válido.");
            event.preventDefault(); 
            return;
        }
        if (!tutorEmpresaValido) {
            alert("El nombre del tutor de la empresa introducido no es válido.");
            event.preventDefault(); 
            return;
        }
        if (!cifEmpresaValido) {
            alert("El cif de la empresa introducido no es válido.");
            event.preventDefault(); 
            return;
        }
        if (!direccionValida) {
            alert("La direccion de la empresa introducido no es válido.");
            event.preventDefault(); 
            return;
        }
        if (!localidadValida) {
            alert("La localidad de la empresa introducido no es válido.");
            event.preventDefault(); 
            return;
        }
        if (!codigoPostalValido) {
            alert("El codigo postal de la empresa introducido no es válido.");
            event.preventDefault(); 
            return;
        }
        if (!validarNombreAlumno) {
            alert("El nombre del Alumno introducido no es válido.");
            event.preventDefault(); 
            return;
        }
        if (!validarApellidoAlumno) {
            alert("El apellido del alumno introducido no es válido.");
            event.preventDefault(); 
            return;
        }
        if (!horasValidas) {
            alert("Las horas totales deben de ser entre 240 y 440.");
            event.preventDefault();
            return;
        }
        if (!fechaInicioValida) {
            alert("la fecha de inicio tiene que ser posterior a hoy.");
            event.preventDefault(); 
            return;
        }
        if (!fechaFinValida) {
            alert("La fecha de fin tiene que ser posterior a la fecha de inicio.");
            event.preventDefault();
            return;
        }

        //ALUMNOS EXTRAS
        // Validar el NIF de los alumnos extra
        var alumnosExtraValidos = true;
        $(".datos-alumno").not(":first").find("#nifAlumno").each(function () {
            if (!validarDNI($(this))) {
                alumnosExtraValidos = false;
                return false; // Romper el bucle si se encuentra un NIF inválido
            }
            // Validar Apellidos del alumno
        if (!validarNombreApellidos($(this).find("#apellidosAlumno"))) {
            alert("Los apellidos de uno o más alumnos introducidos no son válidos.");
            alumnosExtraValidos = false;
            event.preventDefault();
            return false; // Romper el bucle si se encuentra un apellido inválido
        }

        // Validar Nombre del alumno
        if (!validarNombreApellidos($(this).find("#nombreAlumno"))) {
            alert("El nombre de uno o más alumnos introducidos no es válido.");
            alumnosExtraValidos = false;
            event.preventDefault();
            return false; // Romper el bucle si se encuentra un nombre inválido
        }
        });
        if (!alumnosExtraValidos) {
            alert("El nombre,apellido o NIF del alumno extra es invalido.");
            event.preventDefault(); // Evitar que se envíe el formulario si uno o más campos de alumnos son inválidos
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
    $('#solicitudFCT').submit(function(event) {
        event.preventDefault(); // Evitar el envío predeterminado del formulario
        var opcionSeleccionada = $('input[name="opcion"]:checked').val();
        if (opcionSeleccionada === 'pdf') {
            this.action = '/enviarDatos/solicitudAceptadaDireccion';
            this.enctype = 'multipart/form-data'; 
        } else if (opcionSeleccionada === 'observaciones') {
            this.action = '/enviarDatos/corregirDatosDireccionObservaciones';
        }
        this.submit(); // Envía el formulario después de actualizar el action y enctype
    });

    //-------------------------------ARCHIVO CARGA Y DESCARGA-----------------
    
            
});