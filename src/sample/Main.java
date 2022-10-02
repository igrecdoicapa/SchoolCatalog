package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 600, 675));
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();
        SQLServerDriver datasource = new SQLServerDriver();
        if (!datasource.getInstance().open()) {
            System.out.println("Can't open datasource");
            Platform.exit();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        SQLServerDriver.getInstance().close();
    }

    public static void main(String[] args) {

        launch(args);
    }
}
