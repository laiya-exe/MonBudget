package com.tp.gestiondepenses.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "depenses")
public class Depense {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String categorie;
    private double montant;
    private long date;
    private String description;
    private String moyenPaiement;
    private long created_at;

    // Constructeur par défaut (obligatoire pour Room)
    public Depense() {}

    // Constructeur avec tous les champs sauf id
    public Depense(String categorie, double montant, long date, String description, String moyenPaiement) {
        this.categorie = categorie;
        this.montant = montant;
        this.date = date;
        this.description = description;
        this.moyenPaiement = moyenPaiement;
        this.created_at = System.currentTimeMillis();
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    public double getMontant() { return montant; }
    public void setMontant(double montant) { this.montant = montant; }
    public long getDate() { return date; }
    public void setDate(long date) { this.date = date; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getMoyenPaiement() { return moyenPaiement; }
    public void setMoyenPaiement(String moyenPaiement) { this.moyenPaiement = moyenPaiement; }
    public long getCreated_at() { return created_at; }
    public void setCreated_at(long created_at) { this.created_at = created_at; }
}