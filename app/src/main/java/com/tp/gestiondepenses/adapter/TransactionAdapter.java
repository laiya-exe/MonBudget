package com.tp.gestiondepenses.adapter;

import android.content.Context;
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
import java.util.Locale;

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
        Context context = holder.itemView.getContext();
        
        int iconResId = context.getResources().getIdentifier(
                t.getIconName(), "drawable", context.getPackageName());

        if (holder instanceof DepenseViewHolder) {
            DepenseViewHolder h = (DepenseViewHolder) holder;
            h.tvDescription.setText(t.getDescription());
            h.tvMontant.setText(String.format(Locale.FRANCE, "-%,.0f FCFA", t.getMontant()));
            h.tvCategorie.setText(t.getCategoryName());
            
            if (iconResId != 0) {
                h.ivIcon.setImageResource(iconResId);
            } else {
                h.ivIcon.setImageResource(R.drawable.ic_category); // Icône par défaut
            }
            
        } else if (holder instanceof RevenuViewHolder) {
            RevenuViewHolder h = (RevenuViewHolder) holder;
            h.tvSource.setText(t.getCategoryName());
            h.tvAmount.setText(String.format(Locale.FRANCE, "+%,.0f FCFA", t.getMontant()));
            h.tvDate.setText(DateUtils.formatDate(t.getDate()));
            
            if (iconResId != 0) {
                h.ivIcon.setImageResource(iconResId);
            } else {
                h.ivIcon.setImageResource(R.drawable.ic_trending_up); // Icône par défaut revenus
            }
        }
    }

    @Override
    public int getItemCount() {
        return transactions == null ? 0 : transactions.size();
    }

    static class DepenseViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescription, tvMontant, tvCategorie;
        ImageView ivIcon;
        
        DepenseViewHolder(View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvMontant = itemView.findViewById(R.id.tvMontant);
            tvCategorie = itemView.findViewById(R.id.tvCategorie);
            ivIcon = itemView.findViewById(R.id.ivCategoryIcon);
        }
    }

    static class RevenuViewHolder extends RecyclerView.ViewHolder {
        TextView tvSource, tvAmount, tvDate;
        ImageView ivIcon;
        
        RevenuViewHolder(View itemView) {
            super(itemView);
            tvSource = itemView.findViewById(R.id.tvSource);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDate = itemView.findViewById(R.id.tvDate);
            ivIcon = itemView.findViewById(R.id.ivIcon);
        }
    }
}
