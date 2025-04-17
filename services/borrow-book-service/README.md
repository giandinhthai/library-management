# Library System Features

## 📚 Borrow
- Subtract quantity of book when borrowed.
- Create a borrow record for the book.

## 🔁 Return
- Increase book quantity upon return.
- Support **partial return** of books.
- Calculate **fine** for overdue returns.
- Update **member reputation** based on return behavior.

## 🔖 Reserve
- Check if the book is **active and reservable** (i.e., `available_quantity <= active_reservations` with statuses like `PENDING`, `READY_FOR_PICKUP`) ✅
- `member.createReservation()` ✅
- Validation checks: ✅
    - Max reservation limit. ✅
    - Member is already borrowing the book. ✅
    - Outstanding fine over `100,000`. ✅
- Reservation rules: ✅
    - Only allow reservation within **2 days from return date**. ✅
    - Member can only borrow the reserved book **if available quantity > active reservations**, or if member already has a reservation with status `READY_FOR_PICKUP`.
    - If a member borrows a book that has an existing reservation, update reservation status to `COMPLETED`.
    - On book return:
        - Notify the next member in the reservation queue.
        - Update their reservation status to `READY_FOR_PICKUP`.

## 🛠️ Need to Fixes / Adjustments
- Fix system messages.
## Step to do notify for next member
1. receive domain event that book is returned
2. get next member in queue
3. update their reservation status to `READY_FOR_PICKUP`
## do reserve should move to book
- pros:
    - book can know who is reserving, easily to check available for borrow and reserve
- cons:
    - member must query to know if they can reserve (@query for current reserve book, how many book is reserve)
    - reserve must get domain event to change status to complete
=> not move

