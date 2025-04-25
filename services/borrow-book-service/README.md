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
    - Only allow reservation within **2 days from return date**. ✅
- Reservation rules: ✅
    - Member can only borrow the book **if available quantity > active reservations**  ✅, or if member already has a reservation with status `READY_FOR_PICKUP`.
    - If a member borrows a book that has an existing reservation, update reservation status to `COMPLETED`.
    - On book return:
        - Notify the next member in the reservation queue.  ✅
        - Update their reservation status to `READY_FOR_PICKUP`.  ✅
    - Change status to `EXPIRED` at the end of 2 days after status changes to `READY_FOR_PICKUP`✅
## 🛠️ Need to Fixes / Adjustments
- Fix system messages.
- transaction phase of return book event handler
## Todo 
### Step to take notify for next member ✅
1. receive domain event that book is returned
2. get next member in queue
3. update member.reservation status to `READY_FOR_PICKUP`
4. do notification in handler => violate single responsibility => make reservation ready event
5. send notification on reservation ready handler
### handle case member borrow book has a reservation with status `READY_FOR_PICKUP`✅
- allow borrow if member already has a reservation with status `READY_FOR_PICKUP`. ✅
- update reservation status to `COMPLETED`. ✅
- Change status to `EXPIRED` at the end of 2 days after status changes to `READY_FOR_PICKUP` ✅
- If it has reservation `EXPIRED` => allow borrow for next reservation in queue ✅
### Using map for some case, should not return list<boolean> ✅
### handle infra not return object ✅
### break jpa adapter use using one jpa repository ✅
### cronjob must call for command ✅
### change input to borrow ===
### rename some check to validate 
### member cannot borrow book while having reservation on it
### remove validate in some use-case have before commit
### change member id get from token
- add security
- change member in controller get from token
### add some api to query info 
- get borrow from member
- get reservation from member
- get book status
- get member status

# Consider
## update updateMemberReservationStatusOnBookReturnedEvent to handler after event
## do reserve should move to book ✅
- pros:
    - book can know who is reserving, easily to check available for borrow and reserve
        + But with current implement, it still needed to do by query
        + If using AR then in use-case do update book quantity too and have 2 repository.save
    - easily to get next reservation when book return
    - not need 2 event handler book return
- cons:
    - member must use repo to know if they can reserve (@query for current reserve book, how many book is reserve)
    - reserve must get domain event to change status to complete

### step to move reserve to book
- move `member.reserve` to `book.reserve` (member not need to know about reserve anymore)
- book reserve use-case check member can reserve ( 
    + Max reservation limit. 
    + Member is already borrowing the book. 
    + Outstanding fine over `100,000`.
    + Only allow reservation within **2 days from return date**. )
