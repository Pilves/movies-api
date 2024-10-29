<div align="center">

# 🎬 Movie Database API

A modern, high-performance REST API for managing movie collections, built with Spring Boot 3.

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![SQLite](https://img.shields.io/badge/SQLite-3.45.1-blue.svg)](https://www.sqlite.org/)

</div>

## 📑 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Installation](#-installation)
- [API Reference](#-api-reference)
- [Data Models](#-data-models)
- [Error Handling](#-error-handling)
- [Best Practices](#-best-practices)
- [Security](#-security)

## 🌟 Overview

The Movie Database API provides a robust solution for managing movie collections with support for complex relationships between movies, actors, and genres. Built for performance and scalability, it offers comprehensive CRUD operations, advanced searching, intelligent pagination, and a user review system.

## ✨ Features

- **📚 Complete CRUD Operations** for movies, actors, genres, and reviews
- **⭐ Review & Rating System** with user feedback and scoring
- **🔍 Advanced Search Capabilities** with case-insensitive matching
- **📄 Smart Pagination** with configurable page sizes
- **🔒 Data Validation** and error handling
- **🤝 Many-to-Many Relationship** management
- **🚀 High Performance** with optimized queries
- **📊 Rating Analytics** with average calculations

## 🛠 Installation

### Prerequisites

```bash
Java 17+
Maven 3.6+
SQLite 3.45.1+
```

### Quick Start

1️⃣ Clone the repository:
```bash
git clone https://github.com/pilves/movies-api.git
cd movies-api
```

2️⃣ Build the project:
```bash
mvn clean install
```

3️⃣ Run the application:
```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080/api`

## 📚 API Reference

### 🎬 Movies API

#### Create Movie
```http
POST /api/movies
Content-Type: application/json

{
  "title": "Time Travelers",
  "releaseYear": 2014,
  "duration": 145,
  "genres": [
    {"id": 1},  // Action
    {"id": 3}   // Science Fiction
  ],
  "actors": [
    {"id": 11}, // Jennifer Lawrence
    {"id": 8}   // Chadwick Boseman
  ]
}

Response: 201 Created
{
  "id": 9,
  "title": "Time Travelers",
  "releaseYear": 2014,
  "duration": 145,
  "genres": [...],
  "actors": [...]
}
```

#### Get Movies (Paginated)
```http
GET /api/movies?page=0&size=10

Response: 200 OK
{
  "content": [
    {
      "id": 1,
      "title": "The Last Stand",
      "releaseYear": 2022,
      "duration": 135
    },
    {
      "id": 2,
      "title": "Eternal Light",
      "releaseYear": 2021,
      "duration": 142
    },
    // ... more movies
  ],
  "totalPages": 2,
  "totalElements": 20,
  "size": 10,
  "number": 0
}
```

#### Get Movie Details
```http
GET /api/movies/2/details

Response: 200 OK
{
  "id": 2,
  "title": "Eternal Light",
  "releaseYear": 2021,
  "duration": 142,
  "genres": [
    {
      "id": 2,
      "name": "Drama"
    },
    {
      "id": 3,
      "name": "Science Fiction"
    }
  ],
  "actors": [
    {
      "id": 11,
      "name": "Jennifer Lawrence",
      "birthDate": "1990-08-15"
    },
    {
      "id": 5,
      "name": "Leonardo DiCaprio",
      "birthDate": "1974-11-11"
    }
  ]
}
```

### 🎭 Actors API

#### Get Actor Details
```http
GET /api/actors/1/details

Response: 200 OK
{
  "id": 1,
  "name": "Emma Stone",
  "birthDate": "1988-11-06",
  "movies": [
    {
      "id": 3,
      "title": "City of Dreams",
      "releaseYear": 2020,
      "duration": 118,
      "genreNames": ["Comedy", "Romance"]
    },
    {
      "id": 7,
      "title": "The Funny Side",
      "releaseYear": 2016,
      "duration": 110,
      "genreNames": ["Comedy"]
    },
    {
      "id": 14,
      "title": "Sweet Romance",
      "releaseYear": 2009,
      "duration": 115,
      "genreNames": ["Comedy", "Romance"]
    }
  ]
}
```

#### Search Actors by Name
```http
GET /api/actors?name=emma&page=0&size=10

Response: 200 OK
{
  "content": [
    {
      "id": 1,
      "name": "Emma Stone",
      "birthDate": "1988-11-06"
    }
  ],
  "totalElements": 1,
  "totalPages": 1
}
```

### 🎪 Genres API

#### Get All Genres
```http
GET /api/genres?page=0&size=10

Response: 200 OK
{
  "content": [
    {
      "id": 1,
      "name": "Action"
    },
    {
      "id": 2,
      "name": "Drama"
    },
    {
      "id": 3,
      "name": "Science Fiction"
    },
    {
      "id": 4,
      "name": "Comedy"
    },
    {
      "id": 5,
      "name": "Thriller"
    },
    {
      "id": 6,
      "name": "Romance"
    }
  ],
  "totalElements": 6,
  "totalPages": 1
}
```
### 🌟 Reviews API

#### Create Movie Review
```http
POST /api/movies/{movieId}/reviews?userName={userName}
Content-Type: application/json

{
  "rating": 5,
  "comment": "An absolute masterpiece! The cinematography was breathtaking."
}

Response: 201 Created
{
  "id": 1,
  "movieId": 123,
  "movieTitle": "Time Travelers",
  "userName": "john_doe",
  "rating": 5,
  "comment": "An absolute masterpiece! The cinematography was breathtaking.",
  "createdAt": "2024-10-30T15:30:00Z"
}
```

#### Get Movie Reviews (Paginated)
```http
GET /api/movies/{movieId}/reviews?page=0&size=10

Response: 200 OK
{
  "content": [
    {
      "id": 1,
      "movieId": 123,
      "movieTitle": "Time Travelers",
      "userName": "john_doe",
      "rating": 5,
      "comment": "An absolute masterpiece!",
      "createdAt": "2024-10-30T15:30:00Z"
    },
    {
      "id": 2,
      "movieId": 123,
      "movieTitle": "Time Travelers",
      "userName": "jane_smith",
      "rating": 4,
      "comment": "Great movie, loved the special effects",
      "createdAt": "2024-10-30T16:45:00Z"
    }
  ],
  "totalPages": 1,
  "totalElements": 2,
  "size": 10,
  "number": 0
}
```

#### Get Movie Average Rating
```http
GET /api/movies/{movieId}/reviews/average

Response: 200 OK
4.5
```

#### Delete Review
```http
DELETE /api/movies/{movieId}/reviews/{reviewId}?userName={userName}

Response: 204 No Content
```

## 📊 Data Models

### Movie
```json
{
  "id": "Long",
  "title": "String (required)",
  "releaseYear": "Integer (required)",
  "duration": "Integer (required)",
  "genres": "Set<Genre>",
  "actors": "Set<Actor>"
}
```

### Actor
```json
{
  "id": "Long",
  "name": "String (required)",
  "birthDate": "LocalDate (required, past)",
  "movies": "Set<Movie>"
}
```

### Genre
```json
{
  "id": "Long",
  "name": "String (required)",
  "movies": "Set<Movie>"
}
```
### Review
```json
{
  "id": "Long",
  "movieId": "Long (required)",
  "userName": "String (required)",
  "rating": "Integer (required, range: 1-5)",
  "comment": "String (optional, max: 1000 chars)",
  "createdAt": "LocalDateTime",
  "updatedAt": "LocalDateTime"
}
```

## ❌ Error Handling

The API uses standard HTTP status codes and provides detailed error messages:

```json
{
  "error": "Resource not found",
  "message": "Movie with id 999 not found",
  "timestamp": "2024-10-28T14:59:15.146+00:00",
  "path": "/api/movies/999"
}
```

### Common Status Codes
- `200` - Success
- `201` - Created
- `204` - No Content
- `400` - Bad Request
- `404` - Not Found
- `409` - Conflict (e.g., duplicate review)
- `500` - Server Error
- 
## 💡 Best Practices

1. Use pagination for large datasets
2. Include relevant relationships in detail endpoints
3. Use force parameter for deleting related entities
4. Keep page sizes reasonable (max 100)
5. Use case-insensitive search for better user experience
6. One review per user per movie
7. Provide meaningful review comments
8. Handle review updates carefully

## 🔒 Security

- Input validation on all endpoints
- Pagination limits to prevent DoS
- Data integrity checks
- Safe deletion handling
- Review ownership validation
- Comment content validation


---
<div align="center">
Made with ❤️ by Pilves
</div>
