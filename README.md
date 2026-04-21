# Application :  MonBudget

Projet TP Android - Application de gestion des dépenses personnelles

---

## Technologies utilisées

- Java
- Android Studio
- Gradle
- Firebase (jsp encore si ce sera utilisé)

---

##  Installation du projet pour vous 

### 1. Cloner le dépôt

```bash
git clone https://github.com/laiya-exe/MonBudget.git
````

### 2. Ouvrir dans Android Studio

* Lancer Android Studio
* Cliquer sur **File > Open**
* Sélectionner le dossier du projet que vous venez de cloner 

---

##  Configuration requise

### Android SDK

Assurez-vous d’avoir installé Android SDK via Android Studio.

Normalement `local.properties` est géneré par Android Studio automatiquement. Si besoin de configurer `local.properties` :

```properties
sdk.dir=/chemin/vers/votre/Android/Sdk
```

---

## Build et exécution

1. Synchroniser Gradle (Android Studio le fait automatiquement)
2. Attendre le téléchargement des dépendances

---

## Fichiers ignorés (important)

Ce projet ignore automatiquement :

* `.gradle/`
* `build/`
* `local.properties`
* `*.iml`
* `.idea/`
* `*.keystore`
* `google-services.json`

---

##  Collaboration

Pour éviter les conflits :

* Avant de commencer a travailler **(A CHAQUE FOIS)** :

```bash
git checkout main
git pull origin main
```

* Toujours créer une nouvelle branche **AVANT DE CODER** (branche pour fonctionalite de login par exemple) :

```bash
git checkout -b feature/login
```

* Faire des commits clairs :

```bash
git add .
git commit -m "Ajout de la fonctionnalité login"
```

* Pusher sur sa branche personnelle

```bash
git push origin feature/login
```
👉 Important : tu pushes ta branche, pas main.

---

* Créer la Pull Request sur GitHub


Aller sur https://github.com/laiya-exe/MonBudget . Tu verras un bouton : 👉 “pull request”. Cliquer dessus et remplir : Titre (ex: "Ajout login"). Description (ce que tu as fait). Cliquer : Create Pull Request

👉 Ensuite on s'entend que c'est bon avait de valider et fusionner avec la branche main

---

## 🧠 Workflow résumé

1. Pull main
2. Créer une branche
3. Coder
4. Commit
5. Push branche
6. Pull Request
7. Review
8. Merge
