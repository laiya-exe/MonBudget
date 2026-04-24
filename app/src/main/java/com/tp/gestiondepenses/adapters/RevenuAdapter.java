package com.tp.gestiondepenses.adapters;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.model.Revenu;
import com.tp.gestiondepenses.viewmodel.RevenuViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RevenuAdapter extends RecyclerView.Adapter<RevenuAdapter.RevenuViewHolder> {

    private List<Revenu> revenus;
    private RevenuViewModel revenuViewModel;
    private double totalRevenus;

    public RevenuAdapter(List<Revenu> revenus, RevenuViewModel revenuViewModel) {
        this.revenus = revenus;
        this.revenuViewModel = revenuViewModel;

        totalRevenus = 0;
        for (Revenu r : revenus) {
            totalRevenus += r.getMontant();
        }
    }

    @NonNull
    @Override
    public RevenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_revenu, parent, false);
        return new RevenuViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RevenuViewHolder holder, int position) {
        Revenu revenu = revenus.get(position);

        holder.txtSource.setText(revenu.getSource());
        holder.txtMontant.setText(revenu.getMontant() + " FCFA");
        holder.txtDetails.setText(revenu.getDescription());

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(revenu.getDate());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        holder.txtDate.setText("Ajouté le : " + sdf.format(cal.getTime()));

        if (totalRevenus > 0) {
            double pourcentage = (revenu.getMontant() / totalRevenus) * 100;
            holder.txtPourcentage.setText(String.format(Locale.getDefault(), "%.1f%%", pourcentage));
        } else {
            holder.txtPourcentage.setText("0%");
        }

        // Choisir l’icône selon la source
        switch (revenu.getSource().toLowerCase()) {
            case "salaire":
                holder.imgSourceIcon.setImageResource(R.drawable.ic_salary);
                break;
            case "commerce":
                holder.imgSourceIcon.setImageResource(R.drawable.ic_commerce);
                break;
            case "freelance":
                holder.imgSourceIcon.setImageResource(R.drawable.ic_freelance);
                break;
            case "don":
                holder.imgSourceIcon.setImageResource(R.drawable.ic_donation);
                break;
            default:
                holder.imgSourceIcon.setImageResource(R.drawable.ic_income_default);
                break;
        }

        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Supprimer revenu")
                    .setMessage("Êtes-vous sûr de vouloir supprimer ce revenu ?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        revenuViewModel.deleteById(revenu.getId());
                        Toast.makeText(v.getContext(), "Revenu supprimé", Toast.LENGTH_SHORT).show();
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

    public static class RevenuViewHolder extends RecyclerView.ViewHolder {
        TextView txtSource, txtMontant, txtDetails, txtPourcentage, txtDate;
        ImageView imgSourceIcon;

        public RevenuViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSource = itemView.findViewById(R.id.txtSource);
            txtMontant = itemView.findViewById(R.id.txtMontant);
            txtDetails = itemView.findViewById(R.id.txtDetails);
            txtPourcentage = itemView.findViewById(R.id.txtPourcentage);
            txtDate = itemView.findViewById(R.id.txtDate);
            imgSourceIcon = itemView.findViewById(R.id.imgSourceIcon);
        }
    }
}
