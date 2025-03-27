module TableViewFX {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens application to javafx.graphics;
    opens controllers to javafx.fxml;
    opens models to javafx.base;
}
