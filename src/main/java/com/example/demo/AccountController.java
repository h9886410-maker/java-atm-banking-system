package com.example.demo;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AccountController {

    // Display the account profile
    @GetMapping("/account")
    public String account(HttpSession session, Model model) {

        // Only logged-in users can view their account
        if (!Boolean.TRUE.equals(session.getAttribute("loggedIn"))) {
            return "redirect:/";
        }

        // Initialize profile details for this demo account
        if (session.getAttribute("accountName") == null) {
            session.setAttribute("accountName", "Demo Account Holder");
        }

        if (session.getAttribute("accountNumber") == null) {
            session.setAttribute("accountNumber", "123456789012");
        }

        if (session.getAttribute("accountType") == null) {
            session.setAttribute("accountType", "Savings");
        }

        // Reuse the existing ATM session data
        model.addAttribute("accountName",
                session.getAttribute("accountName"));
        model.addAttribute("accountNumber",
                session.getAttribute("accountNumber"));
        model.addAttribute("accountType",
                session.getAttribute("accountType"));
        model.addAttribute("mobile",
                session.getAttribute("mobile"));
        model.addAttribute("balance",
                session.getAttribute("balance"));

        return "account";
    }

    // Update the profile
    @PostMapping("/account/update")
    public String updateAccount(
            @RequestParam String accountName,
            @RequestParam String mobile,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (!Boolean.TRUE.equals(session.getAttribute("loggedIn"))) {
            return "redirect:/";
        }

        String cleanedName = accountName.trim();
        String cleanedMobile = mobile.trim();

        if (cleanedName.isEmpty() || cleanedName.length() > 60) {
            redirectAttributes.addFlashAttribute(
                    "error", "Enter a name between 1 and 60 characters.");
            return "redirect:/account";
        }

        if (!cleanedMobile.matches("[0-9]{10}")) {
            redirectAttributes.addFlashAttribute(
                    "error", "Enter a valid 10-digit mobile number.");
            return "redirect:/account";
        }

        // Update only profile fields, not PIN or balance
        session.setAttribute("accountName", cleanedName);
        session.setAttribute("mobile", cleanedMobile);

        redirectAttributes.addFlashAttribute(
                "success", "Account profile updated successfully.");

        return "redirect:/account";
    }
}