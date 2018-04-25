package com.pensubito.pensubito.util;

import com.pensubito.pensubito.pojosdao.MateriaTrimestreID;
import com.pensubito.pensubito.vo.Materia;
import com.pensubito.pensubito.vo.Trimestre;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

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


    public static ArrayList<Trimestre> calculateDataTrimestres(@Nullable List<MateriaTrimestreID> materiaTrimestreIDList){
        if(materiaTrimestreIDList == null) return null;

        ArrayList<Trimestre> trimestres = new ArrayList<>();

        Trimestre trimestreActual = null;

        int totalCreditosTotal = 0;
        int sumaCreditoXMateriaTotal = 0;

        int totalCreditosTrimestreActual = 0;
        int sumaCreditoXMateriaTrimestreActual = 0;

        int creditosMateriaActual;
        String notaMateriaActual;

        int nota_v;
        int nMaterias = 0;
        double indiceAcumuladoActual = 0 ;
        double indiceAcumuladoAnterior = 0 ;

        for (MateriaTrimestreID materiaTrimestreID : materiaTrimestreIDList) {

            if(trimestreActual == null || materiaTrimestreID.trimestreId != trimestreActual.getTrimestreId()){

                if(trimestreActual != null){
                    if(sumaCreditoXMateriaTotal > 0){
                        indiceAcumuladoActual = (double) sumaCreditoXMateriaTotal / totalCreditosTotal;
                        trimestreActual.setIndiceAcumuladoActual(indiceAcumuladoActual);
                        trimestreActual.setIndiceTrimestre((double) sumaCreditoXMateriaTrimestreActual / totalCreditosTrimestreActual);

                        trimestreActual.setContribucionAlIndiceAcumulado(indiceAcumuladoActual - indiceAcumuladoAnterior);
                        indiceAcumuladoAnterior = indiceAcumuladoActual;
                    }
                    trimestreActual.setnMaterias(nMaterias);
                }
                trimestreActual = new Trimestre(materiaTrimestreID.trimestreId,materiaTrimestreID.periodoId,materiaTrimestreID.anyo);
                trimestres.add(trimestreActual);

                nMaterias = 0;
                sumaCreditoXMateriaTrimestreActual = 0;
                totalCreditosTrimestreActual = 0;
            }

            if(materiaTrimestreID.materiaID != 0) {
                nMaterias += 1;
                if(sumaCreditoXMateriaTotal >= 0) {
                    creditosMateriaActual = materiaTrimestreID.creditos;

                    notaMateriaActual = materiaTrimestreID.nota;

                    if (notaMateriaActual == null || notaMateriaActual.isEmpty()) {
                        sumaCreditoXMateriaTotal = -1;
                    }else{
                        if (notaMateriaActual.equals("R")) {
                            continue;
                        }
                        nota_v = Integer.parseInt(notaMateriaActual);

                        sumaCreditoXMateriaTotal += creditosMateriaActual * nota_v;
                        totalCreditosTotal += creditosMateriaActual;

                        sumaCreditoXMateriaTrimestreActual += creditosMateriaActual * nota_v;
                        totalCreditosTrimestreActual += creditosMateriaActual;
                    }


                }
            }
        }

        if(trimestreActual != null){
            if(sumaCreditoXMateriaTotal > 0){
                indiceAcumuladoActual = (double) sumaCreditoXMateriaTotal / totalCreditosTotal;
                trimestreActual.setIndiceAcumuladoActual(indiceAcumuladoActual);
                trimestreActual.setIndiceTrimestre((double) sumaCreditoXMateriaTrimestreActual / totalCreditosTrimestreActual);

                trimestreActual.setContribucionAlIndiceAcumulado(indiceAcumuladoActual - indiceAcumuladoAnterior);
            }
            trimestreActual.setnMaterias(nMaterias);

        }

        return trimestres;
    }

    public static String roundIndice(double indice){
        Locale spanish = new Locale("es", "ES");
        return String.format(spanish,"%.4f", indice);
    }
}
