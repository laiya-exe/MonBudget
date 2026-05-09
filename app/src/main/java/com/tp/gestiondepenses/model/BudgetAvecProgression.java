package com.tp.gestiondepenses.model;

public class BudgetAvecProgression {
    public Budget budget;
    public double montantDepense;   // total dépensé dans cette catégorie/mois
    public double pourcentage;      // (depense / plafond)*100
    public double restant;          // plafond - depense
    public String nomCategorie;     // pour affichage
    public String iconeCategorie;
    public String couleurCategorie;
    public boolean estDepasse;      // depense > plafond
    public boolean estCritique;     // pourcentage >= 80%

    public BudgetAvecProgression(Budget budget, double montantDepense, String nomCategorie, String iconeCategorie, String couleurCategorie) {
        this.budget = budget;
        this.montantDepense = montantDepense;
        this.nomCategorie = nomCategorie;
        this.iconeCategorie = iconeCategorie;
        this.couleurCategorie = couleurCategorie;
        this.pourcentage = budget.getMontantPlafond() > 0 ? (montantDepense / budget.getMontantPlafond()) * 100 : 0;
        this.restant = budget.getMontantPlafond() - montantDepense;
        this.estDepasse = montantDepense > budget.getMontantPlafond();
        this.estCritique = this.pourcentage >= 80;
    }
}