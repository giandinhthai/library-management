# Library Management System
## Introduction
This library system supports both members and librarians throughout the book borrowing process:

Librarians can manage book inventory and member accounts.
Members can search for books, place reservations, and borrow available copies.
The system tracks borrowing history, manages reservations, and handles fines for overdue returns.

✨ Features
👩‍💼 For Members
- Get book catalog
- Place reservations for unavailable books
- Borrow available books
- View borrowing history and due dates
- Receive notifications about reservations and overdue books

🏢 For Librarians
- Manage book inventory (add/remove/update)
- Process book borrowings and returns
- Manage member accounts
- Handle reservations
- Process fine payments

🔒 Account Management
- Register as member or librarian
- Authorize access via Spring Security

📩 Notifications
- Reservation becomes available
- Due date reminders
- Overdue notices
- Fine payment confirmations


## Technical Architecture
🏛 **Layered Structure**
- Domain: Business logic and rules
- Application: Transaction coordination
- Infrastructure: Persistence and integration
- API: REST API endpoints

⚙ **Key Components**
- Book Catalog Service
- Borrowing Engine
- Reservation Manager
- Notification Service


## Domain Model Highlights
Account
- Attributes: email, password, type (MEMBER, LIBRARIAN)

### Member
- **Attributes**:
  - memberId, email, reputation score
  - membership tier (BRONZE, SILVER, GOLD)
  - outstanding fines amount
- **Relationships**:
  - Has multiple Borrow records
  - Has multiple Reservations
- **Key Behaviors**:
  - `borrow()`: Checks limits and creates new borrow record
  - `returnBook()`: Processes returns and calculates fines
  - `reserve()`: Creates new book reservations
  - `updateReputation()`: Adjusts based on borrowing behavior
  - Enforces tier-based borrowing limits

### Borrow
- **Attributes**:
  - borrowId, borrowedAt, dueDate
  - status (ACTIVE, COMPLETED)
  - totalFineAmount
- **Relationships**:
  - Belongs to one Member
  - Contains multiple BorrowItems
- **Key Behaviors**:
  - `returnBorrowItem()`: Processes individual book returns
  - `markCompletedIfAllReturned()`: Updates status
  - `getCurrentlyBorrowedBookIds()`: Tracks active loans

### BorrowItem
- **Attributes**:
  - bookId, bookPrice
  - fineAmount, returned status
  - returnedAt timestamp
- **Relationships**:
  - Belongs to one Borrow record
- **Key Behaviors**:
  - `calculateFine()`: Determines overdue penalties
  - `processReturn()`: Marks item as returned
  - Enforces return validation rules

### Reservation
- **Attributes**:
  - reservationId, bookId
  - status (PENDING, READY_FOR_PICKUP, COMPLETED, CANCELLED, EXPIRED)
  - reservedAt, expiresAt
- **Relationships**:
  - Belongs to one Member
- **Key Behaviors**:
  - `markAsReady()`: Changes status when book available
  - `complete()`: Finalizes fulfilled reservation
  - `isExpired()`: Checks 2-day pickup window
  - Automatic expiration handling

### Key Business Rules
- **Fines Calculation**:
  - Standard overdue penalty: 2,000/day
  - Maximum fine: 1.5x book price after 30 days
- **Reservations**:
  - 2-day pickup window after becoming available
  - Tier-based limits (0-2 reservations)
- **Borrowing Limits**:
  - Bronze: 0 books
  - Silver: 2 books
  - Gold: 4 books
- **Reputation System**:
  - Monthly +5 reputation
  - Affects membership tier