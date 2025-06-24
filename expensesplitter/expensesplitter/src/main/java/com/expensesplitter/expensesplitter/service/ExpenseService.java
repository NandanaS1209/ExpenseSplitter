package com.expensesplitter.expensesplitter.service;

import com.expensesplitter.expensesplitter.model.Debt;
import com.expensesplitter.expensesplitter.model.Expense;
import com.expensesplitter.expensesplitter.model.User;
import com.expensesplitter.expensesplitter.repository.ExpenseRepository;
import com.expensesplitter.expensesplitter.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final List<Debt> debts = new ArrayList<>();

    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    public Expense addExpense(Expense expense, List<String> participantNames) {
        Expense savedExpense = expenseRepository.save(expense);
        calculateDebts(expense, participantNames);
        return savedExpense;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    private void calculateDebts(Expense expense, List<String> participantNames) {
        double share = expense.getAmount() / participantNames.size();
        for (String participantName : participantNames) {
            User participant = userRepository.findByName(participantName);
            if (participant != null && !participant.getId().equals(expense.getPaidBy())) {
                debts.add(new Debt(participant.getId(), expense.getPaidBy(), share));
            }
        }
    }

    public List<Settlement> getOptimizedSettlements() {
        Map<String, Map<String, Double>> netDebts = new HashMap<>();

        // Calculate net debts
        for (Debt debt : debts) {
            netDebts.putIfAbsent(debt.getDebtorId(), new HashMap<>());
            Map<String, Double> creditorMap = netDebts.get(debt.getDebtorId());
            creditorMap.put(debt.getCreditorId(), creditorMap.getOrDefault(debt.getCreditorId(), 0.0) + debt.getAmount());
        }

        // Net out mutual debts
        List<Settlement> settlements = new ArrayList<>();
        Set<String> processed = new HashSet<>();

        for (String debtorId : netDebts.keySet()) {
            for (String creditorId : netDebts.get(debtorId).keySet()) {
                String pair = debtorId + "-" + creditorId;
                String reversePair = creditorId + "-" + debtorId;

                if (!processed.contains(pair) && !processed.contains(reversePair)) {
                    double debtorOwes = netDebts.get(debtorId).getOrDefault(creditorId, 0.0);
                    double creditorOwes = netDebts.getOrDefault(creditorId, new HashMap<>()).getOrDefault(debtorId, 0.0);

                    double netAmount = debtorOwes - creditorOwes;
                    if (netAmount > 0.01) {
                        User debtor = userRepository.findById(debtorId).orElse(new User());
                        User creditor = userRepository.findById(creditorId).orElse(new User());
                        settlements.add(new Settlement(debtor.getName(), creditor.getName(), netAmount));
                    } else if (netAmount < -0.01) {
                        User debtor = userRepository.findById(creditorId).orElse(new User());
                        User creditor = userRepository.findById(debtorId).orElse(new User());
                        settlements.add(new Settlement(debtor.getName(), creditor.getName(), Math.abs(netAmount)));
                    }

                    processed.add(pair);
                    processed.add(reversePair);
                }
            }
        }

        return settlements;
    }

    public static class Settlement {
        public String debtor;
        public String creditor;
        public double amount;

        public Settlement(String debtor, String creditor, double amount) {
            this.debtor = debtor;
            this.creditor = creditor;
            this.amount = amount;
        }
    }
}
