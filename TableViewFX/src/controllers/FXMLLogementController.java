package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Logement;
import database.DbConnection;

import java.sql.*;

public class FXMLLogementController {
    @FXML private TableView<Logement> logementTable;
    @FXML private TableColumn<Logement, Integer> idColumn;
    @FXML private TableColumn<Logement, String> nomColumn;
    @FXML private TableColumn<Logement, Integer> chambresColumn;
    @FXML private TableColumn<Logement, Integer> sallesDeBainColumn;
    @FXML private TableColumn<Logement, Double> prixParNuitColumn;

    @FXML private TextField nomField;
    @FXML private TextField chambresField;
    @FXML private TextField sallesDeBainField;
    @FXML private TextField prixParNuitField;
    @FXML private Button addLogementButton;
    @FXML private Button updateLogementButton;
    @FXML private Button deleteLogementButton;

    private ObservableList<Logement> logementList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadLogements();

        logementTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nomField.setText(newSelection.getNom());
                chambresField.setText(String.valueOf(newSelection.getChambres()));
                sallesDeBainField.setText(String.valueOf(newSelection.getSallesDeBain()));
                prixParNuitField.setText(String.valueOf(newSelection.getPrixParNuit()));
            }
        });
    }

    private void loadLogements() {
        logementList.clear();
        Connection conn = DbConnection.getConnection();
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM logements");

                while (rs.next()) {
                    logementList.add(new Logement(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getInt("chambres"),
                        rs.getInt("salles_de_bain"),
                        rs.getDouble("prix_par_nuit")
                    ));
                }

                idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
                chambresColumn.setCellValueFactory(new PropertyValueFactory<>("chambres"));
                sallesDeBainColumn.setCellValueFactory(new PropertyValueFactory<>("sallesDeBain"));
                prixParNuitColumn.setCellValueFactory(new PropertyValueFactory<>("prixParNuit"));

                logementTable.setItems(logementList);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void addLogement() {
        Connection conn = DbConnection.getConnection();
        if (conn != null) {
            try {
                String query = "INSERT INTO logements (nom, chambres, salles_de_bain, prix_par_nuit) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, nomField.getText());
                stmt.setInt(2, Integer.parseInt(chambresField.getText()));
                stmt.setInt(3, Integer.parseInt(sallesDeBainField.getText()));
                stmt.setDouble(4, Double.parseDouble(prixParNuitField.getText()));
                stmt.executeUpdate();

                showAlert("Succès", "Logement ajouté !");
                loadLogements();
                clearFields();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void updateLogement() {
        Logement selected = logementTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner un logement !");
            return;
        }

        Connection conn = DbConnection.getConnection();
        if (conn != null) {
            try {
                String query = "UPDATE logements SET nom=?, chambres=?, salles_de_bain=?, prix_par_nuit=? WHERE id=?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, nomField.getText());
                stmt.setInt(2, Integer.parseInt(chambresField.getText()));
                stmt.setInt(3, Integer.parseInt(sallesDeBainField.getText()));
                stmt.setDouble(4, Double.parseDouble(prixParNuitField.getText()));
                stmt.setInt(5, selected.getId());
                stmt.executeUpdate();

                showAlert("Succès", "Logement modifié !");
                loadLogements();
                clearFields();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void deleteLogement() {
        Logement selected = logementTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner un logement !");
            return;
        }

        Connection conn = DbConnection.getConnection();
        if (conn != null) {
            try {
                String query = "DELETE FROM logements WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, selected.getId());
                stmt.executeUpdate();

                showAlert("Succès", "Logement supprimé !");
                loadLogements();
                clearFields();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearFields() {
        nomField.clear();
        chambresField.clear();
        sallesDeBainField.clear();
        prixParNuitField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
