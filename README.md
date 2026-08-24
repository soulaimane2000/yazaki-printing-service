# Printing Service

Backend Spring Boot de démonstration pour orchestrer des impressions `LABEL` et `DOCUMENT`. Toutes les imprimantes et données du profil `dev` sont fictives. Les adapters ne contactent aucune infrastructure réelle.

## Prérequis

- Java 21
- Docker Desktop (pour PostgreSQL local)

## Démarrage local

```powershell
Copy-Item .env.example .env
docker compose --env-file .env up -d postgres
$env:SPRING_PROFILES_ACTIVE='dev'
.\mvnw.cmd spring-boot:run
```

L’application écoute par défaut sur `http://localhost:8080`.

- Swagger UI : `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON : `http://localhost:8080/v3/api-docs`
- Health : `http://localhost:8080/actuator/health`

## Frontend React

Le frontend se trouve dans `frontend/` et nécessite une version LTS récente de Node.js.

```powershell
cd frontend
Copy-Item .env.example .env
npm install
npm run dev
```

Interface : `http://localhost:5173`. En développement, Vite transmet `/api` et `/actuator` vers `http://localhost:8080`.

Pour une URL backend différente, définir `VITE_API_BASE_URL` dans `frontend/.env`. L’origine frontend autorisée côté Spring Boot se configure avec `FRONTEND_ALLOWED_ORIGIN`.

Le logo utilisé se trouve dans `docs/assets/yazaki-logo.png`. Remplacer ce fichier uniquement par un asset validé, en conservant son nom et ses proportions.

## Tests

Les tests d’intégration utilisent un vrai PostgreSQL éphémère via Testcontainers et nécessitent donc Docker.

```powershell
.\mvnw.cmd test
.\mvnw.cmd verify
```

## Démonstration

Le profil `dev` initialise des imprimantes et des jobs explicitement fictifs lorsque la base est vide. Désactivation : `DEMO_DATA_ENABLED=false`.

Les adapters simulés se configurent avec `FAKE_LABEL_MODE` et `FAKE_DOCUMENT_MODE` : `SUCCESS`, `TIMEOUT`, `OFFLINE` ou `PERMANENT_ERROR`. Un succès d’adapter produit `SUBMITTED`, jamais `PRINTED`, faute de confirmation physique.

Le protocole réel des Label Printers et la configuration réelle de CUPS sont **À confirmer avec l’encadrant**.
