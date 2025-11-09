import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;
import java.net.URL;

public class ClienteApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root;
        try {
            URL url = getClass().getResource("/viewfx/VboxMain.fxml");
            if (url == null) throw new RuntimeException("FXML '/viewfx/ClienteView.fxml' nao encontrado no classpath");
            root = FXMLLoader.load(url);
        } catch (Exception ex) {
            System.err.println("Falha ao carregar FXML do classpath: " + ex.getMessage());
            File f = new File("Projeto/src/viewfx/VboxMain.fxml");
            System.err.println("Tentando fallback via arquivo: " + f.getAbsolutePath());
            root = FXMLLoader.load(f.toURI().toURL());
        }
        Scene scene = new Scene(root, 1100, 700);
        stage.setTitle("Clientes");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
