package com.example.BorrowBookService.jpa.repository.jpa;

import com.example.BorrowBookService.aggregate.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaBorrowRepository extends JpaRepository<Borrow, UUID> {

}