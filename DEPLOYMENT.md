# HireTrack Deployment

## Architecture

- React/Vite frontend: Netlify
- Spring Boot API: Render
- PostgreSQL: Neon

No external deployment is performed by this workspace without explicit authorization.

## Neon

1. Create a Neon project and database.
2. Copy the pooled PostgreSQL connection details.
3. Use an SSL JDBC URL, for example:

```text
jdbc:postgresql://<host>/<database>?sslmode=require
```

4. Keep the username and password in Render environment variables only.

For local development after `neon deploy`, use:

```powershell
.\scripts\Run-HireTrackWithNeon.ps1
```

This reads the ignored `.env.local` file in memory and maps Neon’s `DATABASE_URL`
to the Spring Boot JDBC settings. It does not print or commit the connection string.

## Render

1. Push this repository to GitHub.
2. Create a Render Web Service from the repository.
3. Use the included `Dockerfile` or `render.yaml`.
4. Configure these environment variables:

```text
PORT=8080
DB_URL=jdbc:postgresql://<neon-host>/<database>?sslmode=require
DB_USERNAME=<neon-user>
DB_PASSWORD=<neon-password>
JWT_SECRET=<at-least-32-character-random-secret>
FRONTEND_URL=https://<netlify-site>.netlify.app
```

5. Deploy and verify `https://<render-service>/api/health`.

## Netlify

1. Create a site from the GitHub repository.
2. Set the base directory to `frontend`.
3. Base directory: `frontend`
4. Build command: `npm run build`
5. Publish directory: `frontend/dist`
5. Set:

```text
VITE_API_BASE_URL=https://<render-service>/api
```

6. Deploy and test browser refreshes on `/jobs`, `/login`, and dashboard routes.

## GitHub

```powershell
git init
git add .
git commit -m "Complete HireTrack application"
git branch -M main
git remote add origin https://github.com/<account>/<repository>.git
git push -u origin main
```

Do not commit `.env`, passwords, JWT secrets, private keys, or production connection strings.
