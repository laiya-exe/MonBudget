package com.tp.gestiondepenses.adapter;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RevenuAdapter extends RecyclerView.Adapter<RevenuAdapter.RevenuViewHolder> {

    private List<Revenu> revenus = new ArrayList<>();
    private final OnItemClickListener listener;
    private RevenuViewModel viewModel;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);

    public interface OnItemClickListener {
        void onItemClick(Revenu revenu);
    }

    public RevenuAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setRevenus(List<Revenu> revenus) {
        this.revenus = revenus != null ? revenus : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setViewModel(RevenuViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public RevenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_revenu, parent, false);
        return new RevenuViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RevenuViewHolder holder, int position) {
        Revenu revenu = revenus.get(position);

        holder.tvSource.setText(revenu.getSource());
        holder.tvAmount.setText(String.format(Locale.FRENCH, "+%,.0f FCFA", revenu.getMontant()).replace(',', '.'));
        
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(revenu.getDate());
        holder.tvDate.setText(sdf.format(cal.getTime()));
        
        // Icône dynamique selon la source
        String sourceLower = revenu.getSource().toLowerCase();
        if (sourceLower.contains("salaire")) {
            holder.ivIcon.setImageResource(R.drawable.ic_salaryy);
        } else if (sourceLower.contains("freelance")) {
            holder.ivIcon.setImageResource(R.drawable.ic_freelance);
        } else if (sourceLower.contains("commerce") || sourceLower.contains("vente")) {
            holder.ivIcon.setImageResource(R.drawable.ic_commerce);
        } else if (sourceLower.contains("don") || sourceLower.contains("cadeau")) {
            holder.ivIcon.setImageResource(R.drawable.ic_donation);
        } else {
            holder.ivIcon.setImageResource(R.drawable.ic_income_default);
        }

        // Si une description est présente, elle remplace "COMPTE PRINCIPAL"
        if (revenu.getDescription() != null && !revenu.getDescription().trim().isEmpty()) {
            holder.tvAccount.setText(revenu.getDescription());
        } else {
            holder.tvAccount.setText("COMPTE PRINCIPAL");
        }

        // Clic simple pour modifier
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(revenu);
            }
        });

        // Clic long pour supprimer avec confirmation
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Supprimer le revenu")
                    .setMessage("Êtes-vous sûr de vouloir supprimer ce revenu ?")
                    .setPositiveButton("Oui, supprimer", (dialog, which) -> {
                        if (viewModel != null) {
                            viewModel.delete(revenu);
                            Toast.makeText(v.getContext(), "Revenu supprimé", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss())
                    .show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return revenus.size();
    }

    public static class RevenuViewHolder extends RecyclerView.ViewHolder {
        TextView tvSource, tvAmount, tvDate, tvAccount;
        ImageView ivIcon;

        public RevenuViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSource = itemView.findViewById(R.id.tvSource);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvAccount = itemView.findViewById(R.id.tvAccount);
            ivIcon = itemView.findViewById(R.id.ivIcon);
        }
    }
}