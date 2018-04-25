package com.pensubito.pensubito.vm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.pensubito.pensubito.db.PensubitoDao;
import com.pensubito.pensubito.pojosdao.MateriaTrimestreID;
import com.pensubito.pensubito.vo.Trimestre;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by manuggz on 21/11/2017.
 *
 * Se encarga de gestionar la comunicación con la BD, para mostrar una lista de trimestres
 *
 */

public class TrimestreListViewModel extends ViewModel {

    private TrimestreRepositorio trimestreRepositorio;

    //private LiveData<List<Trimestre>> trimestres;

    @Inject // TrimestreRepositorio parameter is provided by Dagger 2
    public TrimestreListViewModel(TrimestreRepositorio trimestreRepositorio){
        this.trimestreRepositorio = trimestreRepositorio;
    }

    public LiveData<List<Trimestre>> getTrimestres() {
        return trimestreRepositorio.loadTrimestres();
    }

    public LiveData<List<MateriaTrimestreID>> getAllMateriasTrimestreID() {
        return trimestreRepositorio.loadAllMateriasTrimestreID();
    }

}
