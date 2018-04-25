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
    protected Integer doInBackground(Void... voids) {

        Trimestre trimestre = new Trimestre();
        trimestre.setPeriodoId(mPeriodoId);
        trimestre.setAnyo(mAnyo);

        if(pensubitoDao != null ) {
            try {
                int newTrimestreId = (int)pensubitoDao.insertTrimestre(trimestre);
                return newTrimestreId ;
            }catch (SQLiteConstraintException exception){
                return -1;
            }
        }

        return -1;
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
    public void setOnNewPeriodoInsertedListener(OnNewTrimestreInsertedListener onNewTrimestreInsertedListener) {
        this.onNewPeriodoInsertedListener = new WeakReference<OnNewTrimestreInsertedListener>(onNewTrimestreInsertedListener);
    }
}