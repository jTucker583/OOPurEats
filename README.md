# OOPur Eats

A food delivery order management system built for CSCI 4448 (Object Oriented Analysis and Design) at CU Boulder.


## How to Run

### Backend

```bash
./gradlew bootRun
```

Backend runs at `http://localhost:8080`

### Frontend

```bash
cd OOpurEatsFrontend
npm install
npm run dev
```

Frontend runs at `http://localhost:5173` (or check terminal output)

## API Endpoints

- `GET /api` - Get all orders
- `GET /api/{id}` - Get order by ID
- `POST /api` - Create new order
- `PUT /api/{id}` - Update order
- `DELETE /api/{id}` - Delete order

## Design Patterns

- State Pattern (order status management)
- Observer Pattern (restaurant order handling)
- Factory Pattern (state creation)
- Template Method Pattern (restaurant order creation)
- Singleton Pattern (OrderBus)

