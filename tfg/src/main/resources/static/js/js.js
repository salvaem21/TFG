var $ = jQuery.noConflict();

$(document).ready(function () {

    function updatePageSize() {
        const pageSize = $('#pageSizeSelect').val();
        const url = new URL(window.location.href);
        url.searchParams.set('size', pageSize);
        url.searchParams.set('page', 0);
        window.location.href = url.toString();
    }

    $('#pageSizeSelect').on('change', function () {
        updatePageSize();
    });

    $(".search-tables").on("keyup", function () {
        var value = $(this).val().toLowerCase();
        var attribute = $(".search-attribute").val();
        $("#solicitudes-table .solicitud-row").filter(function () {
            if (attribute === "all") {
                $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1);
            } else {
                $(this).toggle($(this).find("[data-attribute='" + attribute + "']").text().toLowerCase().indexOf(value) > -1);
            }
        });
    });

    function sortTable(columnIndex, order) {
        var rows = $('#solicitudes-table .solicitud-row').get();
        rows.sort(function (a, b) {
            var A = $(a).children('td').eq(columnIndex).text().toUpperCase();
            var B = $(b).children('td').eq(columnIndex).text().toUpperCase();

            if (A < B) {
                return order === 'asc' ? -1 : 1;
            }
            if (A > B) {
                return order === 'asc' ? 1 : -1;
            }
            return 0;
        });
        $.each(rows, function (index, row) {
            $('#solicitudes-table tbody').append(row);
        });
    }

    $('th a').click(function (e) {
        e.preventDefault();
        var columnIndex = $(this).parent().index();
        var order = $(this).find('span').text() === '▲' ? 'desc' : 'asc';
        sortTable(columnIndex, order);
        $(this).find('span').text(order === 'asc' ? '▲' : '▼');
    });

    function calcularLetraDNI(numero) {
        var letras = 'TRWAGMYFPDXBNJZSQVHLCKET';
        return letras.charAt(numero % 23);
    }

    function validarDNI(dniInput) {
        var dni = dniInput.val();
        var dniRegex = /^\d{8}[a-zA-Z]$/;
        if (!dniRegex.test(dni)) {
            dniInput.css('border-color', 'red');
            return false;
        }

        var numerosDNI = dni.substring(0, 8);
        var letraDNI = dni.substring(8).toUpperCase();

        var letraCorrecta = calcularLetraDNI(parseInt(numerosDNI, 10));

        if (letraDNI !== letraCorrecta) {
            dniInput.css('border-color', 'red');
            return false;
        }

        dniInput.css('border-color', '');

        return true;
    }

    $("#nifAlumno").on('input', function () {
        validarDNI($(this));
    });

    function validarNombreTutor(nombreInput) {
        var nombre = nombreInput.val();
        var nombreRegex = /^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ \-'']+$/;

        if (!nombreRegex.test(nombre)) {
            nombreInput.css('border-color', 'red');
            return false;
        }

        nombreInput.css('border-color', '');
        return true;
    }

    $("#tutorAlumno").on('input', function () {
        validarNombreTutor($(this));
    });

    function validarCicloFormativo(cicloInput) {
        var valorSeleccionado = cicloInput.val();

        if (valorSeleccionado === "") {
            cicloInput.css('border-color', 'red');
            return false;
        }

        cicloInput.css('border-color', '');
        return true;
    }

    $("#cicloFormativoAlumno").on('input', function () {
        validarCicloFormativo($(this));
    });


    function validarNombreEmpresa(nombreInput) {
        var nombreEmpresa = nombreInput.val();
        var nombreRegex = /^[a-zA-Z0-9áéíóúÁÉÍÓÚüÜñÑ \s.,'-]+$/;

        if (!nombreRegex.test(nombreEmpresa)) {
            nombreInput.css('border-color', 'red');
            return false;
        }

        nombreInput.css('border-color', '');
        return true;
    }

    $("#nombreEmpresa").on('input', function () {
        validarNombreEmpresa($(this));
    });

    function validarTutorEmpresa(nombreInput) {
        var nombre = nombreInput.val();
        var nombreRegex = /^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ \-'']+$/;

        if (!nombreRegex.test(nombre)) {
            nombreInput.css('border-color', 'red');
            return false;
        }

        nombreInput.css('border-color', '');
        return true;
    }

    $("#tutorEmpresa").on('input', function () {
        validarTutorEmpresa($(this));
    });

    function validarCIFEmpresa(cifInput) {
        var cifEmpresa = cifInput.val();
        var cifRegex = /^[a-zA-Z]\d{8}$/;

        if (!cifRegex.test(cifEmpresa)) {
            cifInput.css('border-color', 'red');
            return false;
        }

        cifInput.css('border-color', '');
        return true;
    }

    $("#cifEmpresa").on('input', function () {
        validarCIFEmpresa($(this));
    });

    function validarDireccionPracticas(direccionInput) {
        var direccion = direccionInput.val();
        var direccionRegex = /^[0-9a-zA-Zº/,áéíóúÁÉÍÓÚüÜñÑ ]+$/;
        if (!direccionRegex.test(direccion)) {
            direccionInput.css('border-color', 'red');
            return false;
        }
        direccionInput.css('border-color', '');
        return true;
    }

    $("#direccionPracticas").on('input', function () {
        validarDireccionPracticas($(this));
    });

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

    $("#localidadPracticas").on('input', function () {
        validarLocalidadPracticas($(this));
    });

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

    $("#codigoPostalPracticas").on('input', function () {
        validarCodigoPostalPracticas($(this));
    });

    function validarNombreApellidos(input) {
        var texto = input.val();
        var regex = /^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ \-'']+$/;

        if (!regex.test(texto)) {
            input.css('border-color', 'red');
            return false;
        }
        input.css('border-color', '');
        return true;
    }

    $("#apellidosAlumno, #nombreAlumno").on('input', function () {
        validarNombreApellidos($(this));
    });

    $(document).on('input', '.datos-alumno #apellidosAlumno, .datos-alumno #nombreAlumno', function () {
        validarNombreApellidos($(this));
    });

    function validarHorasTotales(horasInput) {
        var horas = parseInt(horasInput.val(), 10);
        if (isNaN(horas) || horas < 240 || horas > 440) {
            horasInput.css('border-color', 'red');
            return false;
        }
        horasInput.css('border-color', '');
        return true;
    }

    $("#horasTotales").on('input', function () {
        validarHorasTotales($(this));
    });

    function validarFechaInicio(fechaInicioInput) {
        fechaInicioInput.css('border-color', '');
        return true;
    }

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

    $("#fechaInicio, #fechaFin").on('change', function () {
        validarFechaInicio($('#fechaInicio'));
        validarFechaFin($('#fechaInicio'), $('#fechaFin'));
    });


    $("#formularioAlumnos").submit(function (event) {
        var alumnoValido = validarDNI($("#nifAlumno"));
        var nombreTutorValido = validarNombreTutor($("#tutorAlumno"));
        var cicloValido = validarCicloFormativo($("#cicloFormativoAlumno"));
        var nombreEmpresaValido = validarNombreEmpresa($("#nombreEmpresa"));
        var tutorEmpresaValido = validarTutorEmpresa($("#tutorEmpresa"));
        var cifEmpresaValido = validarCIFEmpresa($("#cifEmpresa"));
        var direccionValida = validarDireccionPracticas($("#direccionPracticas"));
        var localidadValida = validarLocalidadPracticas($("#localidadPracticas"));
        var codigoPostalValido = validarCodigoPostalPracticas($("#codigoPostalPracticas"));
        var validarNombreAlumno = validarNombreApellidos($("#nombreAlumno"));
        var validarApellidoAlumno = validarNombreApellidos($("#apellidosAlumno"));
        var horasValidas = validarHorasTotales($("#horasTotales"));
        var fechaInicioValida = validarFechaInicio($("#fechaInicio"));
        var fechaFinValida = validarFechaFin($("#fechaInicio"), $("#fechaFin"));

        if (!alumnoValido) {
            alert("El NIF del Alumno introducido no es válido.");
            event.preventDefault();
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

        var alumnosExtraValidos = true;
        $(".datos-alumno").not(":first").find("#nifAlumno").each(function () {
            if (!validarDNI($(this))) {
                alumnosExtraValidos = false;
                return false;
            }
            if (!validarNombreApellidos($(this).find("#apellidosAlumno"))) {
                alert("Los apellidos de uno o más alumnos introducidos no son válidos.");
                alumnosExtraValidos = false;
                event.preventDefault();
                return false;
            }

            if (!validarNombreApellidos($(this).find("#nombreAlumno"))) {
                alert("El nombre de uno o más alumnos introducidos no es válido.");
                alumnosExtraValidos = false;
                event.preventDefault();
                return false;
            }
        });
        if (!alumnosExtraValidos) {
            alert("El nombre,apellido o NIF del alumno extra es invalido.");
            event.preventDefault();
        }
        $("#boton").prop("disabled", true);
    });

    $("#formularioCreador").submit(function (event) {
        var validarNombreAlumno = validarNombreApellidos($("#nombreAlumno"));
        var validarApellidoAlumno = validarNombreApellidos($("#apellidosAlumno"));

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
        $("#boton").prop("disabled", true);
    });

    $("#formularioModAlumnos").submit(function (event) {
        var alumnoValido = validarDNI($("#nifAlumno"));
        var validarNombreAlumno = validarNombreApellidos($("#nombreAlumno"));
        var validarApellidoAlumno = validarNombreApellidos($("#apellidosAlumno"));

        if (!alumnoValido) {
            alert("El NIF del Alumno introducido no es válido.");
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
        $("#boton").prop("disabled", true);
    });

    $("#formularioModUsuarios").submit(function (event) {
        var validarNombreAlumno = validarNombreApellidos($("#nombreAlumno"));
        var validarApellidoAlumno = validarNombreApellidos($("#apellidosAlumno"));

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
        $("#boton").prop("disabled", true);
    });

    $("#formularioAprobado").submit(function (event) {
        $("#boton").prop("disabled", true);
    });

    $("#solicitudFCT").submit(function (event) {
        $("#boton").prop("disabled", true);
    });

    $("#agregarAlumno").click(function () {
        var ultimoAlumno = $(".datos-alumno").last().clone();
        ultimoAlumno.find("input[type=text], input[type=date]").val("");
        ultimoAlumno.find("#nifAlumno").on('input', function () {
            validarDNI($(this));
        });
        if (!ultimoAlumno.find(".eliminar-alumno").length) {
            var botonEliminar = $("<button class='btn btn-danger eliminar-alumno'>Eliminar alumno</button>");
            botonEliminar.click(function () {
                $(this).closest(".datos-alumno").remove();
            });
            ultimoAlumno.append(botonEliminar);
        }
        $("#contenedorAlumnos").append(ultimoAlumno);
    });

    $(document).on("click", ".eliminar-alumno", function () {
        $(this).closest(".datos-alumno").remove();
    });

    $("#confirmarBotonEnvio").click(function (event) {
        const calcularHorasTrabajadas = (inicio1, fin1, inicio2, fin2) => {
            const horaInicio1 = parseInt(inicio1.split(":")[0]);
            const minutoInicio1 = parseInt(inicio1.split(":")[1]);
            const horaFin1 = parseInt(fin1.split(":")[0]);
            const minutoFin1 = parseInt(fin1.split(":")[1]);
            const horaInicio2 = parseInt(inicio2.split(":")[0]);
            const minutoInicio2 = parseInt(inicio2.split(":")[1]);
            const horaFin2 = parseInt(fin2.split(":")[0]);
            const minutoFin2 = parseInt(fin2.split(":")[1]);

            const horasTramo1 = horaFin1 - horaInicio1 + (minutoFin1 - minutoInicio1) / 60;

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
            event.preventDefault();
            return false;
        }
    });

    $("#copiarHoras").click(function () {
        const horasLunesInicio1 = document.getElementsByName("lunesInicio1")[0].value;
        const horasLunesFin1 = document.getElementsByName("lunesFin1")[0].value;
        const horasLunesInicio2 = document.getElementsByName("lunesInicio2")[0].value;
        const horasLunesFin2 = document.getElementsByName("lunesFin2")[0].value;

        const diasSemana = ["martes", "miercoles", "jueves", "viernes"];
        diasSemana.forEach(function (dia) {
            document.getElementsByName(dia + "Inicio1")[0].value = horasLunesInicio1;
            document.getElementsByName(dia + "Fin1")[0].value = horasLunesFin1;
            document.getElementsByName(dia + "Inicio2")[0].value = horasLunesInicio2;
            document.getElementsByName(dia + "Fin2")[0].value = horasLunesFin2;
        });

        alert("Horas del lunes copiadas a todos los días de la semana.");
    });

    const opcionPDF = document.getElementById("opcionPDF");
    const opcionObservaciones = document.getElementById("opcionObservaciones");
    const campoPDF = document.getElementById("campoPDF");
    const campoObservaciones = document.getElementById("campoObservaciones");

    function habilitarPDF() {
        campoPDF.style.display = "block";
        campoObservaciones.style.display = "none";
    }

    function habilitarObservaciones() {
        campoPDF.style.display = "none";
        campoObservaciones.style.display = "block";
    }

    opcionPDF.addEventListener("change", function () {
        if (opcionPDF.checked) {
            habilitarPDF();
        }
    });

    opcionObservaciones.addEventListener("change", function () {
        if (opcionObservaciones.checked) {
            habilitarObservaciones();
        }
    });

    $('#formularioAlumnos').submit(function (event) {
        var opcionSeleccionada = $('input[name="opcion"]:checked').val();
        if (opcionSeleccionada === 'pdf') {
            this.action = '/jefatura/solicitudAceptadaJefatura';
            this.enctype = 'multipart/form-data';
        } else if (opcionSeleccionada === 'observaciones') {
            this.action = '/jefatura/solicitudRechazadaJefatura';
        }
        else {
            this.action = '/jefatura/errorSinSeleccionarJeftura';
        }
    });


    $('#solicitudFCT').submit(function (event) {
        event.preventDefault();
        var opcionSeleccionada = $('input[name="opcion"]:checked').val();
        if (opcionSeleccionada === 'pdf') {
            this.action = '/direccion/solicitudAceptadaDireccion';
            this.enctype = 'multipart/form-data';
        } else if (opcionSeleccionada === 'observaciones') {
            this.action = '/direccion/solicitudRechazadaDireccion';

        }
        else {
            this.action = '/direccion/errorSinSeleccionarDireccion';
        }
        this.submit();
    });


});