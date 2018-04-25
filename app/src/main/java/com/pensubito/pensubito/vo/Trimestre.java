package com.pensubito.pensubito.vo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;


/**
 * Created by manuggz on 18/11/2017.
 *
 * Representa un trimestre del pensum
 */

@Entity(indices = {@Index(value = {"periodo_id","anyo"},unique = true)})
public class Trimestre {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "trimestre_id")
    private int trimestreId;

    /**
     * Periodo del Trimestre:
     * 1 | Enero      - Marzo
     * 2 | Abril      - Julio
     * 3 | Julio      - Agosto
     * 4 | Septiembre - Diciembre
     * */
    @ColumnInfo(name = "periodo_id")
    private int periodoId;

    /**
     * Año en el que se curso el trimestre
     * */
    private int anyo;

    @Ignore
    private double indiceAcumuladoActual;
    @Ignore
    private double indiceTrimestre;
    @Ignore
    private int nMaterias;
    @Ignore
    private double contribucionAlIndiceAcumulado;

    public Trimestre(){
    }

    public Trimestre(int trimestreId,int periodoId,int anyo){
        this.trimestreId = trimestreId;
        this.periodoId = periodoId;
        this.anyo = anyo;
    }
    public int getPeriodoId(){
        return periodoId;
    }

    public void setPeriodoId(int nuevoPeriodoId){
        this.periodoId = nuevoPeriodoId;
    }

    public int getAnyo(){
        return anyo;
    }

    public void setAnyo(int newAnyo){
        this.anyo = newAnyo;
    }

    public int getTrimestreId() {
        return trimestreId;
    }

    public void setTrimestreId(int trimestreId) {
        this.trimestreId = trimestreId;
    }

    public double getIndiceAcumuladoActual() {
        return indiceAcumuladoActual;
    }

    public void setIndiceAcumuladoActual(double indiceAcumuladoActual) {
        this.indiceAcumuladoActual = indiceAcumuladoActual;
    }

    public double getIndiceTrimestre() {
        return indiceTrimestre;
    }

    public void setIndiceTrimestre(double indiceTrimestre) {
        this.indiceTrimestre = indiceTrimestre;
    }

    public int getnMaterias() {
        return nMaterias;
    }

    public void setnMaterias(int nMaterias) {
        this.nMaterias = nMaterias;
    }

    public double getContribucionAlIndiceAcumulado() {
        return contribucionAlIndiceAcumulado;
    }

    public void setContribucionAlIndiceAcumulado(double contribucionAlIndiceAcumulado) {
        this.contribucionAlIndiceAcumulado = contribucionAlIndiceAcumulado;
    }
}
