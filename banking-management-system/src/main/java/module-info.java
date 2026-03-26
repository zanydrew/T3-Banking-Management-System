module com.team4 {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    // Database
    requires java.sql;
    requires mysql.connector.j;
    requires java.management;     // or mysql.connector.java


    // Make controllers accessible to JavaFX
    opens com.team4 to javafx.fxml;
    opens com.team4.controller to javafx.fxml;

    // Allow JavaFX reflection access to ALL model packages
    opens com.team4.model.transaction to javafx.base, javafx.controls;
    opens com.team4.model.account     to javafx.base, javafx.controls;
    opens com.team4.model.user        to javafx.base, javafx.controls;
    opens com.team4.session  to javafx.base, javafx.controls;

    exports com.team4;
    exports com.team4.controller;
    exports com.team4.app;
    opens com.team4.app to javafx.fxml;

}
