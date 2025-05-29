# Product Detail Backend

## Requirements

- Java 17 or higher
- Maven 3.6 or higher
- Port 3000 available (or modify in `application.properties`) but you will need to change the port on the fronted as well

## Environment Setup

1. Clone the repository:
```bash
git clone [REPOSITORY_URL]
cd item-detail-backend
```

2. Verify Java 17 is installed:
```bash
java -version
```

3. Verify Maven is installed:
```bash
mvn -version
```

4. Configure the products file path:
   - Open `src/main/resources/application.properties`
   - Update the `data.products.file` property with the absolute path to your products.json file

Example:
```properties
# Windows
data.products.file=C:/Users/YourUser/Projects/item-detail-backend/src/main/resources/data/products.json

# macOS/Linux
data.products.file=/Users/YourUser/Projects/item-detail-backend/src/main/resources/data/products.json
```

> **Important**: You must update this path after cloning the project as it depends on your local system's file structure.

## Running the Application

### Development
```bash
mvn spring-boot:run
```

The server will start at `http://localhost:3000`

## Running Tests

```bash
mvn test
```

## API Endpoints

### 1. Get Product Detail

**Endpoint:** `GET /api/products/{id}`

**Description:** Retrieves complete details of a specific product.

**Request example:**
```bash
curl http://localhost:3000/api/products/1
```

**Successful response (200 OK):**
```json
{
  "id": 1,
  "title": "iPhone 13 Pro",
  "description": "The iPhone 13 Pro is Apple's most advanced smartphone featuring a Super Retina XDR display with ProMotion",
  "price": 999.99,
  "images": [
    "https://www.losdistribuidores.com/wp-content/uploads/2021/10/iphone-13-pro-4.jpg",
    "https://www.losdistribuidores.com/wp-content/uploads/2021/10/iphone-13-pro-3.jpg",
    "https://www.losdistribuidores.com/wp-content/uploads/2021/10/iphone-13-pro-1.jpg"
  ],
  "stock": 50,
  "condition": "new",
  "seller": {
    "id": "seller123",
    "name": "Apple Store Official",
    "rating": 4.9,
    "sales": 15000
  },
  "paymentMethods": [
    {
      "type": "credit_card",
      "name": "Visa",
      "installments": [
        {
          "quantity": 1,
          "amount": 999.99
        },
        {
          "quantity": 12,
          "amount": 89.99
        }
      ]
    }
  ],
  "ratings": {
    "average": 4.8,
    "total": 2500
  },
  "reviews": [
    {
      "id": "rev1",
      "user": "John D.",
      "rating": 5,
      "comment": "Excellent product, arrived in perfect condition and earlier than expected.",
      "date": "2024-03-15"
    }
  ]
}
```

**Error response (404 Not Found):**
```json
{
}
```

### 2. Create New Product

**Endpoint:** `POST /api/products`

**Description:** Creates a new product in the system.

**Required Headers:**
- Content-Type: application/json

**Request example:**
```bash
curl -X POST http://localhost:3000/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Samsung Galaxy S24",
    "description": "The latest Samsung Galaxy with integrated AI",
    "price": 899.99,
    "images": [
      "https://example.com/galaxy-s24-1.jpg",
      "https://example.com/galaxy-s24-2.jpg"
    ],
    "stock": 30,
    "condition": "new",
    "seller": {
      "id": "seller456",
      "name": "Samsung Store Official",
      "rating": 4.8,
      "sales": 12000
    },
    "paymentMethods": [
      {
        "type": "credit_card",
        "name": "Mastercard",
        "installments": [
          {
            "quantity": 1,
            "amount": 899.99
          },
          {
            "quantity": 12,
            "amount": 79.99
          }
        ]
      }
    ],
    "ratings": {
      "average": 4.7,
      "total": 1500
    }
  }'
```

**Successful response (201 Created):**
```json
{
  "id": 2,
  "title": "Samsung Galaxy S24",
  ...
}
```

**Error responses:**
- 400 Bad Request (Duplicate ID or invalid data):
```json
{
  "error": "Product ID already exists"
}
```
- 500 Internal Server Error:
```json
{
  "error": "an exception occurred while saving the product"
}
```

## Data Validations

The system includes the following product validations:

- `title`: Cannot be empty
- `description`: Cannot be empty
- `price`: Must be greater than 0
- `images`: URLs cannot be empty
- `stock`: Must be 0 or greater
- `condition`: Cannot be empty
- `seller`: Cannot be null
- `paymentMethods`: Must have at least one payment method
- `ratings`: Cannot be null

## Model Structure

### Product
```json
{
  "id": "integer",
  "title": "string",
  "description": "string",
  "price": "number",
  "images": ["string"],
  "stock": "integer",
  "condition": "string",
  "seller": "Seller",
  "paymentMethods": ["PaymentMethod"],
  "ratings": "Ratings",
  "reviews": ["Review"]
}
```

### Seller
```json
{
  "id": "string",
  "name": "string",
  "rating": "number",
  "sales": "integer"
}
```

### PaymentMethod
```json
{
  "type": "string",
  "name": "string",
  "installments": ["Installment"]
}
```

### Installment
```json
{
  "quantity": "integer",
  "amount": "number"
}
```

### Ratings
```json
{
  "average": "number",
  "total": "integer"
}
```

### Review
```json
{
  "id": "string",
  "user": "string",
  "rating": "integer",
  "comment": "string",
  "date": "string"
}
```

## CORS Configuration

The backend is configured to accept requests from `http://localhost:5173` (development frontend). 