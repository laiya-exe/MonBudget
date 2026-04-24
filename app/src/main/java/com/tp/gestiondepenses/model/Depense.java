package com.tp.gestiondepenses.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;

@Entity(tableName = "depenses",
        foreignKeys = {
                @ForeignKey(entity = Categorie.class,
                        parentColumns = "id",
                        childColumns = "categorie_id",
                        onDelete = ForeignKey.RESTRICT), // Empêche de supprimer une catégorie utilisée
                @ForeignKey(entity = Rubrique.class,
                        parentColumns = "id",
                        childColumns = "rubrique_id",
                        onDelete = ForeignKey.SET_NULL) // Met rubrique_id à NULL si la rubrique est supprimée
        })
public class Depense implements Transaction {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "categorie_id")
    private int categorieId;

    @ColumnInfo(name = "rubrique_id")
    private Integer rubriqueId; // Peut être null

    private double montant;

    private long date; // Stockage recommandé en timestamp (long)

    private String description;

    @ColumnInfo(name = "moyen_paiement")
    private String moyenPaiement;

    @ColumnInfo(name = "created_at")
    private long createdAt;

    // Constructeur
    public Depense(int categorieId, Integer rubriqueId, double montant, long date, String description, String moyenPaiement) {
        this.categorieId = categorieId;
        this.rubriqueId = rubriqueId;
        this.montant = montant;
        this.date = date;
        this.description = description;
        this.moyenPaiement = moyenPaiement;
        this.createdAt = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(int categorieId) {
        this.categorieId = categorieId;
    }

    public Integer getRubriqueId() {
        return rubriqueId;
    }

    public void setRubriqueId(Integer rubriqueId) {
        this.rubriqueId = rubriqueId;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMoyenPaiement() {
        return moyenPaiement;
    }

    public void setMoyenPaiement(String moyenPaiement) {
        this.moyenPaiement = moyenPaiement;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String getType() { return "DEPENSE"; }
}