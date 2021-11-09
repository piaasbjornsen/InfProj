module eliteserien.ui {
    requires com.fasterxml.jackson.databind;

    requires java.net.http;

    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;

    requires de.jensd.fx.glyphs.commons;
    requires de.jensd.fx.glyphs.fontawesome;

    requires eliteserien.core;

    opens eliteserien.ui to javafx.graphics, javafx.fxml, javafx.base;
}
