package com.pensubito.pensubito.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.pensubito.pensubito.vo.Materia;
import com.pensubito.pensubito.vo.Trimestre;

/**
 * Created by manuggz on 18/11/2017.
 *
 * Gestiona la base de datos, es el principal punto de acceso.
 */


@Database(entities = {Materia.class,Trimestre.class},version = 1)
public abstract class PensubitoDb extends RoomDatabase {
    /**
     * Contiene los métodos de acceso a la base de datos
     * @return Referencia al Objeto Dato de toda la BD
     */
    abstract public PensubitoDao getPensubitoDao();
}
