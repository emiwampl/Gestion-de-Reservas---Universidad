package universidad.entidades;
import java.io.Serializable;
import java.time.LocalDate;

public class RangoFechas implements Comparable<RangoFechas>, Serializable {
    private LocalDate fechaInicial;
    private LocalDate fechaFinal;
    public RangoFechas(LocalDate fechaI, LocalDate fechaF) {
        fechaInicial = fechaI;
        fechaFinal = fechaF;
    }
    public LocalDate getFechaInicial() {
        return fechaInicial;
    }
    public LocalDate getFechaFinal() {
        return fechaFinal;
    }
    public void setFechaInicial(LocalDate fechaI) {
        fechaInicial = fechaI;
    }
    public void setFechaFinal(LocalDate fechaF) {
        fechaFinal = fechaF;
    }
    @Override
    public String toString() {
        return "\n\t\t  { Fecha inicial: " + fechaInicial + " }\n\t\t  { Fecha final: " + fechaFinal + " }";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        RangoFechas rangoh = (RangoFechas) obj;
        return fechaInicial.equals(rangoh.getFechaInicial()) && fechaFinal.equals(rangoh.getFechaFinal());
    }

    public boolean fechaUnica(){
        return fechaInicial == fechaFinal;
    }

    @Override
    public int hashCode() {
        int result = fechaInicial.hashCode();
        result = 31 * result + fechaFinal.hashCode();
        return result;
    }

    @Override
    public int compareTo(RangoFechas rangoh) {
        int compareStart = this.getFechaInicial().compareTo(rangoh.getFechaInicial());
        if (compareStart != 0) {
            return compareStart;
        }
        return this.getFechaFinal().compareTo(rangoh.getFechaFinal());
    }
}
