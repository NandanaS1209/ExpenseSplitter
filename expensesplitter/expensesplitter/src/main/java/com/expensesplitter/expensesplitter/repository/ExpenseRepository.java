package com.expensesplitter.expensesplitter.repository;

import com.expensesplitter.expensesplitter.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, String> {
}
