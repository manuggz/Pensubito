package com.pensubito.pensubito;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pensubito.pensubito.db.DeleteMateriaAsyncTask;
import com.pensubito.pensubito.db.OnMateriaDeletedListener;
import com.pensubito.pensubito.db.PensubitoDao;
import com.pensubito.pensubito.di.Injectable;
import com.pensubito.pensubito.vm.TrimestreViewModel;
import com.pensubito.pensubito.vo.Materia;

import java.util.List;

import javax.inject.Inject;

// Instances of this class are fragments representing a single
// object in our collection.
public class TrimestreTabMateriasFragment extends Fragment implements Injectable {
    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    @Inject
    public PensubitoDao pensubitoDao;

    private TrimestreViewModel viewModel;
    private RecyclerView mRecyclerView;
    private DeleteMateriaAsyncTask mDeleteMateriaAsyncTask;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.fragment_trimestre_materias, container, false);

        Bundle args = getArguments();

        int trimestreId = args.getInt(TrimestreDetailActivity.ARG_TRIMESTRE_ID);

        TrimestreViewModel viewModel = ViewModelProviders.of(this,viewModelFactory).get(TrimestreViewModel.class);
        mRecyclerView = ((RecyclerView) rootView.findViewById(R.id.frag_materias_list));
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        viewModel.init(trimestreId);
        viewModel.getAllMaterias().observe(this, new Observer<List<Materia>>() {
            @Override
            public void onChanged(@Nullable List<Materia> materias) {
                setUpAdapter(materias);
            }
        });
        return rootView;
    }
    private void setUpAdapter(@Nullable List<Materia> materias){
        // Establecemos los trimestres a la lista
        if(materias == null) return;

        mRecyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, materias));
    }

    public void eliminarMateria(int materiaId){
        mDeleteMateriaAsyncTask = new DeleteMateriaAsyncTask(pensubitoDao,materiaId);
        mDeleteMateriaAsyncTask.setOnMateriaDeletedListener(new OnMateriaDeletedListener(){

            @Override
            public void onMateriaDeleted(int materiaId) {
                CharSequence text = "Materia eliminada";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getContext(), text, duration);
                toast.show();
            }
        });
        mDeleteMateriaAsyncTask.execute();
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final TrimestreTabMateriasFragment mParentFragment;
        private final List<Materia> mValues;
        private final View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                final int materiaId = (int) view.getTag();
                new AlertDialog.Builder(mParentFragment.getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("¿Eliminar esta materia?")
                        //.setMessage("Are you sure you want to close this activity?")
                        .setPositiveButton("BORRAR", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mParentFragment.eliminarMateria(materiaId);
                            }

                        })
                        .setNegativeButton("CANCELAR", null)
                        .show();

                return true;
            }
        };

        SimpleItemRecyclerViewAdapter(TrimestreTabMateriasFragment parent,
                                      List<Materia> values) {
            mValues = values;
            mParentFragment = parent;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.materia_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Materia materia = mValues.get(position);

            String materiaNombre = materia.getNombre();
            if(materiaNombre == null || materiaNombre.isEmpty()){
                materiaNombre = "Nombre no especificado";
            }
            holder.mMateriaName.setText(materiaNombre);

            int materiaCreditos = Integer.parseInt(materia.getCreditos());

            String materiaInfo = "";

            String materiaCodigo = materia.getCodigo();
            if(materiaCodigo != null && !materiaCodigo.isEmpty()){
                materiaInfo += materiaCodigo + " ";
            }

            materiaInfo += materiaCreditos + " credito" + ((materiaCreditos == 1)?"":"s");

            holder.mMateriaInfo.setText(materiaInfo);

            String materiaNota = materia.getNota();
            if(materiaNota == null || materiaNota.isEmpty()){
                ///materiaNota = "Sin nota";
            }
            holder.mMateriaNota.setText(materiaNota);

            holder.itemView.setTag(materia.getMateriaId());
            holder.itemView.setOnLongClickListener(mOnLongClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mMateriaName;
            final TextView mMateriaInfo;
            final TextView mMateriaNota;

            ViewHolder(View view) {
                super(view);
                mMateriaName = (TextView) view.findViewById(R.id.tv_materia_name);
                mMateriaInfo = (TextView) view.findViewById(R.id.tv_materia_info);
                mMateriaNota = (TextView) view.findViewById(R.id.tv_materia_nota);
            }
        }
    }
}
