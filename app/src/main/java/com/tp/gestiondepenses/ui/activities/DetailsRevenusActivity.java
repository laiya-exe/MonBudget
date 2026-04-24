package com.tp.gestiondepenses.ui.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.model.Revenu;
import com.tp.gestiondepenses.viewmodel.RevenuViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DetailsRevenusActivity extends AppCompatActivity {

    private RevenuViewModel revenuViewModel;
    private TextView txtResume;
    private Button btnRetour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_revenus);

        txtResume = findViewById(R.id.txtResume);
        btnRetour = findViewById(R.id.btnRetour);

        revenuViewModel = new ViewModelProvider(this).get(RevenuViewModel.class);

        // Observer les revenus et afficher le plus rentable
        revenuViewModel.getAllRevenus().observe(this, revenus -> {
            if (revenus != null && !revenus.isEmpty()) {
                Revenu maxRevenu = revenus.get(0);
                for (Revenu r : revenus) {
                    if (r.getMontant() > maxRevenu.getMontant()) {
                        maxRevenu = r;
                    }
                }

                // Format mois + année
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(maxRevenu.getDate());
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
                String moisAnnee = sdf.format(cal.getTime());

                // Texte stylisé
                String resumeText = "Revenu le plus rentable :\n"
                        + maxRevenu.getSource() + " : "
                        + maxRevenu.getMontant() + " FCFA\n"
                        + "Période : " + moisAnnee;

                txtResume.setText(resumeText);
            } else {
                txtResume.setText("Aucun revenu enregistré.");
            }
        });

        // Bouton retour
        btnRetour.setOnClickListener(v -> finish());
    }
}
