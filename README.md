# Distributed Order & Inventory Management System

A production-grade backend system built in Java to model how large-scale commerce platforms handle orders, inventory, and payments under high concurrency and partial failures.

This project focuses on correctness, scalability, and fault tolerance rather than UI concerns or simple CRUD APIs.

---

## Goals

- Prevent inventory overselling
- Handle duplicate client requests safely
- Maintain order consistency without distributed transactions
- Scale horizontally using an event-driven architecture
- Recover gracefully from partial failures

---

## System Architecture

The system is built using a microservices architecture with asynchronous, event-driven communication.  
Each service owns its data and interacts with other services exclusively through domain events.

### Services

**Order Service**
- Owns the order lifecycle
- Acts as the main workflow coordinator.
- Manages order state transitions

**Inventory Service**
- Manages product stock
- Performs inventory reservations
- Prevents overselling under concurrent load

**Payment Service**
- Handles payment processing asynchronously
- Ensures idempotent payment execution

**Notification Service**
- Sends order status notifications

---

## Infrastructure

- Kafka for inter-service event communication
- PostgreSQL with one database per service
- Redis for idempotency keys and caching
- Docker and Docker Compose for local orchestration

---

## Order Processing Flow

1. Client sends a request to create an order
2. Order Service persists the order in `CREATED` state
3. Inventory Service attempts to reserve stock atomically
4. Payment Service processes payment asynchronously
5. Order transitions to `COMPLETED` or `CANCELLED`
6. Notification Service sends the final order status

All cross-service communication is asynchronous and event-driven.

---

## Order workflow

The system uses an order workflow pattern.

- Order Service acts as the main workflow coordinator.
- Each step emits domain events
- Failures trigger compensating actions

### Compensation Examples

- Inventory reserved but payment fails → inventory is released
- Order creation fails → no downstream actions are triggered

---

## Data Consistency Strategy

### Inventory Consistency

- Optimistic locking using a `version` column
- Atomic stock updates at the database level
- No distributed locks or global transactions

### Order Consistency

- Strict order state machine enforced by the Order Service
- Idempotent state transitions to handle duplicate events

---

## Idempotency

- All write APIs accept an `Idempotency-Key`
- Keys are stored in Redis with a configurable TTL
- Prevents duplicate order creation and duplicate payment execution

---

## Failure Handling

- At-least-once event delivery
- Consumer-side deduplication
- Retry with exponential backoff
- Dead-letter queues for poison messages

---

## Scalability

- Stateless services enabling horizontal scaling
- Kafka partitions keyed by `orderId`
- Independent database scaling per service

---

## Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Kafka
- Redis
- Docker and Docker Compose
- JUnit and Testcontainers

---

## Project Intent

This project is designed to simulate real-world backend challenges and demonstrate production-grade engineering decisions around concurrency, distributed consistency, and fault tolerance using Java.
