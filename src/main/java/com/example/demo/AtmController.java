
/*package com.example.demo;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AtmController {

    private void initialize(HttpSession session) {
        if (session.getAttribute("balance") == null) {
            session.setAttribute("balance", 10000.0);
            session.setAttribute("pin", "1234");
            session.setAttribute("mobile", "8075647838");
            session.setAttribute("transactions",
                    new ArrayList<String>());
            session.setAttribute("loggedIn", false);
        }
    }

    
private void addTransaction(HttpSession session, String message) {      
    Object data = session.getAttribute("transactions");

    List<String> transactions;

    if (data instanceof List<?>) {
        @SuppressWarnings("unchecked")
        List<String> existing = (List<String>) data;
        transactions = existing;
    } else {
        transactions = new ArrayList<>();
        session.setAttribute("transactions", transactions);
    }

    String record = java.time.LocalDateTime.now()
            .format(java.time.format.DateTimeFormatter.ofPattern(
                    "dd-MM-yyyy HH:mm:ss"))
            + " - " + message;

    transactions.add(record);
}


    private String showPage(
            HttpSession session, Model model, String message) {

        model.addAttribute("balance",
                session.getAttribute("balance"));
        model.addAttribute("mobile",
                session.getAttribute("mobile"));
        model.addAttribute("transactions",
                session.getAttribute("transactions"));
        model.addAttribute("loggedIn",
                session.getAttribute("loggedIn"));
        model.addAttribute("message", message);

        return "index";
    }

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        initialize(session);
        return showPage(session, model, "");
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String pin,
            HttpSession session,
            Model model) {

        initialize(session);

        if (pin.equals(session.getAttribute("pin"))) {
            session.setAttribute("loggedIn", true);
            return showPage(session, model,
                    "Login successful!");
        }

        return showPage(session, model, "Wrong PIN!");
    }

    @PostMapping("/atm")
    public String performAction(
            @RequestParam String action,
            @RequestParam(defaultValue = "") String amount,
            @RequestParam(defaultValue = "") String receiver,
            @RequestParam(defaultValue = "") String oldPin,
            @RequestParam(defaultValue = "") String newPin,
            @RequestParam(defaultValue = "") String confirmPin,
            @RequestParam(defaultValue = "") String newMobile,
            HttpSession session,
            Model model) {

        initialize(session);

        if (!Boolean.TRUE.equals(session.getAttribute("loggedIn"))) {
            return showPage(session, model, "Please log in first.");
        }

        double balance = (double) session.getAttribute("balance");
        String message = "";

        Object storedTransactions = session.getAttribute("transactions");

List<String> transactions;

if (storedTransactions instanceof List<?>) {
    transactions = new ArrayList<>();

    for (Object item : (List<?>) storedTransactions) {
        if (item instanceof String) {
            transactions.add((String) item);
        }
    }
} else {
    transactions = new ArrayList<>();
    session.setAttribute("transactions", transactions);
}

        try {
            switch (action) {

                case "balance":
                    message = "Your current balance is ₹" + balance;
                    break;

                case "withdraw": {
                    double value = Double.parseDouble(amount);

                    if (!Double.isFinite(value) || value <= 0) {
                        message = "Enter a valid amount.";
                    } else if (value > balance) {
                        message = "Insufficient balance!";
                    } else {
                        balance -= value;
                        transactions.add(
                        java.time.LocalDateTime.now()
                        .format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
                        + " - Withdraw: ₹" + value);
                        message = "Withdrawal successful!";
                    }
                    break;
                }

                case "deposit": {
                    double value = Double.parseDouble(amount);

                    if (!Double.isFinite(value) || value <= 0) {
                        message = "Enter a valid amount.";
                    } else {
                        balance += value;
                        transactions.add("Deposit: ₹" + value);
                        message = "Deposit successful!";
                    }
                    transactions.add(
                    java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
                    + " - Deposit: ₹" + value
                    );
                    break;
                }

                case "transfer": {
                    double value = Double.parseDouble(amount);

                    if (!Double.isFinite(value) || value <= 0) {
                        message = "Enter a valid amount.";
                    } else if (receiver.isBlank()) {
                        message = "Enter receiver account number.";
                    } else if (value > balance) {
                        message = "Insufficient balance!";
                    } else {
                        balance -= value;
                        transactions.add(
                                "Transfer: ₹" + value + " to " + receiver);
                        message = "Transfer successful (demo only)!";
                    }
                    break;
                }

                case "changePin":
                    if (!oldPin.equals(session.getAttribute("pin"))) {
                        message = "Incorrect old PIN!";
                    } else if (!newPin.matches("\\d{4}")
                            || !newPin.equals(confirmPin)) {
                        message = "New PIN must be 4 digits and match.";
                    } else {
                        session.setAttribute("pin", newPin);
                        message = "PIN changed successfully!";
                    }
                    break;

                case "mobile":
                    if (newMobile.matches("\\d{10}")) {
                        session.setAttribute("mobile", newMobile);
                        message = "Mobile number updated!";
                    } else {
                        message = "Enter a valid 10-digit mobile number.";
                    }
                    break;

                case "statement":
                    message = "Your transaction history is shown below.";
                    break;

                case "logout":
                    session.setAttribute("loggedIn", false);
                    return showPage(session, model, "You have logged out.");

                default:
                    message = "Invalid action.";
            }

        } catch (NumberFormatException e) {
            message = "Please enter a valid amount.";
        }

        session.setAttribute("balance", balance);

        // Keep only the latest 20 transactions.
        while (transactions.size() > 20) {
            transactions.remove(0);
        }

        return showPage(session, model, message);
    }
}

*/


/*package com.example.demo;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AtmController {

    private static final int MAX_TRANSACTIONS = 20;

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private void initialize(HttpSession session) {
        if (session.getAttribute("balance") == null) {
            session.setAttribute("balance", 10000.0);
            session.setAttribute("pin", "1234");
            session.setAttribute("mobile", "8075647838");
            session.setAttribute("transactions", new ArrayList<String>());
            session.setAttribute("loggedIn", false);
        }
    }

    private void addTransaction(
            List<String> transactions, String message) {

        String record = LocalDateTime.now().format(DATE_FORMAT)
                + " - " + message;

        transactions.add(record);

        // Keep only the latest 20 transactions.
        while (transactions.size() > MAX_TRANSACTIONS) {
            transactions.remove(0);
        }
    }

    private String showPage(
            HttpSession session, Model model, String message) {

        model.addAttribute("balance",
                session.getAttribute("balance"));
        model.addAttribute("mobile",
                session.getAttribute("mobile"));
        model.addAttribute("transactions",
                session.getAttribute("transactions"));
        model.addAttribute("loggedIn",
                session.getAttribute("loggedIn"));
        model.addAttribute("message", message);

        return "index";
    }

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        initialize(session);
        return showPage(session, model, "");
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String pin,
            HttpSession session,
            Model model) {

        initialize(session);

        if (pin.equals(session.getAttribute("pin"))) {
            session.setAttribute("loggedIn", true);

            return showPage(
                    session, model, "Login successful!");
        }

        return showPage(session, model, "Wrong PIN!");
    }

    @PostMapping("/atm")
    public String performAction(
            @RequestParam String action,
            @RequestParam(defaultValue = "") String amount,
            @RequestParam(defaultValue = "") String receiver,
            @RequestParam(defaultValue = "") String oldPin,
            @RequestParam(defaultValue = "") String newPin,
            @RequestParam(defaultValue = "") String confirmPin,
            @RequestParam(defaultValue = "") String newMobile,
            HttpSession session,
            Model model) {

        initialize(session);

        if (!Boolean.TRUE.equals(session.getAttribute("loggedIn"))) {
            return showPage(
                    session, model, "Please log in first.");
        }

        double balance =
                ((Number) session.getAttribute("balance")).doubleValue();

        String message = "";

        // Copy the existing session history into a modifiable list.
        Object storedTransactions =
                session.getAttribute("transactions");

        List<String> transactions = new ArrayList<>();

        if (storedTransactions instanceof List<?>) {
            for (Object item : (List<?>) storedTransactions) {
                if (item instanceof String) {
                    transactions.add((String) item);
                }
            }
        }

        try {
            switch (action) {

                case "balance":
                    message = "Your current balance is ₹" + balance;
                    break;

                case "withdraw": {
                    double value = Double.parseDouble(amount);

                    if (!Double.isFinite(value) || value <= 0) {
                        message = "Enter a valid amount.";
                    } else if (value > balance) {
                        message = "Insufficient balance!";
                    } else {
                        balance -= value;

                        addTransaction(
                                transactions, "Withdraw: ₹" + value);

                        message = "Withdrawal successful!";
                    }
                    break;
                }

                case "deposit": {
                    double value = Double.parseDouble(amount);

                    if (!Double.isFinite(value) || value <= 0) {
                        message = "Enter a valid amount.";
                    } else {
                        balance += value;

                        addTransaction(
                                transactions, "Deposit: ₹" + value);

                        message = "Deposit successful!";
                    }
                    break;
                }

                case "transfer": {
                    double value = Double.parseDouble(amount);

                    if (!Double.isFinite(value) || value <= 0) {
                        message = "Enter a valid amount.";
                    } else if (receiver.isBlank()) {
                        message = "Enter receiver account number.";
                    } else if (value > balance) {
                        message = "Insufficient balance!";
                    } else {
                        balance -= value;

                        addTransaction(
                                transactions,
                                "Transfer: ₹" + value + " to " + receiver);

                        message =
                                "Transfer successful (demo only)!";
                    }
                    break;
                }

                case "changePin":
                    if (!oldPin.equals(session.getAttribute("pin"))) {
                        message = "Incorrect old PIN!";
                    } else if (!newPin.matches("\\d{4}")
                            || !newPin.equals(confirmPin)) {
                        message =
                                "New PIN must be 4 digits and match.";
                    } else {
                        session.setAttribute("pin", newPin);
                        message = "PIN changed successfully!";
                    }
                    break;

                case "mobile":
                    if (newMobile.matches("\\d{10}")) {
                        session.setAttribute("mobile", newMobile);
                        message = "Mobile number updated!";
                    } else {
                        message =
                                "Enter a valid 10-digit mobile number.";
                    }
                    break;

                case "statement":
                    message =
                            "Your transaction history is shown below.";
                    break;

                case "logout":
                    session.setAttribute("loggedIn", false);

                    return showPage(
                            session, model, "You have logged out.");

                default:
                    message = "Invalid action.";
            }

        } catch (NumberFormatException e) {
            message = "Please enter a valid amount.";
        }

        // Save the updated balance.
        session.setAttribute("balance", balance);

        // Save the updated transaction history.
        session.setAttribute("transactions", transactions);

        return showPage(session, model, message);
    }
}
*/


package com.example.demo;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AtmController {

    private static final int MAX_TRANSACTIONS = 20;

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    // Initialize the ATM session
    private void initialize(HttpSession session) {
        if (session.getAttribute("balance") == null) {
            session.setAttribute("balance", 10000.0);
            session.setAttribute("pin", "1234");
            session.setAttribute("mobile", "8075647838");
            session.setAttribute("transactions", new ArrayList<String>());
            session.setAttribute("loggedIn", false);
        }

        // Ensure transaction history exists
        if (!(session.getAttribute("transactions") instanceof List<?>)) {
            session.setAttribute("transactions", new ArrayList<String>());
        }

        if (session.getAttribute("loggedIn") == null) {
            session.setAttribute("loggedIn", false);
        }
    }

    // Add one transaction and keep only the latest 20
    private void addTransaction(
            List<String> transactions, String message) {

        String record = LocalDateTime.now().format(DATE_FORMAT)
                + " - " + message;

        transactions.add(record);

        while (transactions.size() > MAX_TRANSACTIONS) {
            transactions.remove(0);
        }
    }

    // Display the ATM home page
    private String showPage(
            HttpSession session, Model model, String message) {

        model.addAttribute("balance", session.getAttribute("balance"));
        model.addAttribute("mobile", session.getAttribute("mobile"));
        model.addAttribute("transactions", session.getAttribute("transactions"));
        model.addAttribute("loggedIn", session.getAttribute("loggedIn"));
        model.addAttribute("message", message);

        return "index";
    }

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        initialize(session);
        return showPage(session, model, "");
    }

    // Login
    @PostMapping("/login")
    public String login(
            @RequestParam(defaultValue = "") String pin,
            HttpSession session,
            Model model) {

        initialize(session);

        if (pin.equals(session.getAttribute("pin"))) {
            session.setAttribute("loggedIn", true);
            return showPage(session, model, "Login successful!");
        }

        return showPage(session, model, "Wrong PIN!");
    }

    // Process ATM actions
    @PostMapping("/atm")
    public String performAction(
            @RequestParam(defaultValue = "") String action,
            @RequestParam(defaultValue = "") String amount,
            @RequestParam(defaultValue = "") String receiver,
            @RequestParam(defaultValue = "") String oldPin,
            @RequestParam(defaultValue = "") String newPin,
            @RequestParam(defaultValue = "") String confirmPin,
            @RequestParam(defaultValue = "") String newMobile,
            HttpSession session,
            Model model) {

        initialize(session);

        if (!Boolean.TRUE.equals(session.getAttribute("loggedIn"))) {
            return showPage(session, model, "Please log in first.");
        }

        double balance =
                ((Number) session.getAttribute("balance")).doubleValue();

        // Work with a modifiable copy of the existing history
        Object storedTransactions = session.getAttribute("transactions");
        List<String> transactions = new ArrayList<>();

        if (storedTransactions instanceof List<?>) {
            for (Object item : (List<?>) storedTransactions) {
                if (item instanceof String) {
                    transactions.add((String) item);
                }
            }
        }

        String message = "";

        try {
            switch (action) {

                case "balance":
                    message = "Your current balance is ₹" + balance;
                    break;

                case "deposit": {
                    double value = Double.parseDouble(amount);

                    if (!Double.isFinite(value) || value <= 0) {
                        message = "Enter a valid amount.";
                    } else {
                        balance += value;
                        addTransaction(transactions, "Deposit: ₹" + value);
                        message = "Deposit successful!";
                    }
                    break;
                }

                case "withdraw": {
                    double value = Double.parseDouble(amount);

                    if (!Double.isFinite(value) || value <= 0) {
                        message = "Enter a valid amount.";
                    } else if (value > balance) {
                        message = "Insufficient balance!";
                    } else {
                        balance -= value;
                        addTransaction(transactions, "Withdraw: ₹" + value);
                        message = "Withdrawal successful!";
                    }
                    break;
                }

                case "transfer": {
                    double value = Double.parseDouble(amount);

                    if (!Double.isFinite(value) || value <= 0) {
                        message = "Enter a valid amount.";
                    } else if (receiver.isBlank()) {
                        message = "Enter receiver account number.";
                    } else if (value > balance) {
                        message = "Insufficient balance!";
                    } else {
                        balance -= value;
                        addTransaction(
                                transactions,
                                "Transfer: ₹" + value + " to " + receiver);
                        message = "Transfer successful (demo only)!";
                    }
                    break;
                }

                case "changePin":
                    if (!oldPin.equals(session.getAttribute("pin"))) {
                        message = "Incorrect old PIN!";
                    } else if (!newPin.matches("\\d{4}")
                            || !newPin.equals(confirmPin)) {
                        message = "New PIN must be 4 digits and match.";
                    } else {
                        session.setAttribute("pin", newPin);
                        message = "PIN changed successfully!";
                    }
                    break;

                case "mobile":
                    if (newMobile.matches("\\d{10}")) {
                        session.setAttribute("mobile", newMobile);
                        message = "Mobile number updated!";
                    } else {
                        message = "Enter a valid 10-digit mobile number.";
                    }
                    break;

                case "statement":
                    message = "Your transaction history is shown below.";
                    break;

                case "logout":
                    session.setAttribute("loggedIn", false);
                    return showPage(session, model, "You have logged out.");

                default:
                    message = "Invalid action.";
            }

        } catch (NumberFormatException e) {
            message = "Please enter a valid amount.";
        }

        // Save the updated balance and transaction history
        session.setAttribute("balance", balance);
        session.setAttribute("transactions", transactions);

        return showPage(session, model, message);
    }
}
