package com.tp.gestiondepenses.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.model.Transaction;
import com.tp.gestiondepenses.utils.DateUtils;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private List<Transaction> transactions;

    public void submitList(List<Transaction> list) {
        this.transactions = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction_dashboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction t = transactions.get(position);
        holder.tvDescription.setText(t.getDescription());
        holder.tvMontant.setText(String.format("%.0f FCFA", t.getMontant()));
        holder.tvDate.setText(DateUtils.formatDate(t.getDate())); // à créer
        holder.tvType.setText(t.getType());
    }

    @Override
    public int getItemCount() {
        return transactions == null ? 0 : transactions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescription, tvMontant, tvDate, tvType;
        ViewHolder(View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvMontant = itemView.findViewById(R.id.tv_montant);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvType = itemView.findViewById(R.id.tv_type);
        }
    }
}