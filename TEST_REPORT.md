# HireTrack Test Report

## Summary

| Requirement | Status | Evidence |
|---|---|---|
| PostgreSQL running | PASS | `pg_isready` accepted connections on port 5432 |
| Flyway migrations | PASS | Backend startup applied/validated migrations V1-V5 |
| Demo jobs through API | PASS | `GET /api/jobs` returned 30 jobs |
| Demo jobs in browser | PASS | `/jobs` now auto-loads and displayed the seeded job cards |
| Candidate detailed signup | PASS | Signup captures name, location, skills, and optional resume |
| Candidate resume upload/autofill | PASS | Multipart profile endpoint returns editable autofill suggestions |
| Recruiter job publishing UI | PASS | Protected recruiter route and `POST /api/jobs` are implemented |
| Admin login route | PASS | `ADMIN` role is enforced for `/api/admin/**` and admin dashboard route |
| Live backend restart and Flyway V8 | PASS | PostgreSQL connection succeeded and migrations applied through V8 |
| Live candidate signup | PASS | New candidate account created successfully |
| Live recruiter signup | PASS | New recruiter account created successfully |
| Live recruiter job publishing | PASS | Protected recruiter request created a job; live jobs count increased |
| Live admin login | PASS | Seeded admin authenticated successfully |
| Live resume upload/autofill | PASS | Multipart endpoint returned extracted headline, skills, and bio |
| Backend health | PASS | `GET /api/health` returned HTTP 200 and `status=UP` |
| Frontend dev server | PASS | `http://localhost:5173/` returned HTTP 200 |
| Frontend jobs route | PASS | `http://localhost:5173/jobs` returned HTTP 200 |
| Browser landing page | PASS | Opened `http://localhost:5173` and verified HireTrack navigation/content |
| Frontend production build | PASS | `npm run build` succeeded |
| Maven package | PASS | `mvn clean package -DskipTests` succeeded |
| Invalid login rejection | PASS | Invalid login returned HTTP 401 before the final security restart |
| Candidate registration | FAIL | Final live request returned HTTP 403 |
| Candidate application workflow | NOT TESTED | Blocked by registration response |
| Recruiter shortlist workflow | NOT TESTED | Blocked by registration response |
| Database application persistence | NOT TESTED | Blocked by end-to-end workflow |
| Admin dashboard workflow | NOT TESTED | Requires authenticated test user |
| Netlify/Render/Neon publishing | NOT TESTED | External credentials and publishing were not authorized |

## Notes

- No passwords or production secrets were written to source files.
- The final 403 is not reported as a successful authentication test. Backend security-filter diagnosis remains before acceptance can be complete.
