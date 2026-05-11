package com.tp.gestiondepenses.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "budgets",
        foreignKeys = {
                @ForeignKey(entity = User.class,
                        parentColumns = "id",
                        childColumns = "user_id",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Categorie.class,
                        parentColumns = "id",
                        childColumns = "categorie_id",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {
                @Index("user_id"),
                @Index(value = {"user_id", "categorie_id", "mois", "annee"}, unique = true)
        })
public class Budget {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "categorie_id")
    private Integer categorieId; // Null pour le budget global
    
    @ColumnInfo(name = "montant_plafond")
    private double montantPlafond;

    private String periode;      // "MENSUEL"
    private int mois;
    private int annee;

    public Budget() {
        this.periode = "MENSUEL";
    }

    public Budget(int userId, Integer categorieId, double montantPlafond, String periode, int mois, int annee) {
        this.userId = userId;
        this.categorieId = categorieId;
        this.montantPlafond = montantPlafond;
        this.periode = periode;
        this.mois = mois;
        this.annee = annee;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public Integer getCategorieId() { return categorieId; }
    public void setCategorieId(Integer categorieId) { this.categorieId = categorieId; }

    public double getMontantPlafond() { return montantPlafond; }
    public void setMontantPlafond(double montantPlafond) { this.montantPlafond = montantPlafond; }

    public String getPeriode() { return periode; }
    public void setPeriode(String periode) { this.periode = periode; }

    public int getMois() { return mois; }
    public void setMois(int mois) { this.mois = mois; }

    public int getAnnee() { return annee; }
    public void setAnnee(int annee) { this.annee = annee; }
}
