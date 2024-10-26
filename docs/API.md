<div align="center">

# Movie Database API Documentation

[![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)](https://github.com/pilves/movies-api)
[![Documentation](https://img.shields.io/badge/documentation-yes-brightgreen.svg)](README.md)

Complete reference for the Movie Database REST API

</div>

## 📑 Table of Contents

- [Authentication](#-authentication)
- [Response Format](#-response-format)
- [Pagination](#-pagination)
- [Error Handling](#-error-handling)
- [Rate Limiting](#-rate-limiting)
- [Endpoints](#-endpoints)
  - [Genres](#genres)
  - [Movies](#movies)
  - [Actors](#actors)
- [Data Models](#-data-models)
- [Examples](#-examples)

## 🔒 Authentication

Currently, the API is open and doesn't require authentication.

## 📦 Response Format

All responses are returned in JSON format with the following structure:

### Success Response
```json
{
    "content": [...],
    "pageNo": 0,
    "pageSize": 10,
    "totalElements": 100,
    "totalPages": 10,
    "last": false
}
```

### Error Response
```json
{
    "status": 400,
    "message": "Invalid request",
    "errors": {
        "fieldName": "Error description"
    }
}
```

## 📄 Pagination

Paginated endpoints accept the following query parameters:
- `page` (default: 0): Page number
- `size` (default: 10): Items per page

Example:
```http
GET /api/movies?page=2&size=15
```

## ❌ Error Handling

### Error Codes
Status | Description
-------|------------
400 | Bad Request - Invalid input
404 | Not Found - Resource doesn't exist
409 | Conflict - Resource already exists
429 | Too Many Requests - Rate limit exceeded
500 | Internal Server Error

## ⚡ Rate Limiting

- Rate limit: 100 requests per minute
- Headers included in response:
  - `X-RateLimit-Limit`
  - `X-RateLimit-Remaining`
  - `X-RateLimit-Reset`

## 🔚 Endpoints

### Genres

#### Get All Genres
```http
GET /api/genres
```

Response:
```json
[
    {
        "id": 1,
        "name": "Action"
    },
    {
        "id": 2,
        "name": "Comedy"
    }
]
```

#### Get Genre by ID
```http
GET /api/genres/{id}
```

Response:
```json
{
    "id": 1,
    "name": "Action"
}
```

#### Create Genre
```http
POST /api/genres
Content-Type: application/json

{
    "name": "Horror"
}
```

Response:
```json
{
    "id": 3,
    "name": "Horror"
}
```

#### Update Genre
```http
PATCH /api/genres/{id}
Content-Type: application/json

{
    "name": "Horror/Thriller"
}
```

#### Delete Genre
```http
DELETE /api/genres/{id}
```

Optional query parameter:
- `force` (boolean): Force delete even if genre has associated movies

### Movies

#### Get All Movies
```http
GET /api/movies
```

Query parameters:
- `page` (integer): Page number
- `size` (integer): Page size
- `genreId` (integer): Filter by genre
- `year` (integer): Filter by release year
- `title` (string): Search by title
- `actorId` (integer): Filter by actor

Response:
```json
{
    "content": [
        {
            "id": 1,
            "title": "The Matrix",
            "releaseYear": 1999,
            "duration": 136,
            "genres": [
                {
                    "id": 1,
                    "name": "Action"
                },
                {
                    "id": 2,
                    "name": "Science Fiction"
                }
            ],
            "actors": [
                {
                    "id": 1,
                    "name": "Keanu Reeves"
                }
            ]
        }
    ],
    "pageNo": 0,
    "pageSize": 10,
    "totalElements": 100,
    "totalPages": 10,
    "last": false
}
```

#### Get Movie by ID
```http
GET /api/movies/{id}
```

#### Create Movie
```http
POST /api/movies
Content-Type: application/json

{
    "title": "Inception",
    "releaseYear": 2010,
    "duration": 148,
    "genres": [
        {"id": 1},
        {"id": 2}
    ],
    "actors": [
        {"id": 1},
        {"id": 2}
    ]
}
```

#### Update Movie
```http
PATCH /api/movies/{id}
Content-Type: application/json

{
    "title": "Inception - Director's Cut",
    "duration": 162
}
```

#### Delete Movie
```http
DELETE /api/movies/{id}
```

### Actors

#### Get All Actors
```http
GET /api/actors
```

Query parameters:
- `page` (integer): Page number
- `size` (integer): Page size
- `name` (string): Search by name

Response:
```json
{
    "content": [
        {
            "id": 1,
            "name": "Tom Hanks",
            "birthDate": "1956-07-09",
            "movies": [
                {
                    "id": 1,
                    "title": "Forrest Gump"
                }
            ]
        }
    ],
    "pageNo": 0,
    "pageSize": 10,
    "totalElements": 15,
    "totalPages": 2,
    "last": false
}
```

#### Get Actor by ID
```http
GET /api/actors/{id}
```

#### Create Actor
```http
POST /api/actors
Content-Type: application/json

{
    "name": "Leonardo DiCaprio",
    "birthDate": "1974-11-11"
}
```

#### Update Actor
```http
PATCH /api/actors/{id}
Content-Type: application/json

{
    "name": "Leo DiCaprio"
}
```

#### Delete Actor
```http
DELETE /api/actors/{id}
```

Optional query parameter:
- `force` (boolean): Force delete even if actor has associated movies

## 📊 Data Models

### Genre
Field | Type | Description
------|------|------------
id | Long | Unique identifier
name | String | Genre name (2-50 characters)

### Movie
Field | Type | Description
------|------|------------
id | Long | Unique identifier
title | String | Movie title (1-200 characters)
releaseYear | Integer | Year of release
duration | Integer | Duration in minutes
genres | Array | List of genre IDs
actors | Array | List of actor IDs

### Actor
Field | Type | Description
------|------|------------
id | Long | Unique identifier
name | String | Actor name (2-100 characters)
birthDate | Date | Date of birth (ISO format)

## 🔍 Examples

### Complete Movie Creation Flow

1. Create Genres:
```http
POST /api/genres
Content-Type: application/json

{"name": "Action"}
```

2. Create Actor:
```http
POST /api/actors
Content-Type: application/json

{
    "name": "Keanu Reeves",
    "birthDate": "1964-09-02"
}
```

3. Create Movie:
```http
POST /api/movies
Content-Type: application/json

{
    "title": "The Matrix",
    "releaseYear": 1999,
    "duration": 136,
    "genres": [{"id": 1}],
    "actors": [{"id": 1}]
}
```

### Advanced Search Example

Search for action movies from 2020 with specific actor:
```http
GET /api/movies?genreId=1&year=2020&actorId=1&page=0&size=10
```

## 📚 Additional Resources

- [README.md](README.md) - Project overview and setup

## 🤝 Support

For support and questions:
- Create an issue in the GitHub repository

---

<div align="center">

[Back to Top](#movie-database-api-documentation)

</div>

