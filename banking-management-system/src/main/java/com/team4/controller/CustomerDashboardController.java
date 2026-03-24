package com.team4.controller;


import com.team4.app.App;
import com.team4.model.account.Account;
import com.team4.model.transaction.Transaction;
import com.team4.model.user.User;
import com.team4.session.Session;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CustomerDashboardController {

    // ── Sidebar ──
    @FXML
    private Button btnMyAccount, btnTransact, btnHistory, btnProfile;

    // ── Panels ──
    @FXML private VBox myAccountPanel, transactPanel, historyPanel, profilePanel;

    // ── Top bar ──
    @FXML private Label pageTitle, dateLabel, avatarLabel, customerNameLabel;

    // ── My Account ──
    @FXML private Label mainBalanceLabel, mainAccNumLabel;
    @FXML private Label savingsBalanceLabel, savingsAccNumLabel;
    @FXML private Label totalBalanceLabel;
    @FXML private TableView<Account> myAccountsTable;
    @FXML private TableColumn<Account,String> maColNum, maColType, maColStatus;
    @FXML private TableColumn<Account,Double> maColBalance;

    // ── Transactions ──
    @FXML private ComboBox<String> depositAccCombo, withdrawAccCombo, transferFromCombo;
    @FXML private TextField        depositAmtField, withdrawAmtField, transferToField, transferAmtField;
    @FXML private Label            depositMsg, withdrawMsg, transferMsg;

    // ── History ──
    @FXML private ComboBox<String>       historyAccCombo;
    @FXML private TableView<Transaction> historyTable;
    @FXML private TableColumn<Transaction,String> hColId, hColType, hColFrom, hColTo, hColStatus, hColDate;
    @FXML private TableColumn<Transaction,Double> hColAmount;

    // ── Profile ──
    @FXML private TextField profileUsername, profileFullName, profilePhone, profileDob;
    @FXML private Label     profileMsg;

    // State
    private List<Account> myAccounts;
    private Session        session;
    private User currentUser;

    // =========================================================================
    @FXML
    public void initialize() {
        dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));

        session = App.authService.getCurrentSession();
        if (session != null) {
            customerNameLabel.setText(session.getUsername());
            avatarLabel.setText(String.valueOf(session.getUsername().charAt(0)).toUpperCase());
        }

        setupTableColumns();
        loadMyAccounts();
        loadCurrentUser();
    }

    // =========================================================================
    // Navigation
    // =========================================================================

    private void showOnly(VBox panel, String title, Button navBtn) {
        for (VBox p : new VBox[]{myAccountPanel, transactPanel, historyPanel, profilePanel}) {
            p.setVisible(false);
            p.setManaged(false);
        }
        panel.setVisible(true);
        panel.setManaged(true);
        pageTitle.setText(title);
        for (Button b : new Button[]{btnMyAccount, btnTransact, btnHistory, btnProfile})
            b.getStyleClass().remove("nav-btn-active");
        if (!navBtn.getStyleClass().contains("nav-btn-active"))
            navBtn.getStyleClass().add("nav-btn-active");
    }

    @FXML void showMyAccount() { showOnly(myAccountPanel, "My Account",   btnMyAccount); loadMyAccounts(); }
    @FXML void showTransact()  { showOnly(transactPanel,  "Transactions",  btnTransact);  populateCombos(); }
    @FXML void showHistory()   { showOnly(historyPanel,   "History",       btnHistory);   populateHistoryCombo(); }
    @FXML void showProfile()   { showOnly(profilePanel,   "My Profile",    btnProfile);   loadProfile(); }

    // =========================================================================
    // My Account
    // =========================================================================

    private void loadMyAccounts() {
        if (session == null) return;
        try {
            myAccounts = App.managerService.viewAccountsByUserId(session.getUserId());
            myAccountsTable.setItems(FXCollections.observableArrayList(myAccounts));

            double mainBal = 0, savBal = 0;
            String mainNum = "—", savNum = "—";

            for (Account a : myAccounts) {
                switch (a.getAccountType().name()) {
                    case "MAIN" -> { mainBal = a.getBalance(); mainNum = a.getAccountNumber(); }
                    case "SAVINGS" -> { savBal = a.getBalance(); savNum = a.getAccountNumber(); }
                }
            }

            mainBalanceLabel.setText(String.format("$%.2f", mainBal));
            mainAccNumLabel.setText(mainNum);
            savingsBalanceLabel.setText(String.format("$%.2f", savBal));
            savingsAccNumLabel.setText(savNum);
            totalBalanceLabel.setText(String.format("$%.2f", mainBal + savBal));

        } catch (Exception e) { e.printStackTrace(); }
    }

    // =========================================================================
    // Transactions
    // =========================================================================

    private void populateCombos() {
        if (myAccounts == null) return;
        var nums = FXCollections.observableArrayList(
                myAccounts.stream().map(Account::getAccountNumber).toList());
        depositAccCombo.setItems(nums);
        withdrawAccCombo.setItems(FXCollections.observableArrayList(nums));
        transferFromCombo.setItems(FXCollections.observableArrayList(nums));
    }

    @FXML
    private void handleDeposit() {
        try {
            String acc = depositAccCombo.getValue();
            if (acc == null) { showMsg(depositMsg, "Select an account.", true); return; }
            double amt = Double.parseDouble(depositAmtField.getText().trim());
            App.accountService.deposit(acc, amt);
            showMsg(depositMsg, String.format("Deposited $%.2f successfully.", amt), false);
            depositAmtField.clear();
            loadMyAccounts();
        } catch (NumberFormatException e) {
            showMsg(depositMsg, "Invalid amount.", true);
        } catch (Exception e) {
            showMsg(depositMsg, e.getMessage(), true);
        }
    }

    @FXML
    private void handleWithdraw() {
        try {
            String acc = withdrawAccCombo.getValue();
            if (acc == null) { showMsg(withdrawMsg, "Select an account.", true); return; }
            double amt = Double.parseDouble(withdrawAmtField.getText().trim());
            App.accountService.withdraw(acc, amt);
            showMsg(withdrawMsg, String.format("Withdrew $%.2f successfully.", amt), false);
            withdrawAmtField.clear();
            loadMyAccounts();
        } catch (NumberFormatException e) {
            showMsg(withdrawMsg, "Invalid amount.", true);
        } catch (Exception e) {
            showMsg(withdrawMsg, e.getMessage(), true);
        }
    }

    @FXML
    private void handleTransfer() {
        try {
            String from = transferFromCombo.getValue();
            String to   = transferToField.getText().trim();
            if (from == null) { showMsg(transferMsg, "Select source account.", true); return; }
            if (to.isEmpty()) { showMsg(transferMsg, "Enter destination account.", true); return; }
            double amt = Double.parseDouble(transferAmtField.getText().trim());
            App.accountService.transfer(from, to, amt);
            showMsg(transferMsg, String.format("Transferred $%.2f to %s.", amt, to), false);
            transferToField.clear(); transferAmtField.clear();
            loadMyAccounts();
        } catch (NumberFormatException e) {
            showMsg(transferMsg, "Invalid amount.", true);
        } catch (Exception e) {
            showMsg(transferMsg, e.getMessage(), true);
        }
    }

    // =========================================================================
    // History
    // =========================================================================

    private void populateHistoryCombo() {
        if (myAccounts == null) return;
        historyAccCombo.setItems(FXCollections.observableArrayList(
                myAccounts.stream().map(Account::getAccountNumber).toList()));
    }

    @FXML
    private void loadHistory() {
        String acc = historyAccCombo.getValue();
        if (acc == null) return;
        try {
            List<Transaction> list = App.managerService.viewAccountTransactions(acc);
            historyTable.setItems(FXCollections.observableArrayList(list));
        } catch (Exception e) {
            historyTable.setItems(FXCollections.emptyObservableList());
        }
    }

    // =========================================================================
    // Profile
    // =========================================================================

    private void loadCurrentUser() {
        if (session == null) return;
        try {
            currentUser = App.managerService.viewCustomer(session.getUserId());
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadProfile() {
        if (currentUser == null) return;
        profileUsername.setText(currentUser.getUsername());
        profileFullName.setText(currentUser.getFullname());
        profilePhone.setText(currentUser.getPhone());
        profileDob.setText(currentUser.getdOB());
        hideMsg(profileMsg);
    }

    @FXML
    private void saveProfile() {
        if (currentUser == null) return;
        try {
            String newName  = profileFullName.getText().trim();
            String newPhone = profilePhone.getText().trim();
            if (!newName.equals(currentUser.getFullname()))
                App.managerService.updateCustomerFullName(currentUser.getUserId(), newName);
            if (!newPhone.equals(currentUser.getPhone()))
                App.managerService.updateCustomerPhone(currentUser.getUserId(), newPhone);
            showMsg(profileMsg, "Profile updated successfully.", false);
            loadCurrentUser();
        } catch (Exception e) {
            showMsg(profileMsg, e.getMessage(), true);
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
    // Table setup
    // =========================================================================

    private void setupTableColumns() {
        maColNum.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        maColType.setCellValueFactory(new PropertyValueFactory<>("accountType"));
        maColBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        maColStatus.setCellValueFactory(new PropertyValueFactory<>("accountStatus"));

        hColId.setCellValueFactory(new PropertyValueFactory<>("transactionID"));
        hColType.setCellValueFactory(new PropertyValueFactory<>("type"));
        hColAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        hColFrom.setCellValueFactory(new PropertyValueFactory<>("fromAccount"));
        hColTo.setCellValueFactory(new PropertyValueFactory<>("toAccount"));
        hColStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        hColDate.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
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
