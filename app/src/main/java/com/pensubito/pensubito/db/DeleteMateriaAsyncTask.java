package com.pensubito.pensubito.db;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

public class DeleteMateriaAsyncTask extends AsyncTask<Void, Void, Void> {

    private PensubitoDao pensubitoDao;
    private WeakReference<OnMateriaDeletedListener> onMateriaDeletedListener;
    private int mMateriaId;

    public DeleteMateriaAsyncTask(@NonNull PensubitoDao pensubitoDao, int trimestreId){
        this.pensubitoDao = pensubitoDao;
        this.mMateriaId = trimestreId;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if(pensubitoDao != null ) {
            pensubitoDao.deleteMateria(mMateriaId);
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        OnMateriaDeletedListener listener = onMateriaDeletedListener.get();
        if(listener != null){
            listener.onMateriaDeleted(mMateriaId);
        }
    }
    public void setOnMateriaDeletedListener(OnMateriaDeletedListener onMateriaDeletedListener) {
        this.onMateriaDeletedListener = new WeakReference<OnMateriaDeletedListener>(onMateriaDeletedListener);
    }
}