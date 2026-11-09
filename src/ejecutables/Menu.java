package ejecutables;
import java.util.InputMismatchException;
import java.util.Scanner;
import excepciones.NoEncuentraReservadorException;
import persistencia.Persistencia;
import universidad.Universidad;
import universidad.entidades.RangoHora;
import universidad.entidades.Reservador;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalTime;
import universidad.Aula;

// Enum para los diferentes tipos de reserva

enum TipoReserva {
    ASIGNATURA,
    CURSO_EXTENSION,
    EVENTO_INTERNO,
    EVENTO_EXTERNO
}

public class Menu {
    /**
     * Clase Menu que proporciona un menu interactivo para gestionar las operaciones
     * de una Universidad.
     * Metodo estatico que despliega el menu principal y gestiona las operaciones
     * disponibles.
     * 
     * @param uni Objeto Uni que representa la Universidad sobre la cual se
     *            realizan las operaciones.
     */
    public static void despliegaMenu(Universidad uni) {
        Scanner sc = new Scanner(System.in);
        int opcionSeleccionada = 0;
        do {
            System.out.println("\nBienvenido a la Universidad, que desea hacer?");
            System.out.println("* Listar los datos para cada tipo de entidad - 1");
            System.out.println("* Consultar los datos de las aulas segun piso y codigo - 2");
            System.out.println("* Registrar la reserva de un Aula - 3");
            System.out.println("* Cancelar reserva - 4");
            System.out.println("* Generar reporte - 5");
            System.out.println("* Salir - 6");
            System.out.println("Ingrese una opcion: ");
            try {
                opcionSeleccionada = sc.nextInt();
                switch (opcionSeleccionada) {
                    case 1:
                        System.out.println("# Datos de las Entidades:");
                        uni.listarEntidades();
                        break;
                    case 2:
                        consultarDatosAulas(uni, sc);
                        break;
                    case 3:
                        registrarReserva(uni, sc);
                        break;
                    case 4:
                        cancelaReserva(uni, sc);
                        break;
                    case 5:
                        System.out.println("[1] Para generar un reporte del monto recaudado");
                        System.out.println("[2] Para generar un reporte de tipo Listado");
                        System.out.println("[3] Para finalizar con la operacion");
                        uni.generaReportes(uni, sc);
                        break;
                    case 6:
                        Persistencia.persistirUniversidad(uni);
                        System.exit(1);
                        break;
                    default:
                        throw new IllegalStateException("Valor inesperado: " + opcionSeleccionada);
                }
            } catch (InputMismatchException e) {
                System.out.println("Formato ingresado invalido. Por favor, intentalo de nuevo.");
                sc.next();
            } catch (NoEncuentraReservadorException e) {
                System.out.println("No se encontro el reservador: " + e.getMessage());
            } catch (DateTimeParseException e) {
                System.out.println("Formato de fecha/hora invalido: " + e.getMessage());
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Ocurrio un error inesperado: " + e);
            }
        } while (opcionSeleccionada != 6);
        sc.close();
    }

    public static void consultarDatosAulas(Universidad uni, Scanner sc) {
        int valorNumerico = 0;
        boolean informaNoEncontro = false;
        sc.nextLine();
        System.out.println("Ingrese el numero de piso:");
        String piso = sc.nextLine();
        System.out.println("Ingrese el codigo de la asignatura/Curso de extension/Evento:");
        String codigo = sc.nextLine();
        System.out.println("#AULAS ENCONTRADAS:");
        if(piso.isEmpty() && codigo.isEmpty())
           uni.muestraAulas();
        else{
          if(piso.isEmpty()){
            informaNoEncontro = uni.muestraAulasPorReservador(codigo);
          }
          else{
            valorNumerico = Integer.parseInt(piso);
            if(codigo.isEmpty()) 
               uni.muestraAulasPorPiso(valorNumerico); 
            else
              informaNoEncontro = uni.muestraAulasPorPisoReservador(codigo, valorNumerico);
          }
        }
        if (informaNoEncontro)
            System.out.println("No se encontro ningun aula con los filtros aplicados");
    }

    public static void registrarReserva(Universidad uni, Scanner sc) {
        int tipoSeleccionado = 0;
        String codigo;
        Aula aulaReservada = null;
        do {
            System.out.println("Seleccione la entidad a reservar:");
            for (TipoReserva tipo : TipoReserva.values()) {
                System.out.println(tipo.ordinal() + 1 + ". " + tipo);
            }
            System.out.println("5. Volver al Menu");
            System.out.println("Ingrese una opcion: ");
            try {
                tipoSeleccionado = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("El formato ingresado no es valido, ingrese un formato adecuado.");
                sc.next();
                continue;
            }
            Reservador re = null;
            if (tipoSeleccionado > 0 && tipoSeleccionado < 5) {
                System.out.println("Ingrese el codigo de la asignatura/Curso de extension/Evento:");
                codigo = sc.next();
                try {
                    re = uni.buscaReservador(codigo);
                } catch (NoEncuentraReservadorException e) {
                    System.out.println(e.getMessage() + "\n");
                    continue;
                } catch (NullPointerException ignored) {
                }
            }
            switch (tipoSeleccionado) {
                case 1:
                    aulaReservada = reservarAsignatura(uni, re);
                    break;
                case 2:
                    aulaReservada = reservarCursoDeExtension(uni, re, sc);
                    break;
                case 3:
                    aulaReservada = reservarEventoInterno(uni, re);
                    break;
                case 4:
                    aulaReservada = reservarEventoExterno(uni, re, sc);
                    break;
                case 5:
                    Menu.despliegaMenu(uni);
                    break;
                default:
                    System.out.println("Valor inesperado, ingrese otra opcion");
            }
            if (aulaReservada == null)
               System.out.println("Ningun aula se encontraba disponible para la reserva\n");
            else
               System.out.println("\nReserva registrada con exito. \nAula reservada: \n" + aulaReservada);
        } while (tipoSeleccionado != 5);
    }

    public static Aula reservarAsignatura(Universidad uni, Reservador re) {
        Aula aulaReservada = uni.creaReserva(re);
        Persistencia.persistirUniversidad(uni);
        return aulaReservada;
    }

    public static Aula reservarCursoDeExtension(Universidad uni, Reservador re, Scanner sc) {
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fecha = null;
        boolean valido = false;
        while (!valido) {
            System.out.println("Introduce una fecha inicial con el formato : AAAA-MM-DD");
            String entrada = sc.next();
            try {
                fecha = LocalDate.parse(entrada, formateador);
                valido = true;
            } catch (DateTimeParseException e) {
                System.out.println(
                        "Formato de fecha invalido. Por favor, intentalo de nuevo.");
            }
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime inicio = null;
        LocalTime fin = null;
        boolean tiempoValido1 = false;
        boolean tiempoValido2 = false;
        while (!tiempoValido1) {
            System.out.println("Introduce una hora de inicio (HH:mm): ");
            String entrada = sc.next();
            try {
                inicio = LocalTime.parse(entrada, formatter);
                tiempoValido1 = true;
            } catch (DateTimeParseException e) {
                System.out.println(
                        "Formato de hora invalido. Por favor, intentalo de nuevo.");
            }
        }
        while (!tiempoValido2) {
            System.out.println("Introduce una hora de finalizacion (HH:mm): ");
            String entrada = sc.next();
            try {
                fin = LocalTime.parse(entrada, formatter);
                tiempoValido2 = true;
            } catch (DateTimeParseException e) {
                System.out.println(
                        "Formato de hora invalido. Por favor, intentalo de nuevo.");
            }
        }
        RangoHora rh = new RangoHora(inicio, fin);
        Aula aulaReservada = uni.creaReserva(re, fecha, rh);
        Persistencia.persistirUniversidad(uni);
        return aulaReservada;
    }

    public static Aula reservarEventoInterno(Universidad uni, Reservador re) {
        Aula aulaReservada = uni.creaReserva(re);
        Persistencia.persistirUniversidad(uni);
        return aulaReservada;
    }

    public static Aula reservarEventoExterno(Universidad uni, Reservador re, Scanner sc) {
        String nombre;
        float costo;
        boolean valido = false;
        Aula aulaReservada = null;
        System.out.println("Ingrese el nombre de la organizacion");
        nombre = sc.next();
        while (!valido) {
            try {
                System.out.println("Ingrese el costo de alquiler");
                costo = sc.nextFloat();
                valido = true;
                aulaReservada = uni.creaReserva(re, costo, nombre);
                Persistencia.persistirUniversidad(uni);
            } catch (InputMismatchException e) {
                System.out.println("Costo ingresado invalido, ingrese de nuevo");
                sc.next();
            }
        }
        return aulaReservada;
    }

    public static void cancelaReserva(Universidad uni, Scanner sc) {
        boolean valido = false;
        Aula aulaEncontrada = null;
        while (!valido) {
            try {
                System.out.println("Ingrese el numero del aula:");
                int numAula = sc.nextInt();
                System.out.println("Ingrese el codigo numerico de la reserva");
                int numReserva = sc.nextInt();
                aulaEncontrada = uni.cancelareserva(numAula, numReserva);
                Persistencia.persistirUniversidad(uni);
                valido = true;
            } catch (InputMismatchException e) {
                System.out.println("Formato de numero invalido. Por favor, intentalo de nuevo.");
                sc.next();
            }
        }
        if(aulaEncontrada == null)
            System.out.println("No se pudo encontrar un aula\n");
    }
}