package com.pensubito.pensubito;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pensubito.pensubito.db.PensubitoDBUtil;
import com.pensubito.pensubito.pojosdao.MateriaTrimestreID;
import com.pensubito.pensubito.util.USBAlgoritmos;
import com.pensubito.pensubito.vm.TrimestreListViewModel;
import com.pensubito.pensubito.vo.Trimestre;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import timber.log.Timber;

/**
 */
public class TrimestreListActivity extends AppCompatActivity {

    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trimestre_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton btnAddPeriodo = (FloatingActionButton) findViewById(R.id.btn_add_periodo);
        btnAddPeriodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TrimestreListActivity.this, TrimestreAddActivity.class);
                startActivity(intent);
            }
        });

        if (findViewById(R.id.trimestre_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        TrimestreListViewModel viewModel = ViewModelProviders.of(this,viewModelFactory).get(TrimestreListViewModel.class);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView = findViewById(R.id.trimestre_list);
        mRecyclerView.setHasFixedSize(true);
        viewModel.getAllMateriasTrimestreID().observe(this, new Observer<List<MateriaTrimestreID>>() {
            @Override
            public void onChanged(@Nullable List<MateriaTrimestreID> trimestresMateria) {
                updateGUIList(trimestresMateria);
            }
        });
    }

    private void updateGUIList(@Nullable List<MateriaTrimestreID> trimestresMateria){
        // Establecemos los trimestres a la lista
        if(trimestresMateria == null) return;

        List<Trimestre> trimestres = USBAlgoritmos.calculateDataTrimestres(trimestresMateria);

        setUpAdapter(trimestres);
    }

    private void setUpAdapter(@Nullable List<Trimestre> trimestres){
        // Establecemos los trimestres a la lista
        if(trimestres == null) return;
        mRecyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, trimestres, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final TrimestreListActivity mParentActivity;
        private final List<Trimestre> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int trimestreId = (int) view.getTag();
                if (mTwoPane) {
//                    Bundle arguments = new Bundle();
//                    arguments.putString(TrimestreDetailFragment.ARG_TRIMESTRE_ID, trimestreId);
//                    TrimestreDetailFragment fragment = new TrimestreDetailFragment();
//                    fragment.setArguments(arguments);
//                    mParentActivity.getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.trimestre_detail_container, fragment)
//                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, TrimestreDetailActivity.class);
                    intent.putExtra(TrimestreDetailActivity.ARG_TRIMESTRE_ID, trimestreId);

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(TrimestreListActivity parent,
                                      List<Trimestre> values,
                                      boolean twoPane) {
            mValues = values;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.trimestre_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Trimestre trimestre = mValues.get(position);


            String periodoID = PensubitoDBUtil.convertIDPeriodoToString(trimestre.getPeriodoId()) + " " +String.valueOf(trimestre.getAnyo());

            holder.mPeriodoId.setText(periodoID);
            String periodoInfo = String.valueOf(trimestre.getnMaterias());
            if(trimestre.getnMaterias() == 1) {
                periodoInfo += " Materia";
            }else{
                periodoInfo += " Materias";
            }

            if(trimestre.getIndiceAcumuladoActual() >= 0) {
                periodoInfo += " " + USBAlgoritmos.roundIndice(trimestre.getIndiceAcumuladoActual());
            }else{
                periodoInfo += " NC";
            }

            holder.mPeriodoInfo.setText(periodoInfo);

            if(trimestre.getIndiceTrimestre() >= 0) {
                holder.mIndiceAcumulado.setText(USBAlgoritmos.roundIndice(trimestre.getIndiceTrimestre()));
            }else{
                holder.mIndiceAcumulado.setText("NC");
            }

            holder.itemView.setTag(trimestre.getTrimestreId());
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mPeriodoId;
            final TextView mPeriodoInfo;
            final TextView mIndiceAcumulado;

            ViewHolder(View view) {
                super(view);
                mPeriodoId = (TextView) view.findViewById(R.id.tv_periodo_id);
                mPeriodoInfo = (TextView) view.findViewById(R.id.tv_periodo_info);
                mIndiceAcumulado = (TextView) view.findViewById(R.id.tv_indice_acumulado);
            }
        }
    }
}
