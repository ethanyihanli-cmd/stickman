module com.macondo.stickman {
    requires java.desktop;
    requires javafx.controls;
    requires javafx.media;
    requires java.prefs;

    exports com.macondo.stickman;
    exports com.macondo.stickman.controller;
    exports com.macondo.stickman.input;
    exports com.macondo.stickman.model;
    exports com.macondo.stickman.view;
}
