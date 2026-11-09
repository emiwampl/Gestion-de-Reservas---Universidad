package universidad.entidades;

/**
 * Clase que representa un Curso de Extension en la universidad.
 * Extiende la clase Reservador e implementa Serializable.
 * Proporciona informacion sobre el curso, incluyendo el codigo, descripcion,
 * cantidad maxima de inscripciones, cantidad de clases, y el costo del curso.
 */

public class CursoDeExtension extends Reservador implements Comparable<CursoDeExtension>{
    private String codigo;
    private String descripcion;
    private int cantInscMax;
    private int cantClases;
    private float costoCurso;

    public CursoDeExtension(String codAlfa, String desc, int cantInsc, int cantClas, float costoCur){
        codigo = codAlfa;
        descripcion = desc;
        cantInscMax = cantInsc;
        cantClases = cantClas;
        costoCurso = costoCur;
    }
    public String getCodigo(){
        return codigo;
    }
    public float getCosto(){return costoCurso;}
    public void setCod(String cod){
        codigo = cod;
    }
    public String getDescripcion(){
        return descripcion;
    }
    public void setDescripcion(String descripcion){
        this.descripcion = descripcion;
    }
    public int getCantInscMax(){
        return cantInscMax;
    }
    public void setCantInscMax(int cantInscMax){
        this.cantInscMax = cantInscMax;
    }
    public int getCantClases(){return cantClases;}
    public void setCantClases(int cantClases){
        this.cantClases = cantClases;
    }
    public float getCostoCurso(){
        return costoCurso;
    }
    public void setCostoCurso(float costoCurso){
        this.costoCurso = costoCurso;
    }
    public int getInscriptos(){return cantInscMax;}
    @Override
    public RangoFechas getRangoFecha() {return null;}
    public RangoHora getRangoHora(){return null;}

    @Override
    public String toString(){
        return "\n\t  . Codigo Alfanumerico: " + codigo + "\n\t  . Descripcion: " + descripcion + "\n\t  . Cantidad de inscripciones maxima: " + cantInscMax + "\n\t  . Cantidad de clases: " + cantClases + "\n\t  . Costo por curso: " + costoCurso + "\n";
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(obj == null || this.getClass() != obj.getClass()) return false;
        CursoDeExtension cursoExtension = (CursoDeExtension)obj;
        return codigo == cursoExtension.codigo;
    }

    @Override
    public int compareTo(CursoDeExtension cursoExtension){
        return codigo.compareTo(cursoExtension.codigo);
    }
}
