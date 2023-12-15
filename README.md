# Spring Postinator

## Overview

Welcome to Spring Postinator, a project that combines Spring Boot, CRUD operations, and JWT authentication for managing posts.

## Technologies

- Spring Boot
- Spring Data JPA
- Spring Security (JWT)
- Hibernate
- PostgreSQL (or your preferred database)
- Maven

## Getting Started

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/dastunning/backend_web_cscs_5118.git
mvn spring-boot:run
./mvnw spring-boot:run

## Authorization Endpoints

### Register User

- **Endpoint:**
   - `POST /web-backend/auth/registration`
- **Request:**
   - Body: JSON representing the user registration details.

### Login

- **Endpoint:**
   - `POST /web-backend/auth/login`
- **Request:**
   - Header: `authorization` containing user credentials.
- **Response:**
   - JSON with access and refresh tokens.

### Refresh Token

- **Endpoint:**
   - `POST /web-backend/auth/refresh`
- **Request:**
   - Header: `Authorization` with the refresh token.
- **Response:**
   - JSON with a new access token and the provided refresh token.
   - 
## CRUD Endpoints

### Basic CRUD:

- **Create a new post:**
   - `POST /web-backend/api/posts`
   - Request body: JSON representing the post.

- **Get all posts:**
   - `GET /web-backend/api/posts`

- **Get a single post by ID:**
   - `GET /web-backend/api/posts/{postId}`

- **Update a post by ID:**
   - `PUT /web-backend/api/posts/{postId}`
   - Request body: JSON representing the updated post.

- **Delete a post by ID:**
   - `DELETE /web-backend/api/posts/{postId}`

### Extra Features:

- **Get posts by author:**
   - `GET /web-backend/api/posts/byAuthor/{author}`

- **Create multiple posts:**
   - `POST /web-backend/api/posts/multiple`
   - Request body: JSON array representing multiple posts.

- **Get posts created in the last N days:**
   - `GET /web-backend/api/posts/createdInLastNDays/{days}`
Authentication
JWT (JSON Web Token) is used for authentication. Obtain it by making a POST request to /authenticate with your credentials.