# Repository Guidelines

## Project Structure & Module Organization
`picture-backend` hosts the Spring Boot service (`src/main/java/com/spring/picturebackend`) and SQL assets (`sql/`). Configuration and seeds live in `src/main/resources`. `picture-frontend` provides the Vue 3 client; key folders include `src/pages` for views, `src/components` for shared UI, `src/api` for generated clients, and `public/` for static assets. Keep `target/` and `dist/` out of version control.

## Build, Test, and Development Commands
Install dependencies once with `mvn clean install` and `npm install`. Day-to-day commands:
```bash
cd picture-backend && mvn spring-boot:run
cd picture-backend && mvn test
cd picture-frontend && npm run dev
cd picture-frontend && npm run build
cd picture-frontend && npm run openapi
```
`spring-boot:run` starts the API, `mvn test` runs backend unit and integration tests, `npm run dev` launches Vite with hot reload, `npm run build` emits production bundles, and `npm run openapi` regenerates typed clients from the latest backend schema.

## Coding Style & Naming Conventions
Use 4-space indentation for Java and TypeScript. Java packages follow `com.spring.picturebackend.<module>`; classes, DTOs, and enums are PascalCase, while request/response payloads end in `Request`, `Response`, or `VO`. Vue components use PascalCase filenames, Pinia stores are camelCase, and utility modules adopt kebab-case. Run `npm run lint` (ESLint + Vue/TS presets) and `npm run format` (Prettier) before committing; backend code should compile cleanly without IDE-generated comments.

## Testing Guidelines
Place Spring Boot tests under `picture-backend/src/test/java` and favor `@SpringBootTest` + `@Transactional` for service-level coverage. Execute `mvn test` before pushing. The frontend currently relies on static analysis—run `npm run type-check` to catch typing regressions and extend the suite with Vitest specs under `src/__tests__` when introducing complex logic. Document manual QA steps for features that touch file uploads or review workflows.

## Commit & Pull Request Guidelines
Follow the existing Conventional Commits style (`feat:`, `fix:`, `refactor:`) visible in the Git history. Keep messages concise and imperative. Pull requests should summarize the change, list validation (`mvn test`, `npm run type-check`, screenshots for UI changes), and link tracking issues. Call out schema migrations or credential updates explicitly so reviewers can coordinate deployments.

## Environment Notes
Environment configuration resides in `picture-backend/src/main/resources/application*.yml`; prefer environment variables for secrets. Update `openapi.config.js` when backend routes or auth headers change so regenerated clients stay in sync.
