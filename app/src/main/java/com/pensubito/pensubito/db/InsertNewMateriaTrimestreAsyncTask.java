package com.pensubito.pensubito.db;

import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.pensubito.pensubito.vo.Materia;
import com.pensubito.pensubito.vo.Trimestre;

import java.lang.ref.WeakReference;

public class InsertNewMateriaTrimestreAsyncTask extends AsyncTask<Void, Void, Integer> {

    private PensubitoDao pensubitoDao;
    private WeakReference<OnNewMateriaTrimestreInsertedListener> onNewMateriaTrimestreInsertedListener;
    private Materia mMateria;

    public InsertNewMateriaTrimestreAsyncTask(@NonNull PensubitoDao pensubitoDao, Materia materia){
        this.pensubitoDao = pensubitoDao;
        this.mMateria = materia;
    }


    @Override
    protected Integer doInBackground(Void... voids) {

        if(pensubitoDao != null ) {
            try {
                int newMateriaId = (int)pensubitoDao.insertMateria(mMateria);
                return newMateriaId ;
            }catch (SQLiteConstraintException exception){
                return -1;
            }
        }

        return -1;
    }

    @Override
    protected void onPostExecute(Integer newMateriaId) {
        OnNewMateriaTrimestreInsertedListener listener = onNewMateriaTrimestreInsertedListener.get();
        if(listener != null){
            if(newMateriaId >= 0) {
                listener.onNewMateriaTrimestre(newMateriaId);
            }else{
                //listener.onSQLConstraintExceptionNewTrimestre();
            }
        }
    }
    public void setOnNewMateriaTrimestreInsertedListener(OnNewMateriaTrimestreInsertedListener onNewMateriaTrimestreInsertedListener) {
        this.onNewMateriaTrimestreInsertedListener = new WeakReference<OnNewMateriaTrimestreInsertedListener>(onNewMateriaTrimestreInsertedListener);
    }
}