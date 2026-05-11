package com.tp.gestiondepenses.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.database.AppDatabase;

public class SettingsFragment extends Fragment {

    public static final String PREFS_NAME = "MonBudgetPrefs";
    public static final String KEY_BUDGET_ALERTS = "budget_alerts_enabled";

    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        MaterialSwitch switchBudgetAlerts = view.findViewById(R.id.switch_budget_alerts);
        
        // Charger l'état actuel (par défaut true)
        switchBudgetAlerts.setChecked(sharedPreferences.getBoolean(KEY_BUDGET_ALERTS, true));

        // Sauvegarder le changement
        switchBudgetAlerts.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean(KEY_BUDGET_ALERTS, isChecked).apply();
        });

        // Action de réinitialisation des données
        view.findViewById(R.id.btn_reset_data).setOnClickListener(v -> showResetConfirmationDialog());
    }

    private void showResetConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Réinitialiser les données ?")
                .setMessage("Êtes-vous sûr de vouloir supprimer tout votre historique financier ? Cette action est irréversible.")
                .setPositiveButton("Réinitialiser", (dialog, which) -> resetAllData())
                .setNegativeButton("Annuler", null)
                .setIcon(R.drawable.ic_warning)
                .show();
    }

    private void resetAllData() {
        AppDatabase db = AppDatabase.getInstance(requireContext());
        db.resetDatabase();
        
        Toast.makeText(requireContext(), "Données réinitialisées avec succès", Toast.LENGTH_SHORT).show();
        
        // Retourner à l'écran d'accueil après la réinitialisation
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }
}
