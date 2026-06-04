# Database

Database scripts are split into initialization, versioned migrations, and repeatable scripts.

## Directories

- `init.sql`: baseline database initialization for a fresh local environment.
- `migration/`: immutable versioned migrations.
- `repeatable/`: repeatable scripts such as views, procedures, or seed dictionaries.

## Naming

- Versioned migration: `VYYYYMMDDHHMM__description.sql`
- Repeatable script: `R__description.sql`

## Rules

- Do not edit a migration after it has been applied.
- Add a new migration for schema fixes.
- Keep table names semantic, using prefixes such as `sys_` and `lims_`.
- Use `snake_case` for database identifiers.
