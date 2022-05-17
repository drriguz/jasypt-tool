module jasypt.tool {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jasypt;
    requires org.passay;
    opens com.riguz.jasypt to javafx.fxml;
    exports com.riguz.jasypt;
}