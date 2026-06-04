# Anran LIMS

Anran LIMS is a laboratory information management system scaffolded for environmental monitoring workflows.

The repository is organized as a modular Spring Boot backend, a Vue 3 frontend, and a versioned database script area. The current priority is to build the compliance foundation first, then expand resource management, traceability, and instrument integration.

## Current Scope

- Backend: Spring Boot 3, Java 17, Maven multi-module.
- Frontend: Vue 3, TypeScript, Vite, Vue Router.
- Database: MySQL-oriented initialization and migration scripts.
- Team model: about 30 people, using modular monolith boundaries before introducing distributed services.

## Repository Layout

```text
.
|-- anran-lims-boot/          # Spring Boot application entry and runtime config
|-- anran-lims-common/        # Shared result models, errors, constants, utilities
|-- anran-lims-system/        # Users, auth, roles, permissions, audit
|-- anran-lims-sample/        # Sample registration and sample flow
|-- anran-lims-task/          # Testing tasks and review workflow
|-- anran-lims-report/        # Report authoring, approval, issuing
|-- anran-lims-instrument/    # Instrument ledger, calibration, usage
|-- anran-lims-frontend/      # Vue 3 frontend
|-- database/                 # Init scripts, migrations, repeatable scripts
|-- docs/                     # Project structure and team conventions
|-- AGENTS.md                 # Workspace rules for agents and contributors
|-- LIMS*.md                  # Architecture review and upgrade plan
`-- pom.xml                   # Maven parent project
```

## Backend

Use the local Maven runtime if global Maven is unavailable:

```powershell
.\.tools\apache-maven-3.9.6\bin\mvn.cmd validate
.\.tools\apache-maven-3.9.6\bin\mvn.cmd compile
```

Application entry:

```text
anran-lims-boot/src/main/java/com/anran/lims/AnranLimsApplication.java
```

Local health endpoint:

```text
GET http://localhost:8080/api/health
```

Database connection can be overridden with:

```text
ANRAN_LIMS_DB_URL
ANRAN_LIMS_DB_USERNAME
ANRAN_LIMS_DB_PASSWORD
```

## Frontend

```powershell
cd anran-lims-frontend
npm.cmd install
npm.cmd run dev
npm.cmd run build
```

The frontend API base URL is configured through:

```text
VITE_API_BASE_URL
```

See `anran-lims-frontend/.env.example`.

## Database

```text
database/init.sql
database/migration/VYYYYMMDDHHMM__description.sql
database/repeatable/R__description.sql
```

Rules:

- Do not edit a migration after it has been applied.
- Add a new migration for follow-up changes.
- Use semantic table prefixes such as `sys_` and `lims_`.
- Use `snake_case` for database identifiers.

## Implementation Priority

- P0: data change logs, audit logs, backup, password policy, login lockout.
- P1: RBAC, personnel certificates/training, instrument usage and maintenance.
- P2: standard materials, method library, sample flow logs, barcode/QR code.
- P3: instrument data acquisition, electronic signature, environment monitoring, Xinchuang compatibility.

## More Details

See `docs/project-structure.md` for backend, frontend, and database structure conventions.
