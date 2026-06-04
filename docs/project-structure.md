# Project Structure

This project uses a modular monolith structure. The goal is to keep team ownership clear while avoiding premature microservice complexity.

## Backend Modules

```text
anran-lims-boot
anran-lims-common
anran-lims-system
anran-lims-sample
anran-lims-task
anran-lims-report
anran-lims-instrument
```

Module responsibilities:

- `anran-lims-boot`: application entry, web adapters, global exception handling, runtime configuration.
- `anran-lims-common`: shared primitives only, such as response wrappers, error codes, base exceptions, and generic utilities.
- `anran-lims-system`: users, roles, permissions, menus, audit logs, authentication policy.
- `anran-lims-sample`: sample registration, receiving, circulation, storage, disposal.
- `anran-lims-task`: testing tasks, assignment, execution, review workflow.
- `anran-lims-report`: report drafting, approval, signing, issuing, archive state.
- `anran-lims-instrument`: instrument ledger, calibration, maintenance, usage, acquisition integration.

## Backend Layering

Each business module should follow this package layout:

```text
controller/
service/
service/impl/
mapper/
entity/
dto/
vo/
converter/
enums/
constant/
```

Layer rules:

- `controller`: request validation, authorization boundary, response mapping.
- `service`: business orchestration, transaction boundary, state transition.
- `mapper`: persistence only, no business rules.
- `entity`: database persistence fields only.
- `dto`: request and internal transfer models.
- `vo`: response and page display models.
- `converter`: conversion between entity, dto, and vo.
- `enums`: stable business states and codes.
- `constant`: module-local constants.

Cross-module rules:

- Prefer service interfaces or explicit application-layer entry points.
- Do not directly manipulate another module's tables from a mapper.
- Do not move domain-specific logic into `anran-lims-common`.

## Frontend Structure

```text
anran-lims-frontend/src
|-- api/
|   |-- system/
|   |-- sample/
|   |-- task/
|   |-- report/
|   `-- instrument/
|-- components/common/
|-- constants/
|-- layout/
|-- router/
|-- store/
|-- styles/
|-- types/
|-- utils/
`-- views/
    |-- dashboard/
    |-- system/
    |-- sample/
    |-- task/
    |-- report/
    `-- instrument/
```

Frontend rules:

- `views` contains page composition.
- `api` contains backend request functions grouped by domain.
- `components/common` contains reusable UI components only.
- `store` contains shared state when page-local state is not enough.
- `types` contains shared TypeScript contracts.
- Domain names should match backend module names where practical.

## Database Structure

```text
database/init.sql
database/migration/VYYYYMMDDHHMM__description.sql
database/repeatable/R__description.sql
```

Migration rules:

- Versioned migrations are immutable after application.
- Fixes require a new migration.
- Repeatable scripts are reserved for views, procedures, or stable seed data.
- Brand names belong in repository/module names, not every table name.

Naming rules:

- Table names use semantic prefixes, such as `sys_` and `lims_`.
- Column names use `snake_case`.
- Primary key column is `id`.
- Foreign keys use `<entity>_id`.
- Standard audit fields are `create_time`, `update_time`, `delete_time`, `create_by`, `update_by`.

## Team Ownership

For a 30-person team, recommended ownership is:

- Platform/core team: `boot`, `common`, security, audit, database migration guardrails.
- System team: users, roles, permissions, menus, login policy.
- Sample/task team: sample flow, testing workflow, review workflow.
- Report team: report templates, approval, issuing, archive.
- Instrument team: ledger, maintenance, calibration, acquisition adapters.
- Frontend platform owner: layout, routing, shared UI, API conventions.

Each feature should declare the owning module before implementation.
