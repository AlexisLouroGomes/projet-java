package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;

public class TableViewFX extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showUserView(); // Charger la vue par défaut (Users)
    }

    private void showUserView() {
        loadView("/views/User.fxml", "Liste des Utilisateurs");
    }

    private void showReservationView() {
        loadView("/views/Reservation.fxml", "Liste des Réservations");
    }

    private void showLogementView() {
        loadView("/views/Logement.fxml", "Liste des Logements");
    }

    private void loadView(String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));

            // Création de la barre de menu
            MenuBar menuBar = new MenuBar();
            Menu menu = new Menu("Navigation");

            MenuItem usersItem = new MenuItem("Utilisateurs");
            usersItem.setOnAction(e -> showUserView());

            MenuItem reservationsItem = new MenuItem("Réservations");
            reservationsItem.setOnAction(e -> showReservationView());

            MenuItem logementsItem = new MenuItem("Logements");
            logementsItem.setOnAction(e -> showLogementView());

            menu.getItems().addAll(usersItem, reservationsItem, logementsItem);
            menuBar.getMenus().add(menu);

            VBox layout = new VBox(menuBar, root);
            Scene scene = new Scene(layout, 600, 400);

            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
