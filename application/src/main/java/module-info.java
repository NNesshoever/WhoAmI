module startApp {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;

    opens uiController to javafx.fxml;
    exports startApp;
    exports Dtos;
}