module ru.covariance.mythkeeperpackager {
  requires javafx.fxml;
  requires javafx.controls;
  requires javafx.base;
  requires javafx.graphics;
  requires org.apache.commons.io;
  requires org.json;

  opens ru.covariance.mythkeeperpackager.app to javafx.fxml;

  exports ru.covariance.mythkeeperpackager.app;
  exports ru.covariance.mythkeeperpackager.packager;
}