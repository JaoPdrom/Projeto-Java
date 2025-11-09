package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

public class VboxMainController implements Initializable {

    @FXML private MenuItem menuItemMainAtendenteCliente;

    @FXML private AnchorPane anchorPaneMainVbox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Pode deixar vazio; a tela principal abre sem conteúdo
        // e o handler abaixo carrega o Cliente quando o menu é clicado.
    }

    @FXML
    public void handlerMenuItemMainAtendenteCliente() throws IOException {
        URL fxml = getClass().getResource("/viewfx/ClienteView2.fxml");
        if (fxml == null) {
            throw new IOException("FXML '/viewfx/ClienteView2.fxml' não encontrado no classpath");
        }

        Parent root = FXMLLoader.load(fxml);

        AnchorPane.setTopAnchor(root, 0.0);
        AnchorPane.setRightAnchor(root, 0.0);
        AnchorPane.setBottomAnchor(root, 0.0);
        AnchorPane.setLeftAnchor(root, 0.0);

        anchorPaneMainVbox.getChildren().setAll(root);
    }
}