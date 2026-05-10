package com.tp.gestiondepenses.ui.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.model.Revenu;
import com.tp.gestiondepenses.viewmodel.RevenuViewModel;

import java.util.Calendar;

/**
 * Fragment formulaire d’ajout de revenu.
 * Correspond à la maquette fragment_formulaire_revenus.xml.
 */
public class FormulaireRevenusFragment extends Fragment {

    private EditText edtSource, edtMontant, edtDate, edtDescription;
    private Button btnAjouter;
    private Calendar calendar;
    private RevenuViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_formulaire_revenus, container, false);

        edtSource = view.findViewById(R.id.edtSource);
        edtMontant = view.findViewById(R.id.edtMontant);
        edtDate = view.findViewById(R.id.edtDate);
        edtDescription = view.findViewById(R.id.edtDescription);
        btnAjouter = view.findViewById(R.id.btnAjouterRevenu);

        viewModel = new ViewModelProvider(requireActivity()).get(RevenuViewModel.class);
        calendar = Calendar.getInstance();

        edtDate.setOnClickListener(v -> {
            new DatePickerDialog(requireContext(),
                    (datePicker, year, month, day) -> {
                        calendar.set(year, month, day);
                        edtDate.setText(day + "/" + (month + 1) + "/" + year);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        btnAjouter.setOnClickListener(v -> {
            String source = edtSource.getText().toString().trim();
            String montantStr = edtMontant.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();

            if (source.isEmpty() || montantStr.isEmpty()) return;

            double montant = Double.parseDouble(montantStr);
            Revenu revenu = new Revenu(source, montant,
                    calendar.getTimeInMillis(), description, System.currentTimeMillis());
            viewModel.insert(revenu);

            requireActivity().finish();
        });

        return view;
    }
}
