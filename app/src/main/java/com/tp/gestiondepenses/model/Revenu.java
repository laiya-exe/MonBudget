package com.tp.gestiondepenses.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "revenus",
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "id",
                childColumns = "user_id",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("user_id")})
public class Revenu implements Transaction {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "user_id")
    private int userId;

    public String source;
    public double montant;
    public long date;
    public String description;
    public long created_at;

    public Revenu(int id, int userId, String source, double montant, long date, String description, long created_at) {
        this.id = id;
        this.userId = userId;
        this.source = source;
        this.montant = montant;
        this.date = date;
        this.description = description;
        this.created_at = created_at;
    }

    public Revenu() {}

    public Revenu(int userId, String source, double montant, long date, String description, long created_at) {
        this.userId = userId;
        this.source = source;
        this.montant = montant;
        this.date = date;
        this.description = description;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    @Override
    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    @Override
    public String getType() { return "REVENU"; }

    @Override
    public String getIconName() {
        return "ic_trending_up"; // Icône par défaut pour les revenus
    }

    @Override
    public String getCategoryName() {
        return source != null ? source : "Revenu";
    }
}
