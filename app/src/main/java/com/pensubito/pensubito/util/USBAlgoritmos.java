package com.pensubito.pensubito.util;

import com.pensubito.pensubito.vo.Materia;

import java.util.List;

public abstract class USBAlgoritmos {
    public static double calcularIndiceTrimestre(List<Materia> materias){
        int totalCreditos = 0;
        int sumaCreditoXMateria = 0;
        for (Materia materia : materias) {
            int creditos = materia.getCreditos();

            String nota = materia.getNota();

            if(nota == null || nota.isEmpty()){
                return -1;
            }
            if(nota.equals("R")){
                continue;
            }
            int nota_v = Integer.parseInt(nota);

            sumaCreditoXMateria += creditos*nota_v;
            totalCreditos += creditos;
        }

        return (double) sumaCreditoXMateria / totalCreditos;
    }
}
