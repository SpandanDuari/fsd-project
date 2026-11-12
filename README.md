# HireTrack

HireTrack is a recruitment platform for candidates, recruiters, and administrators. Candidates can create profiles, upload resumes, discover jobs, and apply. Recruiters can publish jobs and review applications. Administrators can monitor platform-wide candidates, recruiters, jobs, and applications.


## Technology stack

### Frontend

- React 18
- Vite
- React Router
- Axios
- Tailwind CSS
- Lucide React icons
- Recharts for dashboard visualizations
- Netlify for hosting and continuous deployment

### Backend

- Java 17 source compatibility
- Spring Boot 3.5
- Spring Web
- Spring Data JPA and Hibernate
- Spring Security
- JSON Web Tokens (JWT)
- Bean Validation
- Flyway database migrations
- Apache PDFBox for PDF resume text extraction
- Maven
- Docker

### Data and infrastructure

- PostgreSQL
- Neon PostgreSQL for the deployed database
- Render for the Dockerized backend
- Netlify for the React frontend

## System architecture

```text
Browser
  |
  | HTTPS
  v
Netlify - React/Vite single-page application
  |
  | Axios REST calls with Bearer JWT
  v
Render - Spring Boot API container
  |
  | JDBC + Flyway
  v
Neon PostgreSQL
```

The frontend is responsible for routing, forms, role-aware navigation, API calls, and user-facing state. The backend owns authentication, authorization, validation, business rules, resume parsing, and persistence. PostgreSQL is the source of truth for users, profiles, companies, jobs, applications, interviews, and notifications.

## Repository structure

```text
.
├── frontend/
│   ├── src/
│   │   ├── components/       Shared layout and state components
│   │   ├── context/          Authentication and toast contexts
│   │   ├── lib/              Axios API client
│   │   ├── pages/            Landing, auth, jobs, dashboards, and profile pages
│   │   ├── main.jsx          React Router and application bootstrap
│   │   └── index.css         Global Tailwind/CSS styles
│   ├── package.json
│   ├── netlify.toml
│   └── .env.example
├── src/main/java/com/hiretrack/
│   ├── model/                JPA entities
│   ├── repo/                 Spring Data repositories
│   ├── service/              Shared domain services
│   ├── security/             JWT filter, JWT service, and security rules
│   └── web/                  REST controllers and DTOs
├── src/main/resources/
│   ├── application.properties
│   └── db/migration/         Versioned Flyway SQL migrations
├── scripts/
│   ├── Run-HireTrackBackend.ps1
│   └── Run-HireTrackWithNeon.ps1
├── Dockerfile
├── render.yaml
├── netlify.toml
├── pom.xml
└── DEPLOYMENT.md
```

## Functional areas

### Candidate workflow

1. Select **Sign up as candidate**.
2. Provide personal details, location, skills, email, and password.
3. Optionally upload a PDF or text resume.
4. Review or edit profile details and resume autofill suggestions.
5. Browse public job listings.
6. Open a job and apply when authenticated.
7. Existing applications are detected and displayed as **Already applied**.
8. Review submitted applications from the Applications page.

### Recruiter workflow

1. Select **Sign up as recruiter**.
2. Provide recruiter and company information.
3. Open **Post a job**.
4. Publish a job with title, location, employment type, salary, and description.
5. Review applications in the Applications page.

### Administrator workflow

1. Sign in with an administrator account.
2. View platform counts for candidates, applications, jobs, and recruiters.
3. Open **Candidate accounts** to manage candidate access.
4. Open **Recruiter accounts** to manage recruiter access.
5. Review recent candidates, including application counts.
6. Review recent applications and their statuses.
7. Use the role-specific navigation to access jobs and applications.

From either account list, an administrator can:

- Activate or deactivate an account. Deactivated users cannot sign in and existing JWTs are rejected.
- Generate a secure temporary password. The temporary password is displayed once in the admin UI and should be shared securely.
- Permanently delete a candidate or recruiter account after confirmation. Candidate applications are removed with the candidate record; recruiter job history is preserved.

## Authentication and authorization

Authentication uses JWTs:

1. The frontend submits credentials to `/api/auth/login`.
2. The backend validates the password and returns a JWT and role.
3. The frontend stores the token locally for the current browser session.
4. Axios adds `Authorization: Bearer <token>` to authenticated requests.
5. Spring Security and `JwtFilter` enforce access rules.

Supported roles:

- `CANDIDATE`: profile, resume, applications, and job applications
- `RECRUITER`: job creation and hiring workspace access
- `ADMIN`: platform-wide administration and reporting

Never commit passwords, JWT secrets, Neon connection strings, or production credentials. The development admin seed is documented separately in [DEMO_CREDENTIALS.md](./DEMO_CREDENTIALS.md) and must be changed or removed before production use.

## REST API overview

All API routes are prefixed with `/api`.

### Public routes

| Method | Route | Purpose |
|---|---|---|
| `GET` | `/health` | Service health check |
| `POST` | `/auth/register` | Register a candidate or recruiter |
| `POST` | `/auth/login` | Authenticate a user |
| `GET` | `/jobs` | List or search jobs |
| `GET` | `/jobs/{id}` | View a job |

### Authenticated routes

| Method | Route | Purpose |
|---|---|---|
| `GET` | `/applications` | Candidate applications or all applications for staff |
| `POST` | `/applications` | Submit a candidate application |
| `PATCH` | `/applications/{id}/status` | Update an application status |
| `GET` | `/profile` | Read the current profile |
| `PUT` | `/profile` | Save the current profile |
| `POST` | `/profile/resume` | Upload and parse a resume |
| `GET` | `/notifications` | Read notifications |
| `PATCH` | `/notifications/{id}/read` | Mark a notification as read |
| `GET` | `/interviews` | List interviews |
| `POST` | `/interviews` | Create an interview |
| `PUT` | `/interviews/{id}` | Update an interview |

### Recruiter routes

| Method | Route | Purpose |
|---|---|---|
| `POST` | `/jobs` | Publish a job |
| `PUT` | `/jobs/{id}` | Update a job |
| `DELETE` | `/jobs/{id}` | Delete a job |

### Administrator routes

| Method | Route | Purpose |
|---|---|---|
| `GET` | `/admin/summary` | Platform-wide record counts |
| `GET` | `/admin/candidates` | Candidate account list with application counts and status |
| `GET` | `/admin/recruiters` | Recruiter account list with company details and status |
| `PATCH` | `/admin/accounts/{id}/status` | Activate or deactivate a candidate/recruiter account |
| `POST` | `/admin/accounts/{id}/reset-password` | Generate a temporary password |
| `DELETE` | `/admin/accounts/{id}` | Permanently delete a candidate/recruiter account |
| `GET` | `/dashboard/admin` | Administrative dashboard metrics |

See [API_DOCUMENTATION.md](./API_DOCUMENTATION.md) for additional request and response details.

## Database design

Flyway applies migrations in order during backend startup:

- `V1`: Core users, companies, recruiters, candidates, jobs, and applications
- `V2`: Initial demo data
- `V3`: API support tables such as profiles, notifications, and interviews
- `V4`: API demo records
- `V5`: Demo activity
- `V6`: Resume/profile fields
- `V7`: Development admin seed
- `V8`: Development admin password correction
- `V9`: Account activation/deactivation state

Important relationships:

- A user may be linked to one candidate or recruiter record.
- A recruiter belongs to a company.
- A job belongs to a company.
- An application links one candidate to one job.
- A candidate cannot apply to the same job twice.
- Interviews reference applications.
- Profiles reference users.

Do not reset or overwrite a shared database. Flyway migrations should be additive and versioned.

## Local setup

### Prerequisites

- Java 17 or newer
- Maven 3.9+
- Node.js 22+
- npm
- PostgreSQL 15+

### Configure the backend

Create a local database and application user, then set variables in PowerShell. Do not commit the password:

```powershell
$env:DB_HOST = "localhost"
$env:DB_PORT = "5432"
$env:DB_NAME = "hiretrack_db"
$env:DB_USERNAME = "hiretrack_app"
$env:DB_PASSWORD = "<local-password>"
$env:JWT_SECRET = "<local-secret-at-least-32-characters>"
```

Alternatively, copy the example files and fill them locally:

```powershell
Copy-Item .env.example .env
Copy-Item frontend\.env.example frontend\.env.local
```

The backend uses Flyway automatically. With a JDBC URL, use:

```powershell
$env:DB_URL = "jdbc:postgresql://localhost:5432/hiretrack_db"
```

### Start the backend

```powershell
mvn spring-boot:run
```

Or use the helper script:

```powershell
.\scripts\Run-HireTrackBackend.ps1
```

The API runs at `http://localhost:8080`.

### Start the frontend

```powershell
Set-Location frontend
npm install
npm run dev
```

The frontend runs at `http://localhost:5173`.

Set the frontend API URL when needed:

```powershell
$env:VITE_API_BASE_URL = "http://localhost:8080/api"
```

### Run with Neon locally

After authenticating with the Neon CLI and generating `.env.local`:

```powershell
.\scripts\Run-HireTrackWithNeon.ps1
```

The script reads local Neon variables privately and maps them to Spring Boot variables. It does not print or commit connection strings.

## Build and test

Frontend production build:

```powershell
Set-Location frontend
npm run build
```

Frontend test command:

```powershell
npm test
```

Backend package build:

```powershell
Set-Location ..
mvn -DskipTests package
```

Backend tests:

```powershell
mvn test
```

The integration test requires a reachable PostgreSQL database configured through the environment variables. A local compile/package check can be used when no test database is available:

```powershell
mvn -DskipTests package
```

## Deployment

### Neon

Neon hosts the production PostgreSQL database. The Render backend uses a JDBC URL with SSL:

```text
jdbc:postgresql://<neon-host>:5432/<database>?sslmode=require
```

Configure these values in Render, not in Git:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET`
- `FRONTEND_URL`

Flyway runs against Neon when the Render container starts.

### Render

Render builds the root [Dockerfile](./Dockerfile), starts the Spring Boot jar, and exposes the configured `PORT`. The service must have the Neon variables above. If `DB_URL` is missing, Spring Boot falls back to local development settings and the deployment will fail because Render cannot connect to `localhost:5432`.

### Netlify

Netlify builds the `frontend` directory:

- Base directory: `frontend`
- Build command: `npm run build`
- Publish directory: `dist`
- Environment variable: `VITE_API_BASE_URL=https://hiretrack-p65u.onrender.com/api`

The SPA redirect in [netlify.toml](./netlify.toml) routes unknown frontend paths to `index.html`.

## Team development guidelines

- Keep frontend features in `frontend/src`.
- Keep API controllers in `src/main/java/com/hiretrack/web`.
- Keep persistence models in `model` and repositories in `repo`.
- Add database changes as new Flyway migrations; do not edit an applied migration.
- Keep secrets in environment variables or the deployment provider secret store.
- Test the smallest affected build or endpoint before opening a pull request.
- Verify role permissions for every new authenticated endpoint.
- Account-management operations are restricted to administrators and destructive actions require confirmation.
- Do not commit `node_modules`, `dist`, `target`, `.env`, or `.env.local`.

## Known production considerations

- The free Render instance can sleep and produce a cold-start delay.
- Resume text is currently stored in the database for demo purposes; object storage is preferable for production-scale files.
- The seeded development admin account must be replaced with a secure production account.
- CORS should be restricted to the final Netlify domain for production hardening.
- The frontend bundle currently emits a Vite chunk-size warning; this is non-blocking but can be improved with code splitting.

## Additional documentation

- [DEPLOYMENT.md](./DEPLOYMENT.md) - Deployment-specific instructions
- [API_DOCUMENTATION.md](./API_DOCUMENTATION.md) - API reference
- [ENVIRONMENT_SETUP_REPORT.md](./ENVIRONMENT_SETUP_REPORT.md) - Local environment setup record
- [DEMO_CREDENTIALS.md](./DEMO_CREDENTIALS.md) - Development-only demo accounts
- [TEST_REPORT.md](./TEST_REPORT.md) - Verification history
- [PROGRESS.md](./PROGRESS.md) - Feature progress
