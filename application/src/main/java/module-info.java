module entrypoint {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;

    opens controllers to javafx.fxml;
    exports entrypoint;
    exports models;
}