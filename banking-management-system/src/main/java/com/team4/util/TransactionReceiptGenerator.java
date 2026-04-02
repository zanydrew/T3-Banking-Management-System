package com.team4.util;


import com.team4.model.transaction.Transaction;
import com.team4.model.transaction.TransactionType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransactionReceiptGenerator {
    private static final String RECEIPTS_DIR = "receipts";

    public static File generate(Transaction transaction, String initiatorName) {
        try {
            // Build folder path: receipts/YYYY-MM-DD/
            String dateFolder = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            File dir = new File(RECEIPTS_DIR + File.separator + dateFolder);
            if (!dir.exists()) dir.mkdirs();

            // Build file name
            String shortId    = transaction.getTransactionID().substring(0, 8);
            String timestamp  = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName   = "TXN_" + shortId + "_" + timestamp + ".txt";
            File   file       = new File(dir, fileName);

            // Write receipt content
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(buildReceiptContent(transaction, initiatorName));
            }

            System.out.println("Receipt saved: " + file.getAbsolutePath());
            return file;

        } catch (IOException e) {
            System.err.println("Failed to generate receipt: " + e.getMessage());
            return null;
        }
    }


    // Receipt content builder

    private static String buildReceiptContent(Transaction transaction, String initiatorName) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String line = "─".repeat(48);
        String doubleLine = "═".repeat(48);

        StringBuilder sb = new StringBuilder();

        sb.append(doubleLine).append("\n");
        sb.append("          iBanking — Transaction Receipt          \n");
        sb.append(doubleLine).append("\n\n");

        // ── Transaction type header ──
        sb.append("  Type     : ").append(formatType(transaction.getType())).append("\n");
        sb.append("  Status   : ").append(transaction.getStatus()).append("\n");
        sb.append("  Date     : ").append(transaction.getTransactionDate().format(fmt)).append("\n");
        sb.append("  Receipt  : ").append(transaction.getTransactionID()).append("\n\n");

        sb.append(line).append("\n");
        sb.append("  TRANSACTION DETAILS\n");
        sb.append(line).append("\n");

        // ── Account details based on type ──
        switch (transaction.getType()) {
            case DEPOSIT -> {
                sb.append("  To Account   : ").append(nvl(transaction.getToAccount())).append("\n");
                sb.append("  Amount       : ").append(formatAmount(transaction.getAmount())).append("\n");
            }
            case WITHDRAW -> {
                sb.append("  From Account : ").append(nvl(transaction.getFromAccount())).append("\n");
                sb.append("  Amount       : ").append(formatAmount(transaction.getAmount())).append("\n");
            }
            case TRANSFER -> {
                sb.append("  From Account : ").append(nvl(transaction.getFromAccount())).append("\n");
                sb.append("  To Account   : ").append(nvl(transaction.getToAccount())).append("\n");
                sb.append("  Amount       : ").append(formatAmount(transaction.getAmount())).append("\n");
            }
        }

        if (transaction.getDescription() != null && !transaction.getDescription().isBlank()) {
            sb.append("  Note         : ").append(transaction.getDescription()).append("\n");
        }

        sb.append("\n").append(line).append("\n");
        sb.append("  AUTHORIZATION\n");
        sb.append(line).append("\n");
        sb.append("  Initiated by : ").append(initiatorName).append("\n");
        sb.append("  Processed    : ").append(LocalDateTime.now().format(fmt)).append("\n\n");

        sb.append(doubleLine).append("\n");
        sb.append("   Thank you for banking with iBanking.\n");
        sb.append("   Keep this receipt for your records.\n");
        sb.append(doubleLine).append("\n");

        return sb.toString();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────────

    private static String formatAmount(double amount) {
        return String.format("$%,.2f", amount);
    }

    private static String formatType(TransactionType type) {
        return switch (type) {
            case DEPOSIT  -> "DEPOSIT  (+)";
            case WITHDRAW -> "WITHDRAW (-)";
            case TRANSFER -> "TRANSFER (→)";
        };
    }

    private static String nvl(String value) {
        return (value == null || value.isBlank() || value.equals("—")) ? "N/A" : value;
    }
}


