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
import com.pensubito.pensubito.pojosdao.MateriaTrimestreID;
import com.pensubito.pensubito.util.USBAlgoritmos;
import com.pensubito.pensubito.vm.TrimestreViewModel;
import com.pensubito.pensubito.vo.Materia;
import com.pensubito.pensubito.vo.Trimestre;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

// Instances of this class are fragments representing a single
// object in our collection.
public class TrimestreTabDataFragment extends Fragment implements Injectable {
    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    private TrimestreViewModel viewModel;
    private TextView textViewPeriodoName;
    private TextView textViewIndiceObtenido;
    private TextView textViewIndiceAcumulado;
    private TextView textViewIndiceContribucion;
    private int trimestreId;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.fragment_trimestre_data, container, false);

        textViewPeriodoName = ((TextView) rootView.findViewById(R.id.tv_periodo_name));
        textViewIndiceObtenido = ((TextView) rootView.findViewById(R.id.tv_periodo_io));
        textViewIndiceAcumulado = ((TextView) rootView.findViewById(R.id.tv_periodo_ia));
        textViewIndiceContribucion = ((TextView) rootView.findViewById(R.id.tv_periodo_cont_ia));

        Bundle args = getArguments();

        trimestreId = args.getInt(TrimestreDetailActivity.ARG_TRIMESTRE_ID);
        viewModel = ViewModelProviders.of(this,viewModelFactory).get(TrimestreViewModel.class);
        viewModel.init(trimestreId);
        viewModel.getAllMateriasTrimestreID().observe(this, new Observer<List<MateriaTrimestreID>>() {
            @Override
            public void onChanged(@Nullable List<MateriaTrimestreID> materias) {
                if(materias != null) {
                    updateUI(materias);
                }
            }
        });

        return rootView;
    }

    public void updateUI(List<MateriaTrimestreID> materiaTrimestreIDList){

        ArrayList<Trimestre> trimestres = USBAlgoritmos.calculateDataTrimestres(materiaTrimestreIDList);

        for (Trimestre trimestre: trimestres) {
            if(trimestre.getTrimestreId() == trimestreId){
                updateUITrimestre(trimestre);

                if(trimestre.getIndiceTrimestre() >= 0) {
                    textViewIndiceObtenido.setText(String.valueOf(trimestre.getIndiceTrimestre()));
                }else{
                    textViewIndiceObtenido.setText("No calculable");
                }
                if(trimestre.getIndiceAcumuladoActual() >= 0) {
                    textViewIndiceAcumulado.setText(String.valueOf(trimestre.getIndiceAcumuladoActual()));
                }else{
                    textViewIndiceAcumulado.setText("No calculable");
                }

                double contribucionIndice = trimestre.getContribucionAlIndiceAcumulado();
                Locale spanish = new Locale("es", "ES");
                textViewIndiceContribucion.setText(String.format(spanish,"%+f",contribucionIndice));
            }
        }
    }

    public void updateUITrimestre(Trimestre trimestre){
        String periodoID = PensubitoDBUtil.convertIDPeriodoToString(trimestre.getPeriodoId()) + " " + String.valueOf(trimestre.getAnyo());

        textViewPeriodoName.setText(periodoID);
    }
}
