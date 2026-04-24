package com.tp.gestiondepenses.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

/**
 * Entité représentant un revenu.
 */
@Entity(tableName = "revenus")
public class Revenu {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String source;
    private double montant;
    private long date;
    private String description;
    private long created_at;

    // Constructeur vide requis par Room
    public Revenu() {}

    // Constructeur complet ignoré par Room
    @Ignore
    public Revenu(String source, double montant, long date, String description, long created_at) {
        this.source = source;
        this.montant = montant;
        this.date = date;
        this.description = description;
        this.created_at = created_at;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public double getMontant() { return montant; }
    public void setMontant(double montant) { this.montant = montant; }

    public long getDate() { return date; }
    public void setDate(long date) { this.date = date; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public long getCreated_at() { return created_at; }
    public void setCreated_at(long created_at) { this.created_at = created_at; }
}
