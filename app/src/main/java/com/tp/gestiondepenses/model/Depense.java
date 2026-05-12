package com.tp.gestiondepenses.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "depenses",
        foreignKeys = {
                @ForeignKey(entity = User.class,
                        parentColumns = "id",
                        childColumns = "user_id",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Categorie.class,
                        parentColumns = "id",
                        childColumns = "categorie_id",
                        onDelete = ForeignKey.RESTRICT),
                @ForeignKey(entity = Rubrique.class,
                        parentColumns = "id",
                        childColumns = "rubrique_id",
                        onDelete = ForeignKey.SET_NULL)
        },
        indices = {
                @Index("user_id"),
                @Index("categorie_id"),
                @Index("rubrique_id")
        })
public class Depense implements Transaction {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "categorie_id")
    private int categorieId;

    @ColumnInfo(name = "rubrique_id")
    private Integer rubriqueId;

    private double montant;
    private long date;
    private String description;

    @ColumnInfo(name = "moyen_paiement")
    private String moyenPaiement;

    @ColumnInfo(name = "created_at")
    private long createdAt;

    public Depense() {
        this.createdAt = System.currentTimeMillis();
    }

    @Ignore
    public Depense(int userId, int categorieId, Integer rubriqueId, double montant, long date, String description, String moyenPaiement) {
        this.userId = userId;
        this.categorieId = categorieId;
        this.rubriqueId = rubriqueId;
        this.montant = montant;
        this.date = date;
        this.description = description;
        this.moyenPaiement = moyenPaiement;
        this.createdAt = System.currentTimeMillis();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getCategorieId() { return categorieId; }
    public void setCategorieId(int categorieId) { this.categorieId = categorieId; }
    public Integer getRubriqueId() { return rubriqueId; }
    public void setRubriqueId(Integer rubriqueId) { this.rubriqueId = rubriqueId; }

    @Override public double getMontant() { return montant; }
    public void setMontant(double montant) { this.montant = montant; }
    @Override public long getDate() { return date; }
    public void setDate(long date) { this.date = date; }
    @Override public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getMoyenPaiement() { return moyenPaiement; }
    public void setMoyenPaiement(String moyenPaiement) { this.moyenPaiement = moyenPaiement; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    @Override public String getType() { return "DEPENSE"; }

    @Override
    public String getIconName() { return "ic_category"; }

    @Override
    public String getCategoryName() { return "Dépense"; }
}
