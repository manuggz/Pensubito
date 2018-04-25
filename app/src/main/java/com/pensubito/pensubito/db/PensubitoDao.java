package com.pensubito.pensubito.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.pensubito.pensubito.pojosdao.MateriaTrimestreID;
import com.pensubito.pensubito.vo.Materia;
import com.pensubito.pensubito.vo.Trimestre;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.FAIL;

/**
 *
 * pensubito database access object
 * Created by manuggz on 18/11/2017.
 * Contiene métodos que ofrecen acceso a operaciones en la base de datos
 */


@Dao
public interface PensubitoDao {

    /**
     * Ingresa un nuevo trimestre
     * @param trimestre Trimestre a insertar
     * @return El ID del nuevo trimestre
     */
    @Insert(onConflict = FAIL)
    public long insertTrimestre(Trimestre trimestre);

    /**
     * Ingresa una nueva materia
     * @param materia Materia a insertar
     * @return El ID de la nueva materia
     */
    @Insert(onConflict = FAIL)
    public long insertMateria(Materia materia);

    /**
     * Actualiza los datos de un trimestre
     * @param trimestre Trimestre a actualizar con sus nuevos campos
     */
    @Update
    public void updateTrimestre(Trimestre trimestre);

    /**
     * Actualiza una materia
     * @param materia Materia a actualizar con los nuevos campos
     */
    @Update
    public void updateMateria(Materia materia);

    /**
     * Elimina un trimestre
     * @param trimestre Trimestre a eliminar
     */
    @Delete
    public void deleteTrimestre(Trimestre trimestre);

    /**
     * Elimina un materia
     * @param materia Materia a eliminar
     */
    @Delete
    public void deleteMateria(Materia materia);

    /**
     * Retorna todos los trimestres guardados
     * @return Array de trimestres
     */
    @Query("SELECT * from trimestre")
    public LiveData<List<Trimestre>> loadAllTrimestres();


    /**
     * Retorna todas las materias guardadas
     * @return Array de materias
     */
    @Query("SELECT * from materia")
    public Materia[] loadAllMaterias();

    /**
     * Retorna todas las materias de un trimestre en particular
     * @return Array de materias
     */
    @Query("SELECT * from materia WHERE trimestre_id = :trimestreId")
    public LiveData<List<Materia>> loadAllMateriasFromTrimestreId(int trimestreId);


    /**
     * Regresa un trimestre en particular
     * @return Trimestre
     */
    @Query("SELECT * from trimestre WHERE trimestre_id = :trimestreId")
    public LiveData<Trimestre> getTrimestreFromTrimestreId(int trimestreId);

    @Query("DELETE from trimestre WHERE trimestre_id = :trimestreId")
    public void deleteTrimestre(int trimestreId);

    @Query("DELETE from materia WHERE materia_id = :materiaId")
    public void deleteMateria(int materiaId);

    @Query("SELECT trimestre.trimestre_id AS trimestreId"
            + ", trimestre.periodo_id AS periodoId "
            + ", trimestre.anyo "
            + ", materia.materia_id AS materiaID "
            + ", materia.creditos "
            + ", materia.nota "
            + "FROM trimestre "
            + "LEFT JOIN materia "
            + "ON trimestre.trimestre_id = materia.trimestre_id "
            + "ORDER BY trimestre.trimestre_id ASC"
    )
    public LiveData<List<MateriaTrimestreID>> loadAllMateriasTrimestreID();


}
