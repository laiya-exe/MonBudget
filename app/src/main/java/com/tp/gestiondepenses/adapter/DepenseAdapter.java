package com.tp.gestiondepenses.adapter;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.model.Depense;
import com.tp.gestiondepenses.viewmodel.DepenseViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DepenseAdapter extends RecyclerView.Adapter<DepenseAdapter.DepenseViewHolder> {

    private List<Depense> depenses;
    private DepenseViewModel depenseViewModel;

    public DepenseAdapter(List<Depense> depenses, DepenseViewModel depenseViewModel) {
        this.depenses = depenses;
        this.depenseViewModel = depenseViewModel;
    }

    @NonNull
    @Override
    public DepenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_depense, parent, false);
        return new DepenseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DepenseViewHolder holder, int position) {
        Depense depense = depenses.get(position);

        holder.txtCategorie.setText(depense.getCategorie());
        holder.txtMontant.setText(depense.getMontant() + " FCFA");
        holder.txtDescription.setText(depense.getDescription());

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(depense.getDate());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        holder.txtDate.setText("Ajouté le : " + sdf.format(cal.getTime()));

        // Suppression avec confirmation
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Supprimer dépense")
                    .setMessage("Êtes-vous sûr de vouloir supprimer cette dépense ?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        depenseViewModel.deleteById(depense.getId());
                        Toast.makeText(v.getContext(), "Dépense supprimée", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Non", null)
                    .show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return depenses.size();
    }

    public static class DepenseViewHolder extends RecyclerView.ViewHolder {
        TextView txtCategorie, txtMontant, txtDescription, txtDate;

        public DepenseViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategorie = itemView.findViewById(R.id.txtCategorie);
            txtMontant = itemView.findViewById(R.id.txtMontant);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtDate = itemView.findViewById(R.id.txtDate);
        }
    }
}