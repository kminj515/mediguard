# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Mediguard** is a financial literacy education platform for elderly users. It is a monorepo with a Spring Boot backend and a React frontend. The backend package name is `guardpay` (legacy name).

## Commands

### Frontend (`/frontend`)
```bash
npm run dev        # Start Vite dev server
npm run build      # Production build
npm run lint       # ESLint check
npm run preview    # Preview production build
```

### Backend (`/backend`)
```bash
./gradlew bootRun              # Run Spring Boot app
./gradlew test                 # Run tests
./gradlew build                # Build (includes tests)
./gradlew build -x test        # Build without tests
./gradlew clean build          # Clean build
```

### Docker
```bash
docker-compose up              # Start all services
```

## Architecture

### Backend (Java 21, Spring Boot 3.5.5)

Package root: `com.example.guardpay`

**Global layer** (`global/`):
- `config/` — SecurityConfig, CORS, WebClient, Swagger, async config
- `jwt/` — JWT token provider, filters, custom UserDetails
- `auth/` — OAuth2 success/failure handlers
- `exception/` — `GlobalExceptionHandler`, domain-specific exceptions
- `response/` — `ApiResponse<T>` wrapper used for all endpoints
- `code/` — `ResponseCode` and `ResponseStatus` enums

**Domain layer** (`domain/`): Each domain follows Controller → Service → Repository with DTOs as Java Records.
- `member` — Auth (email/password + OAuth2 Google/Kakao), JWT reissue, profile management
- `chatbot` — Gemini 1.5 Flash integration via WebFlux WebClient, chat session management
- `diagnosis` — Financial literacy assessment and history
- `quiz` — Multiple-choice quizzes with category, scoring, and member progression
- `video` — Financial education video catalog
- `beneficiaries` — Mock transfer training simulator
- `shop` — Reward product catalog and point exchange
- `map` — Kakao Local API integration for nearby bank search

**Security**: Stateless JWT (SessionCreationPolicy.STATELESS). JWT filter runs before UsernamePasswordAuthenticationFilter. BCrypt for passwords.

**API response format**:
```json
{
  "status": { "code": "...", "message": "...", "description": "..." },
  "body": { ... }
}
```

**External APIs**: Google Gemini (AI chatbot), Kakao Local (maps), Google OAuth2, Kakao OAuth2.

**Environment config**: Profile-based (`dev`/`prod`). Production secrets injected via GitHub Secrets as `application-prod.yml` during CI/CD.

### Frontend (React 19, Vite 8)

Follows **Feature-Sliced Design (FSD)**:
```
src/
├── app/store/        # State management
├── features/         # auth, chatbot, diagnosis
├── components/       # common, layout, ui
├── pages/            # Page-level components
├── shared/           # api, constants, hooks, styles, utils
└── assets/
```

The FSD structure is scaffolded but mostly empty — frontend development is in early stages.

**No TypeScript** — plain JavaScript with ESLint (flat config, v9+).

### CI/CD

GitHub Actions (`.github/workflows/cicd.yml` in `backend/`) triggers on push to `develop` or `minkyu` branches: builds with Gradle (skip tests), builds Docker image, deploys to EC2 via SSH with `SPRING_PROFILES_ACTIVE=prod`.

Swagger UI is available at `/swagger-ui/index.html` when the backend is running.