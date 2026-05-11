package com.tp.gestiondepenses.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.database.AppDatabase;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
