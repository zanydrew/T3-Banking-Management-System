package com.team4;

import com.team4.config.DatabaseConnection;
import com.team4.dao.AccountDAO;
import com.team4.dao.SessionDAO;
import com.team4.dao.TransactionDAO;
import com.team4.dao.UserDAO;
import com.team4.dao.impl.MySQLAccountDAO;
import com.team4.dao.impl.MySQLSessionDAO;
import com.team4.dao.impl.MySQLTransactionDAO;
import com.team4.dao.impl.MySQLUserDAO;
import com.team4.service.AccountService;
import com.team4.service.AuthService;
import com.team4.service.ManagerService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

/**
 * JavaFX App
 */


public class App extends Application {

    public static AuthService authService;
    public static AccountService accountService;
    public static ManagerService managerService;
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        Connection conn = DatabaseConnection.getConnection();

        UserDAO userDAO        = new MySQLUserDAO(conn);
        SessionDAO sessionDAO     = new MySQLSessionDAO(conn);
        AccountDAO accountDAO     = new MySQLAccountDAO(conn);
        TransactionDAO transactionDAO = new MySQLTransactionDAO(conn);

        accountService = new AccountService(accountDAO, conn);
        authService    = new AuthService(userDAO, sessionDAO, conn);
        managerService = new ManagerService(accountDAO, transactionDAO,
                accountService, userDAO, conn);

        switchScene("login.fxml");
        stage.setTitle("Banking System");
        stage.show();
    }

    public static void switchScene(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    App.class.getResource("/view/" + fxml));  // fixed
            primaryStage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load scene: " + fxml, e);
        }
    }

    @Override
    public void stop() {
        DatabaseConnection.closeConnection();  // added
    }

    public static void main(String[] args) {
        launch(args);
    }
}

//public class App extends Application {
//
//    private static Scene scene;
//
//    @Override
//    public void start(Stage stage) throws IOException {
//        scene = new Scene(loadFXML("primary"), 640, 480);
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    static void setRoot(String fxml) throws IOException {
//        scene.setRoot(loadFXML(fxml));
//    }
//
//    private static Parent loadFXML(String fxml) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
//        return fxmlLoader.load();
//    }
//
//    public static void main(String[] args) {
//        launch();
//    }
//
//}

