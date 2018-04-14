package com.pensubito.pensubito.db;

public interface OnNewTrimestreInsertedListener {
    void onNewTrimestre(int newTrimestreId);
    void onSQLConstraintExceptionNewTrimestre();
}
