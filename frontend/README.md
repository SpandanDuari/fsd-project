# HireTrack frontend

React + Vite frontend for HireTrack. Copy `.env.example` to `.env` and set
`VITE_API_BASE_URL` to the backend API URL. The Axios client attaches a stored
JWT when present; unavailable endpoints intentionally render loading, error, or
empty states rather than fabricated records.

```powershell
cd frontend
npm install
npm run dev
```

Create a production bundle with `npm run build`. Netlify uses the root
`netlify.toml` configuration.
