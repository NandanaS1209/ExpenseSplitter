package com.expensesplitter.expensesplitter.controller;

import com.expensesplitter.expensesplitter.model.User;
import com.expensesplitter.expensesplitter.service.ExpenseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final ExpenseService expenseService;

    public UserController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return expenseService.getAllUsers();
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        return expenseService.addUser(user);
    }
}
