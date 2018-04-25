package com.pensubito.pensubito;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.pensubito.pensubito.db.InsertNewMateriaAsyncTask;
import com.pensubito.pensubito.db.OnNewMateriaInsertedListener;
import com.pensubito.pensubito.db.PensubitoDao;
import com.pensubito.pensubito.vo.Materia;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

import static com.pensubito.pensubito.TrimestreDetailActivity.ARG_TRIMESTRE_ID;

public class TrimestreMateriaAddActivity extends AppCompatActivity implements OnNewMateriaInsertedListener {

    private int mCreditoSelected;
    private String mNotaSelected;
    @Inject
    PensubitoDao pensubitoDao;
    private InsertNewMateriaAsyncTask mInsertNewMateriaAsyncTask;
    private int mTrimestreId;
    private EditText mEditTextCodigoMateria;
    private EditText mEditTextNombreMateria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trimestre_materia_add);


        Toolbar trAddToolbar = (Toolbar) findViewById(R.id.tr_add_materia_toolbar);
        setSupportActionBar(trAddToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        if (savedInstanceState == null) {
            mTrimestreId = getIntent().getIntExtra(ARG_TRIMESTRE_ID,-1);
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        String creditosArray[] = {"1","2","3","4","5","6","7","8","9"};
        ArrayAdapter<CharSequence> creditosAdapter = new  ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item, creditosArray);
        // Specify the layout to use when the list of choices appears
        creditosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner creditosSpinner = (Spinner) findViewById(R.id.sp_creditos_materia);
        // Apply the adapter to the spinner
        creditosSpinner.setAdapter(creditosAdapter);
        creditosSpinner.setSelection(creditosAdapter.getPosition("3"));

        creditosSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                mCreditoSelected =  Integer.parseInt((String) parent.getItemAtPosition(pos));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        String notasArray[] = {"1","2","3","4","5","R"};

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> notasAdapter = new  ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item, notasArray);
        // Specify the layout to use when the list of choices appears
        notasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner notasSpinner = (Spinner) findViewById(R.id.sp_notas_materia);
        // Apply the adapter to the spinner
        notasSpinner.setAdapter(notasAdapter);
        notasSpinner.setSelection(notasAdapter.getPosition("3"));
        notasSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) {
                mNotaSelected = (String) parent.getItemAtPosition(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mEditTextCodigoMateria = (EditText)findViewById(R.id.et_codigo_materia);
        mEditTextNombreMateria = (EditText)findViewById(R.id.et_nombre_materia);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_trimestre_materia_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mInsertNewMateriaAsyncTask != null) {
            mInsertNewMateriaAsyncTask.cancel(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:

                String nombreMateria = mEditTextNombreMateria.getText().toString();
                if(nombreMateria.isEmpty()){
                    //return false;
                }

                String codigoMateria = mEditTextCodigoMateria.getText().toString();
                if(!codigoMateria.isEmpty()  && !codigoMateria.matches("[A-Za-z0-9]*")){
                    CharSequence text = "El código de la materia solo puede contener letras y números";
                    Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                    toast.show();
                    return true;
                }

                Materia newMateria = new Materia(mTrimestreId,nombreMateria,codigoMateria,mCreditoSelected,mNotaSelected);
                mInsertNewMateriaAsyncTask = new InsertNewMateriaAsyncTask(pensubitoDao,newMateria);
                mInsertNewMateriaAsyncTask.setOnNewMateriaTrimestreInsertedListener(this);
                mInsertNewMateriaAsyncTask.execute();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onNewMateriaInserted(int newMateriaId) {
        Context context = getApplicationContext();
        CharSequence text = "Materia agregada";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        finish();
    }

}

