package com.pensubito.pensubito.db;

import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.pensubito.pensubito.vo.Materia;
import com.pensubito.pensubito.vo.Trimestre;

import java.lang.ref.WeakReference;

public class InsertNewTrimestreAsyncTask extends AsyncTask<Void, Void, Integer> {

    private PensubitoDao pensubitoDao;
    private WeakReference<OnNewTrimestreInsertedListener> onNewPeriodoInsertedListener;
    private int mPeriodoId;
    private int mAnyo;

    public InsertNewTrimestreAsyncTask(@NonNull PensubitoDao pensubitoDao, int periodoId, int anyo){
        this.pensubitoDao = pensubitoDao;
        this.mPeriodoId = periodoId;
        this.mAnyo = anyo;
    }

    @Override
    protected void onPostExecute(Integer newTrimestreId) {
        OnNewTrimestreInsertedListener listener = onNewPeriodoInsertedListener.get();
        if(listener != null){
            if(newTrimestreId >= 0) {
                listener.onNewTrimestre(newTrimestreId);
            }else{
                listener.onSQLConstraintExceptionNewTrimestre();
            }
        }
    }

    @Override
    protected Integer doInBackground(Void... voids) {

        Trimestre trimestre = new Trimestre(mPeriodoId,mAnyo);

        if(pensubitoDao != null ) {
            try {
                int newTrimestreId = (int)pensubitoDao.insertTrimestre(trimestre);
                //Materia materia = new Materia(newTrimestreId,"Lógica Simbólica 1","CI-2525","4");
                //pensubitoDao.insertMateria(materia);
                return newTrimestreId ;
            }catch (SQLiteConstraintException exception){
                return -1;
            }
        }

        return -1;
    }

    public void setOnNewPeriodoInsertedListener(OnNewTrimestreInsertedListener onNewTrimestreInsertedListener) {
        this.onNewPeriodoInsertedListener = new WeakReference<OnNewTrimestreInsertedListener>(onNewTrimestreInsertedListener);
    }
}