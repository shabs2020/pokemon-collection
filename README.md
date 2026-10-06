# Pokémon Collection App

Fullstack app: trainers log in/out, browse Pokémon (via PokéAPI), and maintain a personal collection.

## Prerequisites
- Java 21
- Node 22+
- npm

## Setup

### Environment variables (required)
Copy the template to `.env` at the project root and fill in real values. The backend will fail to start without these:
```bash
cp .env.template .env
```
For the frontend, also copy `frontend/.env.example` to `frontend/.env`:
```bash
cp frontend/.env.example frontend/.env
```
Variables (see `.env.template` for details):
- `APP_JWT_SECRET` — JWT signing secret (required by backend)
- `APP_CORS_ALLOWED_ORIGINS` — allowed CORS origins (required by backend)
- `VITE_API_BASE_URL` — backend API base URL (required by frontend)

> Note: Spring Boot does not auto-load a root `.env`. Use `./run.sh` (which loads `.env` automatically) or export the variables manually before `./mvnw spring-boot:run`.

### Backend
```bash
cd pokemon
./mvnw spring-boot:run
```
- Runs on `http://localhost:8080`
- H2 console: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:pokemon`)

### Frontend
```bash
cd frontend
npm install
npm run dev
```
- Runs on `http://localhost:5173`

### Run both together
```bash
./run.sh
```

## Demo Trainers
- Username: `ash`, Password: `pikachu123`
- Username: `misty`, Password: `starmie123`

## Notes
- PokéAPI is called directly from the browser (requires internet).
- Trainers can only see their own collection.

## Implementation Time

Approx. 2 hours total, with AI assistance (Mistral AI) used for code generation and reviewed/adapted manually.

| Area | Time |
|------|------|
| Backend: Spring Boot project setup, JWT auth, collection CRUD, H2 schema, exception handling | ~25 min |
| Frontend: Vite + Vue 3 setup, login, PokéAPI browse, collection UI, Pinia stores | ~25 min |
| Testing & Enhancements: manual end-to-end testing, verification and enhancements | ~50 min |
| Project wiring: `.gitignore`, env config, `run.sh`, README, repo setup | ~20 min |