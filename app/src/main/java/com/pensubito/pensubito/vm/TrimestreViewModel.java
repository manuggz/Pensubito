package com.pensubito.pensubito.vm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.pensubito.pensubito.vo.Materia;
import com.pensubito.pensubito.vo.Trimestre;

import java.util.List;

import javax.inject.Inject;

public class TrimestreViewModel extends ViewModel {
    private int mTrimestreId;
    private LiveData<Trimestre> trimestre;
    private TrimestreRepositorio trimestreRepositorio;

    @Inject // TrimestreRepositorio parameter is provided by Dagger 2
    public TrimestreViewModel(TrimestreRepositorio trimestreRepositorio){
        this.trimestreRepositorio = trimestreRepositorio;
    }

    public void init(int trimestreId) {
        if(trimestre != null){
            return;
        }
        this.mTrimestreId = trimestreId;
        trimestre = this.trimestreRepositorio.getTrimestre(trimestreId);
    }
    public LiveData<Trimestre> getTrimestre() {
        return trimestre;
    }
    public LiveData<List<Materia>> getAllMaterias() {
        return trimestreRepositorio.loadAllMateriasFromTrimestreId(mTrimestreId);
    }
}
