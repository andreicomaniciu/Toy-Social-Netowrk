module com.example.curs9 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.apache.pdfbox;

    opens com.example.curs9 to javafx.fxml, javafx.base;
    opens com.example.curs9.ubbcluj.map.domain.MyModels to javafx.fxml, javafx.base;
    exports com.example.curs9;
}