package universidad.entidades;

/**
 * La clase Asignatura representa una asignatura en una universidad.
 * Hereda de la clase Reservador e implementa Serializable.
 * Contiene informacion sobre el codigo, nombre, rango de fechas, horario,
 * día de la semana en que se dicta y la cantidad de alumnos inscriptos.
 */

public class Asignatura extends Reservador implements Comparable<Asignatura> {
    private String codigo;
    private RangoFechas rangoFechCurs;
    private RangoHora horario;
    private String nomAsignatura;
    private DiaSemana diaDicta;
    private int cantAlumnosInsc;

    public Asignatura(String codAlfaUniv, String nomAsigna, RangoFechas rangoFech, RangoHora rangoHora, DiaSemana diaSemana, int cantAlumnos) {
        codigo = codAlfaUniv;
        nomAsignatura = nomAsigna;
        rangoFechCurs = rangoFech;
        diaDicta = diaSemana;
        cantAlumnosInsc = cantAlumnos;
        horario = rangoHora;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCod(String cod) {
        codigo = cod;
    }

    public void setRangoFechCurs(RangoFechas rangoFechCurs) {
        this.rangoFechCurs = rangoFechCurs;
    }

    public RangoFechas getRangoFecha() {
        return rangoFechCurs;
    }

    public RangoHora getRangoHora() {
        return horario;
    }

    public DiaSemana getDiaDicta() {
        return diaDicta;
    }

    public void setDiaDicta(DiaSemana diaDicta) {
        this.diaDicta = diaDicta;
    }

    public void setHorario(RangoHora horario) {
        this.horario = horario;
    }

    public int getInscriptos() {
        return cantAlumnosInsc;
    };

    @Override
    public String toString() {
        return "\n\t  . Codigo alfanumerico univoco: " + codigo + "\n\t  . Nombre: " + nomAsignatura
                + "\n\t  . Rango de fechas de la cursada" + rangoFechCurs + "\n\t  . Dia de la semana que se dicta: "
                + diaDicta + "\n\t  . Horario: " + horario + "\n\t  . Cantidad de alumnos inscriptos: "
                + cantAlumnosInsc + "\n";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        Asignatura asignatura = (Asignatura) obj;
        return this.codigo == asignatura.codigo;
    }

    @Override
    public int compareTo(Asignatura asignatura) {
        return this.codigo.compareTo(asignatura.codigo);
    }
}
