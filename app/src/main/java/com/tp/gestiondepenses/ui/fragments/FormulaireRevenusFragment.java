package com.tp.gestiondepenses.ui.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.model.Revenu;
import com.tp.gestiondepenses.viewmodel.RevenuViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Fragment affichant le formulaire d'ajout de revenus.
 */
public class FormulaireRevenusFragment extends Fragment {

    private EditText edtMontant, edtDescription, edtSourceAutre, edtDate;
    private Spinner spinnerSource;
    private Button btnAjouter;
    private Calendar calendar;
    private SimpleDateFormat sdf;
    private RevenuViewModel revenuViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_formulaire_revenus, container, false);

        edtMontant = root.findViewById(R.id.edtMontant);
        edtDescription = root.findViewById(R.id.edtDescription);
        edtSourceAutre = root.findViewById(R.id.edtSourceAutre);
        edtDate = root.findViewById(R.id.edtDate);
        spinnerSource = root.findViewById(R.id.spinnerSource);
        btnAjouter = root.findViewById(R.id.btnAjouter);

        revenuViewModel = new ViewModelProvider(requireActivity()).get(RevenuViewModel.class);

        // Spinner
        String[] sources = {"Salaire", "Commerce", "Freelance", "Don", "Autre"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, sources);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSource.setAdapter(adapterSpinner);

        spinnerSource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                edtSourceAutre.setVisibility(sources[position].equals("Autre") ? View.VISIBLE : View.GONE);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        // DatePicker
        calendar = Calendar.getInstance();
        sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        edtDate.setText(sdf.format(calendar.getTime()));

        edtDate.setOnClickListener(v -> {
            DatePickerDialog datePicker = new DatePickerDialog(requireContext(),
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        edtDate.setText(sdf.format(calendar.getTime()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            datePicker.show();
        });

        // Bouton Ajouter
        btnAjouter.setOnClickListener(v -> {
            String montantStr = edtMontant.getText().toString();
            String source = spinnerSource.getSelectedItem().toString();
            if (source.equals("Autre")) source = edtSourceAutre.getText().toString();
            String dateStr = edtDate.getText().toString();

            if (montantStr.isEmpty() || source.isEmpty() || dateStr.isEmpty()) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Champs obligatoires manquants")
                        .setMessage("Veuillez remplir Montant, Source et Date avant d’ajouter un revenu.")
                        .setPositiveButton("OK", null)
                        .show();
                return;
            }

            Revenu r = new Revenu(
                    source,
                    Double.parseDouble(montantStr),
                    calendar.getTimeInMillis(),
                    edtDescription.getText().toString(),
                    System.currentTimeMillis()
            );

            revenuViewModel.insert(r);

            Toast.makeText(requireContext(), "Revenu ajouté avec succès", Toast.LENGTH_SHORT).show();

            edtMontant.setText("");
            edtSourceAutre.setText("");
            edtDescription.setText("");
            edtDate.setText(sdf.format(Calendar.getInstance().getTime()));
        });

        return root;
    }
}
