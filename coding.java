
import java.util.ArrayList;
import java.util.Scanner;

public class DigitalWalletSystem {

    static Scanner sc = new Scanner(System.in);

    static class Transaction {
        String type;
        double amount;
        String details;

        Transaction(String type, double amount, String details) {
            this.type = type;
            this.amount = amount;
            this.details = details;
        }

        void display() {
            System.out.printf("%-15s Rs. %-10.2f %s%n",
                    type, amount, details);
        }
    }

    static class Wallet {
        String walletId;
        String ownerName;
        double balance;
        ArrayList<Transaction> history;

        Wallet(String walletId, String ownerName) {
            this.walletId = walletId;
            this.ownerName = ownerName;
            this.balance = 0.0;
            this.history = new ArrayList<>();
        }

        void addMoney(double amount) {
            if (amount <= 0) {
                System.out.println("Amount must be greater than zero.");
                return;
            }

            balance += amount;
            history.add(new Transaction(
                    "ADD MONEY", amount, "Money added to wallet"));
            System.out.println("Money added successfully!");
        }

        void withdrawMoney(double amount) {
            if (amount <= 0) {
                System.out.println("Amount must be greater than zero.");
            } else if (amount > balance) {
                System.out.println("Insufficient wallet balance.");
            } else {
                balance -= amount;
                history.add(new Transaction(
                        "WITHDRAW", amount, "Money withdrawn"));
                System.out.println("Withdrawal successful!");
            }
        }

        void transferMoney(Wallet receiver, double amount) {
            if (receiver == null) {
                System.out.println("Receiver wallet not found.");
                return;
            }

            if (receiver == this) {
                System.out.println("Cannot transfer money to your own wallet.");
                return;
            }

            if (amount <= 0) {
                System.out.println("Amount must be greater than zero.");
                return;
            }

            if (amount > balance) {
                System.out.println("Insufficient wallet balance.");
                return;
            }

            balance -= amount;
            receiver.balance += amount;

            history.add(new Transaction(
                    "TRANSFER", amount,
                    "Sent to " + receiver.ownerName));

            receiver.history.add(new Transaction(
                    "RECEIVED", amount,
                    "Received from " + ownerName));

            System.out.println("Money transferred successfully!");
        }

        void checkBalance() {
            System.out.println("\n--- WALLET BALANCE ---");
            System.out.println("Wallet ID : " + walletId);
            System.out.println("Owner     : " + ownerName);
            System.out.printf("Balance   : Rs. %.2f%n", balance);
        }

        void showHistory() {
            System.out.println("\n--- TRANSACTION HISTORY ---");

            if (history.isEmpty()) {
                System.out.println("No transactions available.");
                return;
            }

            System.out.printf("%-15s %-18s %s%n",
                    "TYPE", "AMOUNT", "DETAILS");
            System.out.println("-----------------------------------------------");

            for (Transaction t : history) {
                t.display();
            }
        }
    }

    static ArrayList<Wallet> wallets = new ArrayList<>();
    static int nextWalletId = 1001;

    public static void main(String[] args) {
        int choice;

        do {
            System.out.println("\n==================================");
            System.out.println(" DIGITAL WALLET MANAGEMENT SYSTEM");
            System.out.println("==================================");
            System.out.println("1. Create Wallet");
            System.out.println("2. Add Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Transfer Money");
            System.out.println("5. Check Balance");
            System.out.println("6. Transaction History");
            System.out.println("7. Display All Wallets");
            System.out.println("8. Exit");
            System.out.println("==================================");
            System.out.print("Enter your choice: ");

            choice = readInt();

            switch (choice) {
                case 1:
                    createWallet();
                    break;
                case 2:
                    addMoney();
                    break;
                case 3:
                    withdrawMoney();
                    break;
                case 4:
                    transferMoney();
                    break;
                case 5:
                    checkBalance();
                    break;
                case 6:
                    showHistory();
                    break;
                case 7:
                    displayAllWallets();
                    break;
                case 8:
                    System.out.println("Thank you for using Digital Wallet!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 8);

        sc.close();
    }

    static int readInt() {
        while (!sc.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            sc.next();
        }

        int value = sc.nextInt();
        sc.nextLine();
        return value;
    }

    static double readAmount() {
        while (!sc.hasNextDouble()) {
            System.out.print("Enter a valid amount: ");
            sc.next();
        }

        double amount = sc.nextDouble();
        sc.nextLine();
        return amount;
    }

    static Wallet findWallet(String id) {
        for (Wallet w : wallets) {
            if (w.walletId.equalsIgnoreCase(id)) {
                return w;
            }
        }
        return null;
    }

    static void createWallet() {
        System.out.print("Enter wallet owner's name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        String id = String.valueOf(nextWalletId++);
        Wallet wallet = new Wallet(id, name);
        wallets.add(wallet);

        System.out.println("Wallet created successfully!");
        System.out.println("Wallet ID: " + id);
        System.out.println("Owner    : " + name);
    }

    static void addMoney() {
        Wallet w = getWallet();
        if (w == null) return;

        System.out.print("Enter amount to add: Rs. ");
        double amount = readAmount();
        w.addMoney(amount);
    }

    static void withdrawMoney() {
        Wallet w = getWallet();
        if (w == null) return;

        System.out.print("Enter amount to withdraw: Rs. ");
        double amount = readAmount();
        w.withdrawMoney(amount);
    }

    static void transferMoney() {
        Wallet sender = getWallet();
        if (sender == null) return;

        System.out.print("Enter receiver Wallet ID: ");
        String receiverId = sc.nextLine().trim();
        Wallet receiver = findWallet(receiverId);

        System.out.print("Enter transfer amount: Rs. ");
        double amount = readAmount();

        sender.transferMoney(receiver, amount);
    }

    static void checkBalance() {
        Wallet w = getWallet();
        if (w != null) w.checkBalance();
    }

    static void showHistory() {
        Wallet w = getWallet();
        if (w != null) w.showHistory();
    }

    static void displayAllWallets() {
        System.out.println("\n--- ALL WALLETS ---");

        if (wallets.isEmpty()) {
            System.out.println("No wallets created.");
            return;
        }

        for (Wallet w : wallets) {
            System.out.printf(
                    "ID: %s | Owner: %s | Balance: Rs. %.2f%n",
                    w.walletId, w.ownerName, w.balance);
        }
    }

    static Wallet getWallet() {
        System.out.print("Enter Wallet ID: ");
        String id = sc.nextLine().trim();

        Wallet w = findWallet(id);

        if (w == null) {
            System.out.println("Wallet not found. Please check the ID.");
        }

        return w;
    }
}
