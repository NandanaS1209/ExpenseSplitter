package com.expensesplitter.expensesplitter.controller;

import com.expensesplitter.expensesplitter.model.Expense;
import com.expensesplitter.expensesplitter.service.ExpenseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public Expense addExpense(@RequestBody Map<String, Object> payload) {
        String id = (String) payload.get("id");
        double amount = Double.parseDouble(payload.get("amount").toString());
        String paidBy = (String) payload.get("paidBy");
        List<String> participants = (List<String>) payload.get("participants");

        Expense expense = new Expense(id, amount, paidBy);
        return expenseService.addExpense(expense, participants);
    }

    @GetMapping("/settlements")
    public List<ExpenseService.Settlement> getSettlements() {
        return expenseService.getOptimizedSettlements();
    }
}
