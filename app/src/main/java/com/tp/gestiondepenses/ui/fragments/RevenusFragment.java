package com.tp.gestiondepenses.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.adapter.RevenuAdapter;
import com.tp.gestiondepenses.model.Revenu;
import com.tp.gestiondepenses.ui.activities.DetailsRevenusActivity;
//import com.tp.gestiondepenses.ui.activities.FormulaireRevenusActivity;
import com.tp.gestiondepenses.viewmodel.RevenuViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RevenusFragment extends Fragment {

    private RecyclerView recyclerRevenus;
    private Button btnVoirDetails, btnAjouterRevenu;
    private Spinner spinnerMois;
    private TextView txtTotalRevenus;
    private RevenuAdapter revenuAdapter;
    private RevenuViewModel revenuViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_revenus, container, false);

        recyclerRevenus = view.findViewById(R.id.recyclerRevenus);
        btnVoirDetails = view.findViewById(R.id.btnVoirDetails);
        btnAjouterRevenu = view.findViewById(R.id.btnAjouterRevenu);
        spinnerMois = view.findViewById(R.id.spinnerMois);
        txtTotalRevenus = view.findViewById(R.id.txtTotalRevenus);

        recyclerRevenus.setLayoutManager(new LinearLayoutManager(getContext()));
        revenuAdapter = new RevenuAdapter(new ArrayList<>(), null);
        recyclerRevenus.setAdapter(revenuAdapter);

        revenuViewModel = new ViewModelProvider(requireActivity()).get(RevenuViewModel.class);

        // Observer revenus
        revenuViewModel.getAllRevenus().observe(getViewLifecycleOwner(), revenus -> {
            revenuAdapter = new RevenuAdapter(revenus, revenuViewModel);
            recyclerRevenus.setAdapter(revenuAdapter);

            // Calcul du total
            double total = 0;
            for (Revenu r : revenus) {
                total += r.getMontant();
            }
            txtTotalRevenus.setText("Total: " + total + " FCFA");

            // Générer la liste des mois dynamiquement
            List<String> moisList = new ArrayList<>();
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

            // Ajouter le mois courant
            moisList.add(sdf.format(cal.getTime()));

            // Ajouter les mois précédents si revenus existent
            for (Revenu r : revenus) {
                Calendar revCal = Calendar.getInstance();
                revCal.setTimeInMillis(r.getDate());
                String mois = sdf.format(revCal.getTime());
                if (!moisList.contains(mois)) {
                    moisList.add(mois);
                }
            }

            ArrayAdapter<String> moisAdapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_item, moisList);
            spinnerMois.setAdapter(moisAdapter);
        });

        // Bouton détails
        btnVoirDetails.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DetailsRevenusActivity.class);
            startActivity(intent);
        });

        // Bouton ajouter
        btnAjouterRevenu.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FormulaireRevenusActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
