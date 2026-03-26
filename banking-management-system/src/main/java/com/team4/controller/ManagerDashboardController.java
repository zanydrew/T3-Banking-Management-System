package com.team4.controller;


import com.team4.app.App;
import com.team4.model.account.Account;
import com.team4.model.account.AccountStatus;
import com.team4.model.transaction.Transaction;
import com.team4.model.user.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ManagerDashboardController {

    // ── Sidebar nav buttons ──
    @FXML
    private Button btnOverview, btnAccounts, btnCustomers, btnTransactions, btnTransfer;

    // ── Panels ──
    @FXML private VBox overviewPanel, accountsPanel, customersPanel, transactionsPanel, transferPanel;

    // ── Top bar ──
    @FXML private Label pageTitle, dateLabel, avatarLabel, managerNameLabel;

    // ── Overview ──
    @FXML private Label statTotalAccounts, statActiveAccounts, statTotalCustomers, statBlockedAccounts;
    @FXML private TableView<Account> overviewTable;
    @FXML private TableColumn<Account,String> colAccNum, colHolder, colType, colStatus;
    @FXML private TableColumn<Account,Double>  colBalance;

    // ── Accounts ──
    @FXML private TextField accountSearchField;
    @FXML private VBox      accountDetailBox;
    @FXML private Label     detailAccNum, detailHolder, detailBalance, detailStatus, accountActionMsg;
    @FXML private TableView<Account> accountsTable;
    @FXML private TableColumn<Account,String>  aColNum, aColHolder, aColEmail, aColType, aColStatus;
    @FXML private TableColumn<Account,Double>  aColBalance;

    // ── Customers ──
    @FXML private TextField customerSearchField, editFullName, editPhone;
    @FXML private VBox      customerDetailBox;
    @FXML private Label     detailUserId, detailUsername, customerActionMsg;
    @FXML private TableView<User> customersTable;
    @FXML private TableColumn<User,Integer> cColId;
    @FXML private TableColumn<User,String>  cColUsername, cColFullName, cColPhone, cColDob, cColRole;

    // ── Transactions ──
    @FXML private TextField txSearchField;
    @FXML private TableView<Transaction> transactionsTable;
    @FXML private TableColumn<Transaction,String> tColId, tColType, tColFrom, tColTo, tColStatus, tColDate;
    @FXML private TableColumn<Transaction,Double> tColAmount;

    // ── Fund Transfer ──
    @FXML private TextField depositAccField, depositAmtField;
    @FXML private TextField withdrawAccField, withdrawAmtField;
    @FXML private TextField transferFromField, transferToField, transferAmtField;
    @FXML private Label     depositMsg, withdrawMsg, transferMsg;

    // State
    private Account  selectedAccount;
    private User     selectedCustomer;

    // =========================================================================
    @FXML
    public void initialize() {
        // Set date
        dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));

        // Set manager name from session
        if (App.authService.getCurrentSession() != null) {
            String name = App.authService.getCurrentSession().getUsername();
            managerNameLabel.setText(name);
            avatarLabel.setText(String.valueOf(name.charAt(0)).toUpperCase());
        }

        setupTableColumns();
        loadOverview();
    }

    // =========================================================================
    // Navigation
    // =========================================================================

    private final Button[] navBtns() {
        return new Button[]{btnOverview, btnAccounts, btnCustomers, btnTransactions, btnTransfer};
    }

    private void activateNav(Button active) {
        for (Button b : navBtns()) {
            b.getStyleClass().remove("nav-btn-active");
        }
        if (!active.getStyleClass().contains("nav-btn-active"))
            active.getStyleClass().add("nav-btn-active");
    }

    private void showOnly(VBox panel, String title, Button navBtn) {
        for (VBox p : new VBox[]{overviewPanel, accountsPanel, customersPanel, transactionsPanel, transferPanel}) {
            p.setVisible(false);
            p.setManaged(false);
        }
        panel.setVisible(true);
        panel.setManaged(true);
        pageTitle.setText(title);
        activateNav(navBtn);
    }

    @FXML void showOverview()     { showOnly(overviewPanel,      "Overview",         btnOverview);     loadOverview(); }
    @FXML void showAccounts()     { showOnly(accountsPanel,      "Account Mgmt",     btnAccounts);     loadAllAccounts(); }
    @FXML void showCustomers()    { showOnly(customersPanel,     "Customer Mgmt",    btnCustomers);    loadAllCustomers(); }
    @FXML void showTransactions() { showOnly(transactionsPanel,  "Transactions",     btnTransactions); }
    @FXML void showTransfer()     { showOnly(transferPanel,      "Fund Transfer",    btnTransfer); }

    // =========================================================================
    // Overview
    // =========================================================================

    private void loadOverview() {
        try {
            List<Account> accounts = App.managerService.viewAllAccounts();
            List<User>    customers = App.managerService.viewAllCustomers();

            long active  = accounts.stream().filter(a -> a.getAccountStatus() == AccountStatus.ACTIVE).count();
            long blocked = accounts.stream().filter(a -> a.getAccountStatus() == AccountStatus.BLOCKED).count();

            statTotalAccounts.setText(String.valueOf(accounts.size()));
            statActiveAccounts.setText(String.valueOf(active));
            statTotalCustomers.setText(String.valueOf(customers.size()));
            statBlockedAccounts.setText(String.valueOf(blocked));

            overviewTable.setItems(FXCollections.observableArrayList(accounts));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================================================================
    // Accounts
    // =========================================================================

    private void loadAllAccounts() {
        try {
            List<Account> list = App.managerService.viewAllAccounts();
            accountsTable.setItems(FXCollections.observableArrayList(list));
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    private void searchAccount() {
        String num = accountSearchField.getText().trim();
        if (num.isEmpty()) return;
        try {
            selectedAccount = App.managerService.viewAccount(num);
            detailAccNum.setText(selectedAccount.getAccountNumber());
            detailHolder.setText(selectedAccount.getHolderName());
            detailBalance.setText(String.format("$%.2f", selectedAccount.getBalance()));
            detailStatus.setText(selectedAccount.getAccountStatus().name());
            accountDetailBox.setVisible(true);
            accountDetailBox.setManaged(true);
            hideMsg(accountActionMsg);
        } catch (Exception e) {
            showMsg(accountActionMsg, "Account not found: " + num, true);
            accountDetailBox.setVisible(false);
            accountDetailBox.setManaged(false);
        }
    }

    @FXML
    private void openAccount() {
        if (selectedAccount == null) return;
        try {
            App.managerService.openAccount(selectedAccount.getAccountNumber());
            showMsg(accountActionMsg, "Account opened successfully.", false);
            searchAccount();
            loadAllAccounts();
        } catch (Exception e) { showMsg(accountActionMsg, e.getMessage(), true); }
    }

    @FXML
    private void blockAccount() {
        if (selectedAccount == null) return;
        try {
            App.managerService.blockAccount(selectedAccount.getAccountNumber());
            showMsg(accountActionMsg, "Account blocked successfully.", false);
            searchAccount();
            loadAllAccounts();
        } catch (Exception e) { showMsg(accountActionMsg, e.getMessage(), true); }
    }

    @FXML
    private void closeAccount() {
        if (selectedAccount == null) return;
        try {
            App.managerService.closeAccount(selectedAccount.getAccountNumber());
            showMsg(accountActionMsg, "Account closed successfully.", false);
            searchAccount();
            loadAllAccounts();
        } catch (Exception e) { showMsg(accountActionMsg, e.getMessage(), true); }
    }

    // =========================================================================
    // Customers
    // =========================================================================

    private void loadAllCustomers() {
        try {
            List<User> list = App.managerService.viewAllCustomers();
            customersTable.setItems(FXCollections.observableArrayList(list));
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    private void searchCustomer() {
        String username = customerSearchField.getText().trim();
        if (username.isEmpty()) return;
        try {
            selectedCustomer = App.managerService.viewCustomerByUsername(username);
            detailUserId.setText(String.valueOf(selectedCustomer.getUserId()));
            detailUsername.setText(selectedCustomer.getUsername());
            editFullName.setText(selectedCustomer.getFullname());
            editPhone.setText(selectedCustomer.getPhone());
            customerDetailBox.setVisible(true);
            customerDetailBox.setManaged(true);
            hideMsg(customerActionMsg);
        } catch (Exception e) {
            showMsg(customerActionMsg, "Customer not found: " + username, true);
            customerDetailBox.setVisible(false);
            customerDetailBox.setManaged(false);
        }
    }

    @FXML
    private void saveCustomer() {
        if (selectedCustomer == null) return;
        try {
            String newName  = editFullName.getText().trim();
            String newPhone = editPhone.getText().trim();
            if (!newName.equals(selectedCustomer.getFullname()))
                App.managerService.updateCustomerFullName(selectedCustomer.getUserId(), newName);
            if (!newPhone.equals(selectedCustomer.getPhone()))
                App.managerService.updateCustomerPhone(selectedCustomer.getUserId(), newPhone);
            showMsg(customerActionMsg, "Customer updated successfully.", false);
            loadAllCustomers();
        } catch (Exception e) { showMsg(customerActionMsg, e.getMessage(), true); }
    }

    @FXML
    private void deleteCustomer() {
        if (selectedCustomer == null) return;
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete customer \"" + selectedCustomer.getUsername() + "\"? This cannot be undone.",
                ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText("Confirm Delete");
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                try {
                    App.managerService.deleteCustomer(selectedCustomer.getUserId());
                    customerDetailBox.setVisible(false);
                    customerDetailBox.setManaged(false);
                    selectedCustomer = null;
                    loadAllCustomers();
                    showMsg(customerActionMsg, "Customer deleted.", false);
                } catch (Exception e) { showMsg(customerActionMsg, e.getMessage(), true); }
            }
        });
    }

    // =========================================================================
    // Transactions
    // =========================================================================

    @FXML
    private void loadTransactions() {
        String accNum = txSearchField.getText().trim();
        if (accNum.isEmpty()) return;
        try {
            List<Transaction> list = App.managerService.viewAccountTransactions(accNum);
            transactionsTable.setItems(FXCollections.observableArrayList(list));
        } catch (Exception e) {
            transactionsTable.setItems(FXCollections.emptyObservableList());
        }
    }

    // =========================================================================
    // Fund Transfer
    // =========================================================================

    @FXML
    private void handleDeposit() {
        try {
            String acc = depositAccField.getText().trim();
            double amt = Double.parseDouble(depositAmtField.getText().trim());
            String manager = App.authService.getCurrentSession().getUsername();
            App.managerService.managerDeposit(acc, amt, manager);
            showMsg(depositMsg, String.format("Deposited $%.2f to %s", amt, acc), false);
            depositAccField.clear(); depositAmtField.clear();
        } catch (NumberFormatException e) {
            showMsg(depositMsg, "Invalid amount.", true);
        } catch (Exception e) {
            showMsg(depositMsg, e.getMessage(), true);
        }
    }

    @FXML
    private void handleWithdraw() {
        try {
            String acc = withdrawAccField.getText().trim();
            double amt = Double.parseDouble(withdrawAmtField.getText().trim());
            String manager = App.authService.getCurrentSession().getUsername();
            App.managerService.managerWithdraw(acc, amt, manager);
            showMsg(withdrawMsg, String.format("Withdrew $%.2f from %s", amt, acc), false);
            withdrawAccField.clear(); withdrawAmtField.clear();
        } catch (NumberFormatException e) {
            showMsg(withdrawMsg, "Invalid amount.", true);
        } catch (Exception e) {
            showMsg(withdrawMsg, e.getMessage(), true);
        }
    }

    @FXML
    private void handleTransfer() {
        try {
            String from = transferFromField.getText().trim();
            String to   = transferToField.getText().trim();
            double amt  = Double.parseDouble(transferAmtField.getText().trim());
            String manager = App.authService.getCurrentSession().getUsername();
            App.managerService.managerTransfer(from, to, amt, manager);
            showMsg(transferMsg, String.format("Transferred $%.2f from %s to %s", amt, from, to), false);
            transferFromField.clear(); transferToField.clear(); transferAmtField.clear();
        } catch (NumberFormatException e) {
            showMsg(transferMsg, "Invalid amount.", true);
        } catch (Exception e) {
            showMsg(transferMsg, e.getMessage(), true);
        }
    }

    // =========================================================================
    // Logout
    // =========================================================================

    @FXML
    private void handleLogout() {
        App.authService.logout();
        App.switchScene("login.fxml");
    }

    // =========================================================================
    // Table column setup
    // =========================================================================

    private void setupTableColumns() {
        // Overview table
        colAccNum.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        colHolder.setCellValueFactory(new PropertyValueFactory<>("holderName"));
        colType.setCellValueFactory(new PropertyValueFactory<>("accountType"));
        colBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("accountStatus"));

        // Accounts table
        aColNum.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        aColHolder.setCellValueFactory(new PropertyValueFactory<>("holderName"));
        aColEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        aColType.setCellValueFactory(new PropertyValueFactory<>("accountType"));
        aColBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        aColStatus.setCellValueFactory(new PropertyValueFactory<>("accountStatus"));

        // Customers table
        cColId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        cColUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        cColFullName.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        cColPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        cColDob.setCellValueFactory(new PropertyValueFactory<>("dOB"));
        cColRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Transactions table
        tColId.setCellValueFactory(new PropertyValueFactory<>("transactionID"));
        tColType.setCellValueFactory(new PropertyValueFactory<>("type"));
        tColAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        tColFrom.setCellValueFactory(new PropertyValueFactory<>("fromAccount"));
        tColTo.setCellValueFactory(new PropertyValueFactory<>("toAccount"));
        tColStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tColDate.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    private void showMsg(Label lbl, String msg, boolean isError) {
        lbl.setText(msg);
        lbl.getStyleClass().removeAll("op-message-error");
        if (isError) lbl.getStyleClass().add("op-message-error");
        lbl.setVisible(true);
        lbl.setManaged(true);
    }

    private void hideMsg(Label lbl) {
        lbl.setVisible(false);
        lbl.setManaged(false);
    }
}

