package com.pensubito.pensubito.vo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by manuggz on 18/11/2017.
 *
 *  Representa una materia del pensum
 */

@Entity(foreignKeys = @ForeignKey(entity = Trimestre.class,
                                    parentColumns = "trimestre_id",
                                    childColumns = "trimestre_id",
                                    onDelete = CASCADE))
public class Materia {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "materia_id")
    private int materiaId;

    /***
     * Nombre de la Materia: Ej Matemáticas I
     */
    private String nombre;

    /***
     * Código de la Materia: Ej MA-2111
     */
    private String codigo;

    /***
     * Créditos de la Materia: Ej 5
     */
    private int creditos;

    /***
     * Nota de la Materia: 1-5  | R | NA
     */
    private String nota;

    /**
     * Referencia de la materia al trimestre donde se cursa
     * - No debe haber la misma materia más de una vez en el trimestre
     */
    @ColumnInfo(name = "trimestre_id")
    private int trimestreId;

    public Materia(int trimestreId,String nombre,String codigo,int creditos,String nota){
        this.trimestreId = trimestreId;
        this.nombre = nombre;
        this.codigo = codigo;
        this.creditos = creditos;
        this.nota = nota;
    }
    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getMateriaId() {
        return materiaId;
    }

    public void setMateriaId(int materiaId) {
        this.materiaId = materiaId;
    }

    public int getTrimestreId() {
        return trimestreId;
    }

    public void setTrimestreId(int trimestreId) {
        this.trimestreId = trimestreId;
    }
}
