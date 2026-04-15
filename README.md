# Invoice Processor

A Spring Boot service that extracts structured data from raw invoice text using an LLM (Anthropic Claude), then stores the results in PostgreSQL.

---

## The Problem

Invoices arrive in many unstructured forms — pasted text, OCR output, email bodies, CSV dumps. Manually parsing these into structured records (vendor, customer, line items, totals, dates) is tedious, error-prone, and does not scale.

This service solves that by accepting raw invoice text over a REST API and using a language model to reliably extract and normalise the fields into a structured record that is immediately persisted and queryable.

---

## How It Works

```
Client sends raw invoice text
        ↓
POST /api/invoices/process
        ↓
InvoiceExtractionService builds a prompt and calls Anthropic Claude
        ↓
Spring AI deserialises the model response into a typed ExtractedInvoice record
        ↓
InvoiceService maps the extraction to an Invoice entity and saves it to PostgreSQL
        ↓
Returns a structured InvoiceResponseDto (HTTP 201)
```

---

## Tech Stack

| Layer       | Technology                                  |
|-------------|---------------------------------------------|
| Framework   | Spring Boot 4.0.5, Java 25                  |
| Web         | Spring Web MVC                              |
| AI          | Spring AI 2.0.0-M4 — Anthropic Claude       |
| Persistence | Spring Data JPA / Hibernate — PostgreSQL    |
| Mapping     | MapStruct + Lombok                          |
| Local DB    | Docker Compose (`compose.yml`)              |
| Build       | Maven                                       |

---

## API Endpoints

| Method | Path                      | Description                                         |
|--------|---------------------------|-----------------------------------------------------|
| `POST` | `/api/invoices/process`   | Extract and persist a new invoice from raw text     |
| `GET`  | `/api/invoices`           | List all stored invoices                            |
| `GET`  | `/api/invoices/{id}`      | Fetch a single invoice by id                        |

### Process request body

```json
{
  "rawText": "Invoice #1042\nVendor: Acme Corp\n..."
}
```

### Example response (201)

```json
{
  "id": 1,
  "vendorName": "Acme Corp",
  "vendorEmail": "billing@acme.com",
  "customerName": "Donald",
  "invoiceNumber": "1042",
  "invoiceDate": "2024-03-15",
  "dueDate": "2024-04-15",
  "totalAmount": 1250.00,
  "currency": "USD",
  "status": "PROCESSED",
  "processedAt": "2026-04-15T10:30:00",
  "lineItems": [
    { "description": "Consulting services", "quantity": 5, "unitPrice": 250.00, "totalPrice": 1250.00 }
  ]
}
```

---

## Prerequisites

- Java 25+
- Maven
- Docker (for the local PostgreSQL container)
- An [Anthropic API key](https://console.anthropic.com/)

---

## Getting Started

**1. Start the database**

```bash
docker compose up -d
```

This spins up PostgreSQL on `localhost:5432` with database `invoice_db`.

**2. Set your Anthropic API key**

```bash
# Windows PowerShell
$env:ANTHROPIC_API_KEY="sk-ant-..."

# Linux / macOS
export ANTHROPIC_API_KEY="sk-ant-..."
```

**3. Run the application**

```bash
./mvnw spring-boot:run
```

The service starts on `http://localhost:8080`.

**4. Process an invoice**

```bash
curl -X POST http://localhost:8080/api/invoices/process \
  -H "Content-Type: application/json" \
  -d '{"rawText": "Invoice #1042 from Acme Corp dated March 15 2024..."}'
```

---

## Configuration

Key properties in `src/main/resources/application.properties`:

| Property | Default | Notes |
|----------|---------|-------|
| `spring.datasource.url` | `jdbc:postgresql://localhost:5432/invoice_db` | Matches `compose.yml` |
| `spring.ai.anthropic.api-key` | `${ANTHROPIC_API_KEY}` | Must be set in environment |
| `spring.ai.anthropic.chat.options.model` | `claude-haiku-4-5-20251001` | Swap for a different Claude model if needed |
| `spring.jpa.hibernate.ddl-auto` | `update` | Schema is managed automatically |

---

## Data Model

**Invoice**

- `id`, `invoiceNumber`, `invoiceDate`, `dueDate`
- `vendorName`, `vendorEmail`
- `customerName`
- `totalAmount`, `currency`
- `status` (`PROCESSED` / `ERROR`)
- `processedAt` (set automatically on insert)
- `lineItems` — embedded collection (`description`, `quantity`, `unitPrice`, `totalPrice`)

---

## Running Tests

```bash
./mvnw test
```

> Requires the PostgreSQL container to be running (or a configured test datasource).
