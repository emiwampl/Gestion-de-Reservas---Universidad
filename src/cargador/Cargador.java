package cargador;
import universidad.entidades.Asignatura;
import universidad.entidades.CursoDeExtension;
import universidad.entidades.DiaSemana;
import universidad.entidades.EventoExterno;
import universidad.entidades.EventoInterno;
import universidad.entidades.RangoFechas;
import universidad.entidades.RangoHora;
import universidad.entidades.Reservador;
import universidad.Universidad;
import universidad.Aula;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import excepciones.ValorNegativoException;
import excepciones.NoEncuentraReservadorException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;

/**
 * Clase que proporciona metodos estaticos para cargar datos desde un documento XML a una instancia de Universidad.
 * Utiliza DOM para el analisis del XML.
 * Permite cargar un objeto de tipo Uni a partir de un documento.
 */

public class Cargador {
    public static Document cargarDocumentoDesdeXML(String caminoDocumentoXML) {
        try {
            Document documento;
            // Crear un constructor de documentos
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Cargar el archivo XML en un documento
            File archivoXML = new File(caminoDocumentoXML);
            documento = builder.parse(archivoXML);
            // Normalizar el documento (opcional)
            documento.getDocumentElement().normalize();
            return documento;
        } catch (IOException | IllegalArgumentException | ParserConfigurationException | SAXException e) {
            Logger.getLogger(Cargador.class.getName()).log(Level.SEVERE, "Error al cargar el documento XML", e);
            return null;
        }
    }
    public static Universidad cargaUniversidad(Document doc) {
        Universidad universi = new Universidad();
        cargarReservadoresDesdeXML(universi, doc);
        cargarAulasDesdeXML(universi, doc);
        return universi;
    }

    ///Método para cargar reservadores desde el XML
    private static void cargarReservadoresDesdeXML(Universidad universidad, Document documento) {

        // Cargar Asignaturas
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        NodeList asignaturasList = documento.getElementsByTagName("Asignatura");
        for (int i = 0; i < asignaturasList.getLength(); i++) {
            Node node = asignaturasList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elemento = (Element) node;
                String codigo = elemento.getAttribute("codigo");
                String nomAsignatura = elemento.getElementsByTagName("nomAsignatura").item(0).getTextContent();
                RangoFechas rangoFechas;
                try{
                   rangoFechas = parseRangoFechas(elemento.getElementsByTagName("RangoFechas").item(0));
                }catch(DateTimeParseException e){
                    throw new DateTimeParseException("Error al parsear RangoFechas en la Asignatura " + codigo + "-" + nomAsignatura, "", i);
                }
                String diaDictaTexto = elemento.getElementsByTagName("diaDicta").item(0).getTextContent();
                DiaSemana diaDicta;
                try {
                    diaDicta = DiaSemana.valueOf(diaDictaTexto.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Valor de diaDicta inválido: " + diaDictaTexto, e);
                }
                RangoHora rangoHora;
                try{
                    rangoHora = parseRangoHora(elemento.getElementsByTagName("RangoHora").item(0));
                }catch(DateTimeParseException e){
                    throw new DateTimeParseException("Error al parsear RangoHora en la asignatura " + codigo + "-" + nomAsignatura, null, i);
                }
                int cantAlumnosInsc;
                try{
                    cantAlumnosInsc = Integer.parseInt(elemento.getElementsByTagName("cantAlumnoInsc").item(0).getTextContent());
                }catch(NumberFormatException e){
                    throw new NumberFormatException("Error al parsear \"cantAlumnosInsc\" en la Asignatura "+nomAsignatura+"-"+codigo);
                }
                if(cantAlumnosInsc<0){
                    throw new ValorNegativoException("Error al parsear \"cantAlumnosInsc\" en la Asignatura "+nomAsignatura+"-"+codigo);
                }
                Asignatura asignatura = new Asignatura(codigo, nomAsignatura, rangoFechas, rangoHora, diaDicta, cantAlumnosInsc);
                universidad.agregaReservador(asignatura);
            }
        }

        // Cargar Cursos de Extension
        NodeList cursosList = documento.getElementsByTagName("CursoExtension");
        for (int i = 0; i < cursosList.getLength(); i++) {
            Node node = cursosList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elemento = (Element) node;
                String codigo = elemento.getAttribute("codigo");
                String descripcion = elemento.getElementsByTagName("descripcion").item(0).getTextContent();
                int cantInscMax;
                try{
                    cantInscMax = Integer.parseInt(elemento.getElementsByTagName("cantInscMax").item(0).getTextContent());
                }catch(NumberFormatException e){
                    throw new NumberFormatException("Error al parsear \"cantInscMax\" en el CursoDeExtension "+codigo);
                }
                if(cantInscMax < 0){
                    throw new ValorNegativoException("Valor invalido en \"cantInscMax\" en el CursoDeExtension "+codigo);
                }
                int cantClases;
                try{
                    cantClases = Integer.parseInt(elemento.getElementsByTagName("cantClases").item(0).getTextContent());
                }catch(NumberFormatException e){
                    throw new NumberFormatException("Error al parsear \"cantClases\" en el CursoDeExtension "+codigo);
                }
                if(cantClases < 0){
                    throw new ValorNegativoException("Valor invalido en \"cantInscMax\" en el CursoDeExtension "+codigo);
                }
                float costoCurso;
                try{
                   costoCurso = Float.parseFloat(elemento.getElementsByTagName("costoCurso").item(0).getTextContent());
                }catch(NumberFormatException e){
                    throw new NumberFormatException("Error al parsear costoCurso en el CursoDeExtension "+codigo+", formato de numero invalido");
                }
                if(costoCurso < 0){
                    throw new ValorNegativoException("Error al parsear costoCurso en el CursoDeExtension "+codigo);
                }
                CursoDeExtension curso = new CursoDeExtension(codigo, descripcion, cantInscMax, cantClases, costoCurso);
                universidad.agregaReservador(curso);
            }
        }

        // Cargar Eventos

        //EventoInterno
        NodeList eventosInternosList = documento.getElementsByTagName("EventoInterno");
        for (int i = 0; i < eventosInternosList.getLength(); i++) {
            Node node = eventosInternosList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE){
                Element elemento = (Element) node;
                String codigo = String.format(elemento.getAttribute("codigo"));
                LocalDate fecha;
                try{
                    fecha = LocalDate.parse(elemento.getElementsByTagName("fecha").item(0).getTextContent(), formatter);
                }catch(DateTimeParseException e){
                    throw new DateTimeParseException("Error al parsear \"fecha\" en el EventoInterno " + codigo, null, i);
                }
                RangoHora rangoHora;
                try{
                    rangoHora = parseRangoHora(elemento.getElementsByTagName("RangoHora").item(0));
                }catch(DateTimeParseException e){
                    throw new DateTimeParseException("Error al parsear \"RangoHora\" en el EventoInterno "+codigo, null, i);
                }
                String descripcion = elemento.getElementsByTagName("descripcion").item(0).getTextContent();
                int cantMaxPart;
                try{
                    cantMaxPart = Integer.parseInt(elemento.getElementsByTagName("cantMaxPart").item(0).getTextContent());
                }catch(NumberFormatException e){
                    throw new NumberFormatException("Error al parsear \"cantMaxPart\" en el EventoInterno "+codigo);
                }
                if(cantMaxPart < 0){
                    throw new ValorNegativoException("Valor invalido de \"cantMaxPart\" en el EventoInterno "+codigo);
                }
                EventoInterno evento = new EventoInterno(codigo, fecha, rangoHora, descripcion, cantMaxPart);
                universidad.agregaReservador(evento);
            }
        }

        //EventoExterno
        NodeList eventosExternosList = documento.getElementsByTagName("EventoExterno");
        for (int i = 0; i < eventosExternosList.getLength(); i++) {
            Node node = eventosExternosList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE){
                Element elemento = (Element) node;
                String codigo = String.format(elemento.getAttribute("codigo"));
                LocalDate fecha;
                try{
                    fecha = LocalDate.parse(elemento.getElementsByTagName("fecha").item(0).getTextContent(), formatter);
                }catch(DateTimeParseException e){
                    throw new DateTimeParseException("Error al parsear \"fecha\" en el EventoExterno " + codigo, null, i);
                }
                RangoHora rangoHora;
                try{
                    rangoHora = parseRangoHora(elemento.getElementsByTagName("RangoHora").item(0));
                }catch(DateTimeParseException e){
                    throw new DateTimeParseException("Error al parsear \"RangoHora\" en el EventoExterno "+codigo, null, i);
                }
                int cantMaxPart = Integer.parseInt(elemento.getElementsByTagName("cantMaxPart").item(0).getTextContent());
                float costo;
                try{
                    costo = Float.parseFloat(elemento.getElementsByTagName("costo").item(0).getTextContent());
                }catch(NumberFormatException e){
                    throw new NumberFormatException("Error al parsear \"costo\" en el EventoExterno "+codigo);
                }
                if(costo < 0){
                    throw new ValorNegativoException("Error al parsear \"costo\" en el EventoExterno "+codigo+", valor de costo negativo");
                }
                EventoExterno evento = new EventoExterno(codigo, fecha, rangoHora,cantMaxPart, costo);
                universidad.agregaReservador(evento);
            }
        }
    }

    ///Método para cargar aulas desde el XML
    private static void cargarAulasDesdeXML(Universidad universidad, Document documento) {
        NodeList nodeList = documento.getElementsByTagName("Aula");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elemento = (Element) node;
                int id;
                try{
                    id = Integer.parseInt(elemento.getAttribute("id"));
                }catch(NumberFormatException e){
                    throw new NumberFormatException("Error al parsear \"id\" en Aula");
                }
                if(id<0){
                    throw new ValorNegativoException("Valor invalido de \"id\" en Aula");
                }
                int capacidad;
                try{
                    capacidad = Integer.parseInt(elemento.getElementsByTagName("capacidad").item(0).getTextContent());
                }catch(NumberFormatException e){
                    throw new NumberFormatException("Error al parsear \"Capacidad\" en el Aula "+id);
                }
                if(capacidad < 0){
                    throw new ValorNegativoException("Valor invalido de \"capacidad\" en el Aula "+id);
                }
                Aula aula = new Aula(capacidad,id);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                // Cargar reservas para esta aula
                NodeList reservasList = elemento.getElementsByTagName("reserva");
                for (int j = 0; j < reservasList.getLength(); j++) {
                    Node reservaNode = reservasList.item(j);
                    if (reservaNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element reservaElemento = (Element) reservaNode;
                        LocalDate fecha;
                        try{
                            fecha = LocalDate.parse(reservaElemento.getElementsByTagName("fecha").item(0).getTextContent(), formatter);
                        }catch(DateTimeParseException e){
                            throw new DateTimeParseException("Error al parsear \"fecha\" en el Aula "+id, null, i);
                        }
                        RangoHora rangoHora;
                        try{
                            rangoHora = parseRangoHora(reservaElemento.getElementsByTagName("RangoHora").item(0));
                        }catch(DateTimeParseException e){
                            throw new DateTimeParseException("Error al parsear \"rangoHora\" en el Aula "+id, null, i);
                        }
                        Element rsElement = (Element) reservaElemento.getElementsByTagName("rs").item(0);
                        String codigoRs = rsElement.getAttribute("codigo");
                        //Obtengo el reservador en base al codigo
                        try{
                            Reservador reservador = universidad.buscaReservador(codigoRs);
                            aula.poneReserva(reservador, fecha, rangoHora);
                        }
                        catch(NoEncuentraReservadorException e){
                            System.out.println("Error en la reserva:\n" + fecha + "\n" + rangoHora + "\n" + codigoRs + "\n" + e.getMessage());
                        }
                    }
                }
                //Agregamos el aula.
                universidad.agregaAula(aula);

            }
        }
    }

    ///Metodo auxiliar para parsear RangoHora
    private static RangoHora parseRangoHora(Node rangoHoraNode) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        if (rangoHoraNode.getNodeType() == Node.ELEMENT_NODE) {
            Element rangoHoraElemento = (Element) rangoHoraNode;
            LocalTime horaInicial;
            try{
            horaInicial = LocalTime.parse(rangoHoraElemento.getElementsByTagName("horaInicial").item(0).getTextContent(), timeFormatter);
            }catch(DateTimeParseException e){
                throw new DateTimeParseException(null, null, 0);
            }
            LocalTime horaFinal;
            try{
                horaFinal = LocalTime.parse(rangoHoraElemento.getElementsByTagName("horaFinal").item(0).getTextContent(), timeFormatter);
            }catch(DateTimeParseException e){
                throw new DateTimeParseException(null, null, 0);
            }
            return new RangoHora(horaInicial, horaFinal);
        }
        return null;
    }

    ///Metodo auxiliar para parsear RangoFechas
    private static RangoFechas parseRangoFechas(Node rangoFechasNode) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (rangoFechasNode.getNodeType() == Node.ELEMENT_NODE) {
            Element rangoFechasElemento = (Element) rangoFechasNode;
            LocalDate fechaInicial = null;
            LocalDate fechaFinal = null;
            try{
               fechaInicial = LocalDate.parse(rangoFechasElemento.getElementsByTagName("fechaInicial").item(0).getTextContent(), formatter);
            }catch(DateTimeParseException e){
                throw new DateTimeParseException("", "", 0);
            }
            try{
                fechaFinal = LocalDate.parse(rangoFechasElemento.getElementsByTagName("fechaFinal").item(0).getTextContent(), formatter);
            }catch(DateTimeParseException e){
                throw new DateTimeParseException("", "", 0);
            }
            return new RangoFechas(fechaInicial, fechaFinal);
        }
        return null;
    }
}
