# HireTrack API

The local API base URL is `http://localhost:8080/api`. The production URL is configured with `VITE_API_BASE_URL`.

## Health

- `GET /health`

## Authentication

- `POST /auth/register/candidate`
- `POST /auth/register/recruiter`
- `POST /auth/login`
- `GET /auth/me`

Login returns a JWT bearer token. Send it as `Authorization: Bearer <token>`.

## Jobs

- `GET /jobs`
- `GET /jobs/{id}`
- `POST /jobs` (RECRUITER)
- `PUT /jobs/{id}` (owning RECRUITER)
- `DELETE /jobs/{id}` (owning RECRUITER or ADMIN)

## Applications

- `POST /jobs/{id}/applications` (CANDIDATE)
- `GET /applications/me` (CANDIDATE)
- `GET /jobs/{id}/applications` (owning RECRUITER or ADMIN)
- `PATCH /applications/{id}/status` (owning RECRUITER or ADMIN)

## Dashboard

- `GET /dashboard/candidate`
- `GET /dashboard/recruiter`
- `GET /dashboard/admin`

Exact response shapes are defined by the controller DTOs in the backend source.
