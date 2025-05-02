package com.example.BorrowBookService.aggregate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(name = "reservations")
@Getter
public class Reservation {
    @Id
    private UUID reservationId;
    private UUID bookId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;
    private LocalDateTime reservedAt;
    private LocalDateTime expiresAt;
    final static int RESERVATION_EXPIRATION_DAYS = 2;
    @Setter
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    private Reservation(UUID reservationId, UUID bookId, Member member) {
        this.reservationId = reservationId;
        this.bookId = bookId;
        this.member = member;
        this.status = ReservationStatus.PENDING;
        this.reservedAt = LocalDateTime.now();
        this.expiresAt = null;
    }
    protected Reservation(){}
    public static Reservation create(UUID bookId, Member member) {
        return new Reservation(UUID.randomUUID(), bookId, member);
    }
    private void markAsReady(){
        if (status != ReservationStatus.PENDING) {
            throw new IllegalStateException("Can only mark a pending reservation as ready");
        }
        this.status = ReservationStatus.READY_FOR_PICKUP;
        //TODO: send notification to member
        this.expiresAt = LocalDateTime.now().plusDays(RESERVATION_EXPIRATION_DAYS);
    }
    public void cancel(){
        if (status == ReservationStatus.COMPLETED || status == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("Cannot cancel a completed or already cancelled reservation");
        }
        this.status = ReservationStatus.CANCELLED;
    }
    public void complete(){
        if (status != ReservationStatus.READY_FOR_PICKUP) {
            throw new IllegalStateException("Cannot complete a reservation that is not ready for pickup");
        }
        this.status = ReservationStatus.COMPLETED;
    }
    public boolean isExpired() {
        return status == ReservationStatus.READY_FOR_PICKUP &&
                expiresAt != null &&
                LocalDateTime.now().isAfter(expiresAt);
    }
    public Reservation checkAndExpireIfNeeded() {
        if (isExpired()) {
            this.status = ReservationStatus.EXPIRED;
        }
        return this;
    }


}
