
package com.example.demo;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class TransactionController {

    @GetMapping("/transactions")
    public String showTransactions(HttpSession session, Model model) {

        if (!Boolean.TRUE.equals(session.getAttribute("loggedIn"))) {
            return "redirect:/";
        }

        Object data = session.getAttribute("transactions");

        List<String> transactions = new ArrayList<>();

        if (data instanceof List<?>) {
            for (Object item : (List<?>) data) {
                transactions.add(String.valueOf(item));
            }
        }

        Collections.reverse(transactions);

        model.addAttribute("transactions", transactions);

        return "transaction-history";
    }
}
