# HireTrack

HireTrack is a recruitment platform with a Spring Boot/PostgreSQL API and a React/Vite frontend.

## Local development

Set the backend variables without committing the password:

```powershell
$env:DB_HOST = "localhost"
$env:DB_PORT = "5432"
$env:DB_NAME = "hiretrack_db"
$env:DB_USERNAME = "hiretrack_app"
$env:DB_PASSWORD = "<local-password>"
$env:JWT_SECRET = "<local-secret>"
```

Run the backend:

```powershell
mvn test
mvn spring-boot:run
```

Run the frontend:

```powershell
cd frontend
npm install
npm run dev
```

Open `http://localhost:5173`.

See [DEPLOYMENT.md](./DEPLOYMENT.md) for Netlify, Render, and Neon preparation.
