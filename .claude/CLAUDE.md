# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

- **Build**: `./gradlew build`
- **Run**: `./gradlew run`
- **Test**: `./gradlew test`
- **Clean**: `./gradlew clean`

## Architecture Overview

This is a Kotlin-based task management API built with Ktor server framework. The application follows a layered architecture:

### Core Components

- **Application Entry Point**: `Application.kt` - Main entry point that configures plugins and loads environment variables from `.env` file
- **Database Layer**: Uses Exposed ORM with MySQL backend, HikariCP connection pooling, and Flyway for migrations
- **API Layer**: RESTful endpoints defined in `routes/TaskRoutes.kt`
- **Data Models**: Task entities defined in `model/Task.kt` with both database entities and serializable DTOs


### Project Structure Architecture
-**plugins** configurations for database, http, routing, etc...
-**routes** http routing for application
-**Services** service keep basic business logic
-**Model** object

### Technology & Architecture
- **Frameworks & Versions:**
    - http requests: OkHttp
- **Language:**
    - kotlin
- **Database & ORM:**
    - MySql
- **UI & Styling:** TODO: List your UI framework and styling approach
- **Authentication:** TODO: Specify your authentication system
- **Key Architectural Patterns:** TODO: List your main architectural patterns

### Configs
The application is modularized through Ktor plugins in `plugins/`:
- `Database.kt` - Database configuration, connection pooling, and Flyway migrations
- `HTTP.kt` - HTTP server configuration  
- `Routing.kt` - Route registration
- `Serialization.kt` - JSON serialization setup

### Database Schema

- Uses Flyway migrations located in `src/main/resources/db/migration/`
- Current schema includes a `tasks` table with id, title, description, completed status, and timestamps
- Note: There's a schema mismatch between the migration (uses `name`, `details`) and the Kotlin model (uses `title`, `description`, `completed`, timestamps)

### Environment Configuration

- Requires `.env` file with `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`
- Server runs on port 8080 by default (configurable via `PORT` environment variable)
- Configuration managed through `application.conf`

### Dependencies

- Ktor 2.3.4 for web server
- Exposed 0.43.0 for ORM
- MySQL connector for database
- Flyway for database migrations
- Kotlinx Serialization for JSON handling

### Package structure
- root package is com.pz.jobito
-- ** each new service or model must have new package following this structure ** 
- services package is com.pz.jobito.service
- api model package is com.pz.jobito.model.api
- service model package is com.pz.jobito.model

### Rest api
In case of any error will return object [ErrorResponse]
{ 
  status: "error",
  message: "Error message",
  details: "Any other details if it is possible"
}

