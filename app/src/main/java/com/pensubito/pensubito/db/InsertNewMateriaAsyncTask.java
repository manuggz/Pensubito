package com.pensubito.pensubito.db;

import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.pensubito.pensubito.vo.Materia;

import java.lang.ref.WeakReference;

public class InsertNewMateriaAsyncTask extends AsyncTask<Void, Void, Integer> {

    private PensubitoDao pensubitoDao;
    private WeakReference<OnNewMateriaInsertedListener> onNewMateriaTrimestreInsertedListener;
    private Materia mMateria;

    public InsertNewMateriaAsyncTask(@NonNull PensubitoDao pensubitoDao, Materia materia){
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
        OnNewMateriaInsertedListener listener = onNewMateriaTrimestreInsertedListener.get();
        if(listener != null){
            if(newMateriaId >= 0) {
                listener.onNewMateriaInserted(newMateriaId);
            }else{
                //listener.onSQLConstraintExceptionNewTrimestre();
            }
        }
    }
    public void setOnNewMateriaTrimestreInsertedListener(OnNewMateriaInsertedListener onNewMateriaInsertedListener) {
        this.onNewMateriaTrimestreInsertedListener = new WeakReference<OnNewMateriaInsertedListener>(onNewMateriaInsertedListener);
    }
}