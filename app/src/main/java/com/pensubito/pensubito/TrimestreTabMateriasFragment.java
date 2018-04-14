package com.pensubito.pensubito;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    private TrimestreViewModel viewModel;
    private RecyclerView mRecyclerView;

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

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final TrimestreTabMateriasFragment mParentFragment;
        private final List<Materia> mValues;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int trimestreId = (int) view.getTag();
//                    Context context = view.getContext();
//                    Intent intent = new Intent(context, TrimestreDetailActivity.class);
//                    intent.putExtra(TrimestreDetailActivity.ARG_TRIMESTRE_ID, trimestreId);
//
//                    context.startActivity(intent);
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
            String materiaInfo = materia.getCodigo() + " " + materiaCreditos + " credito" + ((materiaCreditos == 1)?"":"s");

            holder.mMateriaInfo.setText(materiaInfo);

            String materiaNota = materia.getNota();
            if(materiaNota == null || materiaNota.isEmpty()){
                ///materiaNota = "Sin nota";
            }
            holder.mMateriaNota.setText(materiaNota);

            holder.itemView.setTag(materia.getTrimestreId());
            holder.itemView.setOnClickListener(mOnClickListener);
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
