module order.processing {
    requires transitive order.storage;
    requires order.threading;
    requires javafaker;
    requires java.sql; // JavaFaker завантажує дані через SnakeYAML, якому потрібен java.sql.Date.

    exports com.nov.order.processing;
}
