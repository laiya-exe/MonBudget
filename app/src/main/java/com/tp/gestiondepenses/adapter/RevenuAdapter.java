package com.tp.gestiondepenses.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.model.Revenu;
import com.tp.gestiondepenses.viewmodel.RevenuViewModel;

import java.util.List;

public class RevenuAdapter extends RecyclerView.Adapter<RevenuAdapter.ViewHolder> {
    private List<Revenu> revenus;
    private RevenuViewModel viewModel;

    public RevenuAdapter(List<Revenu> revenus, RevenuViewModel viewModel) {
        this.revenus = revenus;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_revenu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Revenu revenu = revenus.get(position);
        holder.txtSource.setText(revenu.source);
        holder.txtMontant.setText(revenu.montant + " FCFA");
        holder.txtDetails.setText(revenu.description);

        // Suppression via clic long
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Supprimer revenu")
                    .setMessage("Êtes-vous sûr de vouloir supprimer ce revenu ?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        viewModel.delete(revenu);
                        Toast.makeText(v.getContext(), "Revenu supprimé avec succès", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Non", null)
                    .show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return revenus.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSource, txtMontant, txtDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSource = itemView.findViewById(R.id.txtSource);
            txtMontant = itemView.findViewById(R.id.txtMontant);
            txtDetails = itemView.findViewById(R.id.txtDetails);
        }
    }
}
