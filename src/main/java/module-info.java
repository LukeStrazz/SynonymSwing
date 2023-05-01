module com.example.babygpt {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens p1 to javafx.fxml;
    exports p1;
}