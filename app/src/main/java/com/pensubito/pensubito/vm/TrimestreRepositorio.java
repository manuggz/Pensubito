package com.pensubito.pensubito.vm;

import android.arch.lifecycle.LiveData;

import com.pensubito.pensubito.db.PensubitoDao;
import com.pensubito.pensubito.pojosdao.MateriaTrimestreID;
import com.pensubito.pensubito.vo.Materia;
import com.pensubito.pensubito.vo.Trimestre;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by manuggz on 21/11/2017.
 */
@Singleton // informs Dagger that this class should be constructed once
public class TrimestreRepositorio {

    private final PensubitoDao pensubitoDao;
    //private final Executor executor;

    //private TrimestreCache trimestreCache;

    @Inject
    public TrimestreRepositorio(PensubitoDao pensubitoDao) {
        //this.webservice = webservice;
        this.pensubitoDao = pensubitoDao;
        //this.executor = executor;
    }

    public  LiveData<Trimestre> getTrimestre(int trimestreId){
        return pensubitoDao.getTrimestreFromTrimestreId(trimestreId);
    }

    public LiveData<List<Trimestre>> loadTrimestres() {

        //MutableLiveData<List<Trimestre>> cached = userCache.get(userId);
        //if (cached != null) {
        //    return cached;
        //}

        // This is not an optimal implementation, we'll fix it below
        //executor.execute(() -> {
        // running in a background thread
        //});

        return pensubitoDao.loadAllTrimestres();
    }

    public LiveData<List<MateriaTrimestreID>> loadAllMateriasTrimestreID(){
        return pensubitoDao.loadAllMateriasTrimestreID();
    }
    public LiveData<List<Materia>> loadAllMateriasFromTrimestreId(int trimestre_id) {

        //MutableLiveData<List<Trimestre>> cached = userCache.get(userId);
        //if (cached != null) {
        //    return cached;
        //}

        // This is not an optimal implementation, we'll fix it below
        //executor.execute(() -> {
        // running in a background thread
        //});

        return pensubitoDao.loadAllMateriasFromTrimestreId(trimestre_id);
    }

}
