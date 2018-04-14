package com.pensubito.pensubito.db;

import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.util.StringUtil;

import java.util.Collections;
import java.util.List;

/**
 * Created by manuggz on 18/11/2017.
 *
 * Contiene funciones últiles que trabajan sobre propiedades / acciones de la base de datos
 */

public abstract class PensubitoDBUtil {

    @TypeConverter
    public static List<Integer> stringToIntList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        return StringUtil.splitToIntList(data);
    }

    @TypeConverter
    public static String intListToString(List<Integer> ints) {
        return StringUtil.joinIntoString(ints);
    }
    /**
     * Los periodos se guardan como enteros en la BD para acelerar operaciones de ordenación
     * @param id_periodo ID que tiene el periodoId en la BD
     * @return Cadena leíble para un usuario de lo que representa el ID en la BD
     */
    public static String convertIDPeriodoToString(int id_periodo){
        switch (id_periodo){
            case 1:
                return "Enero - Marzo";
            case 2:
                return "Abril - Julio";
            case 3:
                return "Julio - Agosto";
            case 4:
                return "Septiembre - Diciembre";
            default:
                return "Periodo Desconocido";
        }
    }
}
