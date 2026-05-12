# MonBudget

**MonBudget** est une application Android de gestion des finances personnelles permettant de suivre ses dépenses, de gérer ses revenus et de définir des budgets par catégorie. Développée en Java, elle suit les principes de l'architecture moderne Android (MVVM).

---

## Fonctionnalités

- **Tableau de Bord** : Vue d'ensemble de la situation financière actuelle.
- **Gestion des Dépenses** : Enregistrement, modification et suivi détaillé des dépenses.
- **Gestion des Revenus** : Suivi des différentes sources de revenus.
- **Budgets Personnalisés** : Création de budgets par catégories avec indicateur de progression.
- **Catégorisation** : Organisation des transactions par catégories et rubriques.
- **Authentification** : Système de connexion et d'inscription sécurisé.
- **Navigation Fluide** : Utilisation d'une barre de navigation inférieure (Bottom Navigation) pour un accès rapide.

---

## Stack Technique

- **Langage** : Java (JDK 11+)
- **Architecture** : MVVM (Model-View-ViewModel)
- **Composants Jetpack** :
    - **Room** : Persistance des données locale (SQLite).
    - **ViewModel & LiveData** : Gestion du cycle de vie et données réactives.
    - **Navigation Component** : Gestion des flux de navigation et fragments.
    - **View Binding** : Interaction sécurisée avec les vues XML.
- **UI/UX** :
    - Material Design 3
    - RecyclerView & CardView
    - ConstraintLayout pour des interfaces flexibles.

---

## Structure du Projet

Le projet est organisé selon le pattern MVVM pour une meilleure maintenabilité :

- `ui/` : Activités, Fragments et Adapteurs pour l'affichage.
- `viewmodel/` : Logique métier et liaison entre la base de données et l'interface.
- `repository/` : Couche d'abstraction pour l'accès aux données.
- `model/` : Entités Room et modèles de données.
- `database/` : Configuration de la base de données Room et des DAOs.
- `utils/` : Classes utilitaires (formatage, constantes).

---

## Installation et Configuration

### 1. Prérequis
- Android Studio
- Android SDK 36 (targetSdk)
- Min SDK 23 (Android 5.0)

### 2. Cloner le projet
```bash
git clone https://github.com/laiya-exe/MonBudget.git
```

### 3. Build
Ouvrez le projet dans Android Studio et synchronisez Gradle. Les dépendances seront automatiquement téléchargées.


---

## Licence
Application réalisée dans le cadre d'un Projet Android.
