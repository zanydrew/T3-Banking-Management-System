module com.team4 {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // Database
    requires java.sql;
    requires mysql.connector.j;     // or mysql.connector.java


    // Make controllers accessible to JavaFX
    opens com.team4 to javafx.fxml;
    opens com.team4.controller to javafx.fxml;

    exports com.team4;
    exports com.team4.controller;
    exports com.team4.app;
    opens com.team4.app to javafx.fxml;

}
