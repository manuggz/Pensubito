package com.pensubito.pensubito;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.pensubito.pensubito.db.InsertNewTrimestreAsyncTask;
import com.pensubito.pensubito.db.OnNewTrimestreInsertedListener;
import com.pensubito.pensubito.db.PensubitoDao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class TrimestreAddActivity extends AppCompatActivity implements OnNewTrimestreInsertedListener {

    private int mPeriodoSelected;
    private int mAnyoSelected;
    @Inject
    PensubitoDao pensubitoDao;
    private InsertNewTrimestreAsyncTask mInsertNewTrimestreAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trimestre_add);

        Toolbar trAddToolbar = (Toolbar) findViewById(R.id.tr_add_toolbar);
        setSupportActionBar(trAddToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        Spinner periodosSpinner = (Spinner) findViewById(R.id.periodos_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        String periodosList[] = {"Enero-Marzo","Abril-Julio","Julio-Agosto(Verano)","Septiembre-Diciembre"};
        ArrayAdapter<CharSequence> periodosAdapter = new  ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item, periodosList);
        // Specify the layout to use when the list of choices appears
        periodosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        periodosSpinner.setAdapter(periodosAdapter);
        periodosSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) {
                mPeriodoSelected = pos + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Spinner anyosSpinner = (Spinner) findViewById(R.id.anyo_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<Integer> anyosAdapter = new  ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, range(1999, 2020));
        // Specify the layout to use when the list of choices appears
        anyosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        anyosSpinner.setAdapter(anyosAdapter);
        anyosSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) {
                mAnyoSelected = (int) parent.getItemAtPosition(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_trimestre_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mInsertNewTrimestreAsyncTask != null) {
            mInsertNewTrimestreAsyncTask.cancel(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                mInsertNewTrimestreAsyncTask = new InsertNewTrimestreAsyncTask(pensubitoDao,mPeriodoSelected,mAnyoSelected);
                mInsertNewTrimestreAsyncTask.setOnNewPeriodoInsertedListener(this);
                mInsertNewTrimestreAsyncTask.execute();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public static List<Integer> range(int min, int max) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = min; i <= max; i++) {
            list.add(i);
        }

        return list;
    }

    @Override
    public void onNewTrimestre(int newTrimestreId) {
        Context context = getApplicationContext();
        CharSequence text = "Trimestre agregado";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        Intent intent = new Intent(this, TrimestreDetailActivity.class);
        intent.putExtra(TrimestreDetailActivity.ARG_TRIMESTRE_ID, newTrimestreId);
        startActivity(intent);
    }

    @Override
    public void onSQLConstraintExceptionNewTrimestre() {
        Context context = getApplicationContext();
        CharSequence text = "Ya existe ese trimestre";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }
}
