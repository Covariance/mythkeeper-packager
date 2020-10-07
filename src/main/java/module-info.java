module ru.covariance.mythkeeperpackager {
  requires javafx.fxml;
  requires javafx.controls;
  requires javafx.base;
  requires javafx.graphics;
  requires org.json;
  requires org.apache.commons.io;

  opens ru.covariance.mythkeeperpackager.app to javafx.fxml;

  exports ru.covariance.mythkeeperpackager.app;
  exports ru.covariance.mythkeeperpackager.packager;
}
