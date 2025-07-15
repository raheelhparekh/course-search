# 📚 Course Search – Spring Boot + Elasticsearch

A full-text course search engine using Spring Boot and Elasticsearch. Supports keyword search, filters, sorting, pagination, and autocomplete over a dataset of 50+ sample course documents.

---

## 🚀 Features

* 🔎 Full-text search on `title` and `description`
* 🎯 Filter by:

  * Category
  * Type
  * Age range (`minAge`, `maxAge`)
  * Price range
  * Start date (`nextSessionDate`)
* 📊 Sort by:

  * Upcoming date (default)
  * Price ascending (`priceAsc`)
  * Price descending (`priceDesc`)
* 📄 Pagination support (`page`, `size`)
* ✨ Autocomplete suggestions for course titles

---

## ⚙️ Technologies

* Java 17
* Spring Boot 3
* Elasticsearch 8
* Elasticsearch Java Client
* Lombok
* Jackson
* Docker

---

## 🛠️ Setup Instructions

### 1. Clone the project

```bash
git clone https://github.com/raheelhparekh/course-search.git
cd course-search
```

### 2. Start Elasticsearch

Make sure you have Docker installed, then run:

```bash
docker run -p 9200:9200 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:8.13.0
```

OR use Docker Desktop → launch a container using image `elasticsearch:8.13.0`.

---

### 3. Run the Spring Boot App

Make sure you have JDK 17 and Maven installed.

```bash
./mvnw spring-boot:run
```

This will:

* Load sample data from `sample-courses.json`
* Index it into the `courses` index
* Add a `suggest` field for autocomplete
* Start the app at: `http://localhost:8080`

---

## 🔍 Search API

### Endpoint: `GET /api/search`

### Query Parameters

| Param       | Type     | Description                                |
| ----------- | -------- | ------------------------------------------ |
| `q`         | string   | Search keyword (full-text)                 |
| `category`  | string   | Filter by category                         |
| `type`      | string   | Filter by type (e.g., `COURSE`, `CLUB`)    |
| `minAge`    | integer  | Minimum age                                |
| `maxAge`    | integer  | Maximum age                                |
| `minPrice`  | double   | Minimum price                              |
| `maxPrice`  | double   | Maximum price                              |
| `startDate` | ISO date | Show courses on/after this date            |
| `sort`      | string   | `priceAsc`, `priceDesc` (default: by date) |
| `page`      | integer  | Page number (default: 0)                   |
| `size`      | integer  | Page size (default: 10)                    |

---

### 🧪 Sample Requests

#### 1. Search by keyword:

```bash
curl "http://localhost:8080/api/search?q=math"
```

#### 2. Filter by category and type:

```bash
curl "http://localhost:8080/api/search?category=Science&type=COURSE"
```

#### 3. Filter by age and price range:

```bash
curl "http://localhost:8080/api/search?minAge=8&maxAge=10&minPrice=100&maxPrice=200"
```

#### 4. Search with start date and sort by price descending:

```bash
curl "http://localhost:8080/api/search?startDate=2025-07-20T00:00:00Z&sort=priceDesc"
```

#### 5. Paginated search:

```bash
curl "http://localhost:8080/api/search?page=1&size=5"
```

---

### 📎 Response Format

```json
{
  "total": 50,
  "courses": [
    {
      "id": "1",
      "title": "Math Explorers",
      "category": "Math",
      "price": 199.99,
      "nextSessionDate": "2025-07-20T15:00:00Z"
    },
    ...
  ]
}
```

---

## ✨ Autocomplete API

### Endpoint: `GET /api/suggest`

### Query Parameter:

| Param    | Type   | Description                      |
| -------- | ------ | -------------------------------- |
| `prefix` | string | Partial title to get suggestions |

#### Sample Request:

```bash
curl "http://localhost:8080/api/suggest?prefix=math"
```

#### Response:

```json
["Math Explorers", "Math Mania"]
```

---

## 📁 Sample Data

* Stored in: `src/main/resources/sample-courses.json`
* Loaded automatically on startup via `DataLoader.java`
* Indexed into Elasticsearch `courses` index

---

## 📌 Notes

* Make sure Elasticsearch is running **before** starting the Spring Boot app
* Dates must be in **ISO 8601** format (e.g., `2025-07-22T00:00:00Z`)
* Course document mapping uses `Instant` for `nextSessionDate`
* `suggest` field must be mapped as a `completion` field for autocomplete

---
