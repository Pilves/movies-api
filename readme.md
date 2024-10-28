<div align="center">

# 📽️ Movie Database API

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-20-orange.svg)](https://www.oracle.com/java/)
[![SQLite](https://img.shields.io/badge/SQLite-3.45.1-blue.svg)](https://www.sqlite.org/)

A modern, robust REST API for managing movie data with advanced querying capabilities.

[Key Features](#-features) •
[Quick Start](#-quick-start) •
[API Reference](#-api-endpoints) •
[Documentation](#-usage-examples)

</div>

---

## ✨ Features

- **Rich Domain Model**
    - Movies with multiple genres and actors
    - Comprehensive actor profiles
    - Genre categorization
    - Flexible relationship management

- **Advanced Querying**
    - Full text search for movies and actors
    - Filter by year, genre, or actor
    - Pagination and sorting support
    - Case-insensitive searching

- **Data Export**
    - Export to JSON and CSV formats
    - Customizable data formatting
    - Bulk data operations

- **Statistics & Analytics**
    - Movie distribution by year
    - Genre popularity metrics
    - Actor participation statistics
    - Average duration analytics

## 🛠️ Tech Stack

- **Spring Boot 3.3.5** - Application framework
- **SQLite** - Database
- **JPA/Hibernate** - ORM and data persistence
- **Jakarta Validation** - Data validation
- **Jackson** - JSON processing
- **Apache Commons CSV** - CSV processing

---

## 🚀 Quick Start

### Prerequisites

You'll need:
- Java 20+ 
- Maven 3.6+
- Your favorite IDE (we recommend IntelliJ IDEA)

### Setup Steps

1. Clone the repository
```bash
git clone https://github.com/yourusername/movies-api.git
cd movies-api
```

2. Build the project
```bash
./mvnw clean install
```

3. Run the application
```bash
./mvnw spring-boot:run
```

That's it! The API is now running at `http://localhost:8080` 🎉

---

## 🎯 API Endpoints

### Movies
```
GET    /api/movies              # List all movies (with pagination)
POST   /api/movies              # Create a new movie
GET    /api/movies/{id}         # Get movie details
PATCH  /api/movies/{id}         # Update movie
DELETE /api/movies/{id}         # Delete movie
```

### Actors
```
GET    /api/actors              # List all actors (with pagination)
POST   /api/actors              # Create a new actor
GET    /api/actors/{id}         # Get actor details
PATCH  /api/actors/{id}         # Update actor
DELETE /api/actors/{id}         # Delete actor
```

### Genres
```
GET    /api/genres              # List all genres
POST   /api/genres              # Create a new genre
GET    /api/genres/{id}         # Get genre details
PATCH  /api/genres/{id}         # Update genre
DELETE /api/genres/{id}         # Delete genre
```

### Statistics & Export
```
GET    /api/statistics          # Get movie statistics
GET    /api/export/movies/json  # Export movies as JSON
GET    /api/export/movies/csv   # Export movies as CSV
```

## 📝 Usage Examples

### Create a Movie
```http
POST /api/movies
Content-Type: application/json

{
    "title": "Inception",
    "releaseYear": 2010,
    "duration": 148,
    "genres": [
        {"id": 1},
        {"id": 3}
    ],
    "actors": [
        {"id": 1},
        {"id": 2}
    ]
}
```

### Search Movies
```http
GET /api/movies?title=inception&page=0&size=10
```

### Get Statistics
```http
GET /api/statistics
```

## 🔍 Advanced Features

### Robust Error Handling
- Comprehensive exception handling
- Detailed error messages
- Proper HTTP status codes

### Data Validation
- Input validation for all endpoints
- Business rule validation
- Relationship integrity checks

### Performance Optimization
- Paginated responses
- Efficient database queries
- Proper indexing

## 🤝 Contributing

1. Fork it
2. Create your feature branch (`git checkout -b feature/amazing`)
3. Commit your changes (`git commit -am 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing`)
5. Create a new Pull Request

## 🐛 Common Issues & Solutions

Problem | Solution
--------|----------
Database not creating | Check write permissions
404 on all routes | Verify port 8080 is free
Validation errors | Check request body format

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

[Report Bug](https://github.com/pilves/movies-api/issues) •
[Request Feature](https://github.com/pilves/movies-api/issues)

Made with ❤️ by Pilves

</div>
