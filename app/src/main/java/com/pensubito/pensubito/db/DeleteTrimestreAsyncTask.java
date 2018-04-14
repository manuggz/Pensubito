package com.pensubito.pensubito.db;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

public class DeleteTrimestreAsyncTask extends AsyncTask<Void, Void, Void> {

    private PensubitoDao pensubitoDao;
    private WeakReference<OnTrimestreDeletedListener> onTrimestreDeletedListener;
    private int mTrimestreId;

    public DeleteTrimestreAsyncTask(@NonNull PensubitoDao pensubitoDao, int trimestreId){
        this.pensubitoDao = pensubitoDao;
        this.mTrimestreId = trimestreId;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        OnTrimestreDeletedListener listener = onTrimestreDeletedListener.get();
        if(listener != null){
            listener.onTrimestreDeleted(mTrimestreId);
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if(pensubitoDao != null ) {
            pensubitoDao.deleteTrimestre(mTrimestreId);
        }
        return null;
    }

    public void setOnTrimestreDeletedListener(OnTrimestreDeletedListener onTrimestreDeletedListener) {
        this.onTrimestreDeletedListener = new WeakReference<OnTrimestreDeletedListener>(onTrimestreDeletedListener);
    }
}