package com.tp.gestiondepenses.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.model.Transaction;
import com.tp.gestiondepenses.utils.DateUtils;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Transaction> transactions;

    private static final int TYPE_DEPENSE = 1;
    private static final int TYPE_REVENU = 2;

    public void submitList(List<Transaction> list) {
        this.transactions = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if ("REVENU".equals(transactions.get(position).getType())) {
            return TYPE_REVENU;
        }
        return TYPE_DEPENSE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_REVENU) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_revenu, parent, false);
            return new RevenuViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_depense, parent, false);
            return new DepenseViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Transaction t = transactions.get(position);
        if (holder instanceof DepenseViewHolder) {
            DepenseViewHolder h = (DepenseViewHolder) holder;
            h.tvDescription.setText(t.getDescription());
            h.tvMontant.setText(String.format("-%.0f FCFA", t.getMontant()));
            // On pourrait afficher la catégorie si on l'avait dans l'objet Transaction
            // Pour l'instant on met un texte par défaut ou on essaie de caster
        } else if (holder instanceof RevenuViewHolder) {
            RevenuViewHolder h = (RevenuViewHolder) holder;
            h.tvSource.setText(t.getDescription());
            h.tvAmount.setText(String.format("+%.0f FCFA", t.getMontant()));
            // h.tvDate.setText(DateUtils.formatDate(t.getDate()));
        }
    }

    @Override
    public int getItemCount() {
        return transactions == null ? 0 : transactions.size();
    }

    static class DepenseViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescription, tvMontant, tvCategorie;
        DepenseViewHolder(View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvMontant = itemView.findViewById(R.id.tvMontant);
            tvCategorie = itemView.findViewById(R.id.tvCategorie);
        }
    }

    static class RevenuViewHolder extends RecyclerView.ViewHolder {
        TextView tvSource, tvAmount, tvDate;
        RevenuViewHolder(View itemView) {
            super(itemView);
            tvSource = itemView.findViewById(R.id.tvSource);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}