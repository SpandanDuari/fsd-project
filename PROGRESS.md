# HireTrack Progress

## Current state

- [x] Inspected the existing workspace.
- [x] Confirmed PostgreSQL 17 is installed and running on port 5432.
- [x] Confirmed the existing project was a minimal Spring Boot/database scaffold.
- [x] Complete Spring Boot REST API, JWT security, role authorization, and demo data.
- [x] Complete React/Vite frontend and API integration.
- [x] Run backend and frontend simultaneously.
- [ ] Execute end-to-end candidate/recruiter/admin verification.
- [x] Validate production builds and deployment configuration.

## Current limitation

The backend has been started successfully with Flyway at version 5, and the health/jobs endpoints were verified. The final candidate registration request returned HTTP 403 during local verification and requires one more security-filter investigation before the end-to-end workflow can be marked complete.

## Verification commands

```powershell
# Backend
$env:DB_HOST = "localhost"
$env:DB_PORT = "5432"
$env:DB_NAME = "hiretrack_db"
$env:DB_USERNAME = "hiretrack_app"
$env:DB_PASSWORD = "<local-password>"
mvn test
mvn spring-boot:run

# Frontend
cd frontend
npm install
npm run build
npm run dev
```

No production deployment has been performed. External account access and publishing require explicit authorization.
## Role-based onboarding and workspaces

- Candidate signup now captures personal details, skills, and an optional resume.
- Candidate profile supports resume upload and Autofill suggestions.
- Recruiter signup captures company details and recruiters can publish jobs at `/recruiter/jobs/new`.
- Admin login is role-protected and the seeded development admin can view `/admin/dashboard`.
