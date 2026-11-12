# HireTrack Environment Setup Report

## Installed software and versions
- Java: OpenJDK 25.0.2 (Eclipse Adoptium Temurin)
- Maven: 3.9.16
- Node.js: v22.15.0
- npm: 10.9.2
- PostgreSQL Server: 17.11-5 (official winget package: PostgreSQL.PostgreSQL.17)
- pgAdmin 4: 9.19 (official winget package: PostgreSQL.pgAdmin)

## Missing dependencies
- No missing required development dependencies were found after installation.
- PostgreSQL and pgAdmin were initially missing from PATH and were installed using official Windows Package Manager packages.

## Database connection status
- PostgreSQL service status: running (`postgresql-x64-17`)
- PostgreSQL port: confirmed as `5432` in `postgresql.conf`
- PostgreSQL is running with secure `scram-sha-256` authentication.
- The dedicated `hiretrack_app` password-based connection succeeded in the elevated setup script.
- Flyway migrations completed successfully.
- Spring Boot integration verification completed successfully, including database connectivity and demo-record checks.

## Completed configurations
- Inspected the Windows environment for Java, Maven, Node/npm, PostgreSQL, and pgAdmin.
- Verified PostgreSQL is installed and the server is running.
- Verified the PostgreSQL server is configured for port `5432`.
- Installed PostgreSQL Server and pgAdmin 4 using official winget packages.
- Created the HireTrack Spring Boot app scaffold with Maven and Flyway configuration.
- Added PostgreSQL datasource and Flyway settings using environment variables:
  - `DB_HOST`
  - `DB_PORT`
  - `DB_NAME`
  - `DB_USERNAME`
  - `DB_PASSWORD`
- Created the initial Flyway migration files for schema creation and seeded demo recruitment data.
- Added [`scripts/Complete-PostgreSqlSetup.ps1`](./scripts/Complete-PostgreSqlSetup.ps1), an elevated script that prompts for the database password locally, creates `hiretrack_app` only if needed, creates `hiretrack_db` only if absent, restores secure authentication, and verifies the connection.
- Added the PostgreSQL Flyway database support module required by modern Flyway versions.
- Verified `DATABASE_CONNECTION_OK` and `SPRING_BOOT_FLYWAY_TEST_OK`.
- Added the React/Vite frontend with configurable Axios API base URL and Netlify SPA redirects.
- Added Render Docker deployment configuration and Neon/Render/Netlify deployment instructions.

## Remaining manual steps
1. Export the environment variables in each new development shell before starting the application:
   - `DB_HOST=localhost`
   - `DB_PORT=5432`
   - `DB_NAME=hiretrack_db`
   - `DB_USERNAME=hiretrack_app`
   - `DB_PASSWORD=<strong-password>`
2. Run the Spring Boot verification test when needed:
   - `mvn test`
3. No database installation or migration steps remain.
4. Start the backend with `.\scripts\Run-HireTrackBackend.ps1` when a development session needs the API; it prompts locally for the database password.

## Notes
- Database passwords were intentionally not stored in source files or committed to this repository.
- No existing PostgreSQL data was dropped or reset. The setup is designed to create a new database/user only when missing, and to avoid destructive resets.
