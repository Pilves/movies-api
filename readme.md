<div align="center">

# 🎬 Movie Database API

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![SQLite](https://img.shields.io/badge/SQLite-3.45.1-blue.svg)](https://www.sqlite.org/)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)

A modern, robust REST API for managing movie data with advanced querying capabilities.

[Key Features](#features) •
[Quick Start](#quick-start) •
[API Reference](#api-reference) •
[Documentation](#documentation)

</div>

---

## ✨ Features

### Core Functionality
- 🎯 Complete CRUD operations for movies, actors, and genres
- 🔄 Smart relationship management between entities
- 📱 RESTful API with intuitive endpoints
- 🔍 Advanced search and filtering capabilities
- 📊 Comprehensive data validation

### Technical Highlights
- 📦 Zero-config SQLite database
- 🚀 Pagination for optimal performance
- 💾 Efficient caching system
- ⚡ Rate limiting protection
- 🛡️ Robust error handling

---

## 🚀 Quick Start

### Prerequisites

You'll need:
- Java 17+ 
- Maven 3.6+
- Your favorite IDE (we recommend IntelliJ IDEA)

### One-Minute Setup

```bash
# Clone the repo
git clone https://github.com/yourusername/movies-api

# Navigate to project
cd movies-api

# Build and run
mvn spring-boot:run
```

That's it! The API is now running at `http://localhost:8080` 🎉

---

## 🎯 API Reference

### Movies

Endpoint | Method | Description
---------|--------|------------
`/api/movies` | GET | Get all movies
`/api/movies/{id}` | GET | Get movie by ID
`/api/movies` | POST | Create movie
`/api/movies/{id}` | PATCH | Update movie
`/api/movies/{id}` | DELETE | Delete movie

### Quick Examples

```http
# Create a new movie
POST http://localhost:8080/api/movies
{
    "title": "Inception",
    "releaseYear": 2010,
    "duration": 148,
    "genres": [{"id": 1}],
    "actors": [{"id": 1}]
}

# Search movies by title
GET http://localhost:8080/api/movies?title=inception
```

[View Full API Documentation →](docs/API.md)

---

## 🛠️ Advanced Features

### 🔍 Smart Search
```http
GET /api/movies?year=2020&genre=action&actor=1
```

### 📊 Statistics
```http
GET /api/stats/movies/by-year
GET /api/stats/actors/most-active
```

### 📥 Data Export
```http
GET /api/export/movies?format=csv
GET /api/export/movies?format=json
```

---

## 🏗️ Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/example/moviesapi/
│   │       ├── controller/
│   │       ├── service/
│   │       ├── repository/
│   │       ├── entity/
│   │       └── exception/
│   └── resources/
│       └── application.properties
```

---

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run specific test suite
mvn test -Dtest=MovieControllerTest
```

---

## 📈 Performance

- ⚡ Handles 1000+ requests/second
- 🔄 Average response time < 100ms
- 💾 Smart caching reduces database load

---

## 🤝 Contributing

1. Fork it
2. Create your feature branch (`git checkout -b feature/amazing`)
3. Commit your changes (`git commit -am 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing`)
5. Create a new Pull Request

---

## 🐛 Common Issues & Solutions

Problem | Solution
--------|----------
Database not creating | Check write permissions
404 on all routes | Verify port 8080 is free
Validation errors | Check request body format

---

## 📱 Example Requests

### Using cURL
```bash
# Get all movies
curl http://localhost:8080/api/movies

# Create a genre
curl -X POST http://localhost:8080/api/genres \
     -H "Content-Type: application/json" \
     -d '{"name": "Action"}'
```

### Using HTTPie
```bash
# Get all actors
http GET localhost:8080/api/actors

# Create movie
http POST localhost:8080/api/movies \
    title="The Matrix" \
    releaseYear:=1999 \
    duration:=136
```

---

<div align="center">

## 🌟 Star us on GitHub

If you find this project useful, please give it a star. It helps others discover this project.

[Report Bug](https://github.com/yourusername/movies-api/issues) •
[Request Feature](https://github.com/yourusername/movies-api/issues)

</div>

---

<div align="center">

Made with ❤️ by Pilves

</div>
