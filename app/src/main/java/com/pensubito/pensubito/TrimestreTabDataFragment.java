package com.pensubito.pensubito;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pensubito.pensubito.db.PensubitoDBUtil;
import com.pensubito.pensubito.di.Injectable;
import com.pensubito.pensubito.util.USBAlgoritmos;
import com.pensubito.pensubito.vm.TrimestreViewModel;
import com.pensubito.pensubito.vo.Materia;
import com.pensubito.pensubito.vo.Trimestre;

import java.util.List;

import javax.inject.Inject;

// Instances of this class are fragments representing a single
// object in our collection.
public class TrimestreTabDataFragment extends Fragment implements Injectable {
    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    private TrimestreViewModel viewModel;
    private TextView textViewPeriodoName;
    private TextView textViewIndiceObtenido;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.fragment_trimestre_data, container, false);

        textViewPeriodoName = ((TextView) rootView.findViewById(R.id.tv_periodo_name));
        textViewIndiceObtenido = ((TextView) rootView.findViewById(R.id.tv_periodo_io));

        Bundle args = getArguments();

        int trimestreId = args.getInt(TrimestreDetailActivity.ARG_TRIMESTRE_ID);
        viewModel = ViewModelProviders.of(this,viewModelFactory).get(TrimestreViewModel.class);
        viewModel.init(trimestreId);
        viewModel.getTrimestre().observe(this, new Observer<Trimestre>() {
            @Override
            public void onChanged(@Nullable Trimestre trimestre) {
                if(trimestre != null) {
                    updateUITrimestre(trimestre);
                }
            }
        });
        viewModel.getAllMaterias().observe(this, new Observer<List<Materia>>() {
            @Override
            public void onChanged(@Nullable List<Materia> materias) {
                if(materias != null) {
                    updateUIMaterias(materias);
                }
            }
        });
        return rootView;
    }

    public void updateUITrimestre(Trimestre trimestre){
        String periodoID = PensubitoDBUtil.convertIDPeriodoToString(trimestre.getPeriodoId()) + " " + String.valueOf(trimestre.getAnyo());

        textViewPeriodoName.setText(periodoID);
    }
    public void updateUIMaterias(List<Materia> materias){
        double indiceTrimestre = USBAlgoritmos.calcularIndiceTrimestre(materias);
        if(indiceTrimestre >= 1) {
            textViewIndiceObtenido.setText(String.valueOf(indiceTrimestre));
        }else{
            textViewIndiceObtenido.setText("No calculable");
        }
    }
}
