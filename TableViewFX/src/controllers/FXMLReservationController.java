package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Reservation;
import database.DbConnection;

import java.sql.*;

public class FXMLReservationController {
    @FXML private TableView<Reservation> reservationTable;
    @FXML private TableColumn<Reservation, Integer> idColumn;
    @FXML private TableColumn<Reservation, Integer> userIdColumn;
    @FXML private TableColumn<Reservation, Integer> logementIdColumn;
    @FXML private TableColumn<Reservation, String> dateDebutColumn;
    @FXML private TableColumn<Reservation, String> dateFinColumn;
    @FXML private TableColumn<Reservation, Double> prixTotalColumn;

    @FXML private TextField userIdField;
    @FXML private TextField logementIdField;
    @FXML private DatePicker dateDebutField;
    @FXML private DatePicker dateFinField;
    @FXML private TextField prixTotalField;
    @FXML private Button addReservationButton;
    @FXML private Button updateReservationButton;
    @FXML private Button deleteReservationButton;

    private ObservableList<Reservation> reservationList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadReservations();

        reservationTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                userIdField.setText(String.valueOf(newSelection.getUserId()));
                logementIdField.setText(String.valueOf(newSelection.getLogementId()));
                dateDebutField.setValue(java.time.LocalDate.parse(newSelection.getDateDebut()));
                dateFinField.setValue(java.time.LocalDate.parse(newSelection.getDateFin()));
                prixTotalField.setText(String.valueOf(newSelection.getPrixTotal()));
            }
        });
    }

    private void loadReservations() {
        reservationList.clear();
        Connection conn = DbConnection.getConnection();
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM reservations");

                while (rs.next()) {
                    reservationList.add(new Reservation(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("logement_id"),
                        rs.getString("date_debut"),
                        rs.getString("date_fin"),
                        rs.getDouble("prix_total")
                    ));
                }

                idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
                logementIdColumn.setCellValueFactory(new PropertyValueFactory<>("logementId"));
                dateDebutColumn.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
                dateFinColumn.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
                prixTotalColumn.setCellValueFactory(new PropertyValueFactory<>("prixTotal"));

                reservationTable.setItems(reservationList);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void addReservation() {
        Connection conn = DbConnection.getConnection();
        if (conn != null) {
            try {
                String query = "INSERT INTO reservations (user_id, logement_id, date_debut, date_fin, prix_total) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, Integer.parseInt(userIdField.getText()));
                stmt.setInt(2, Integer.parseInt(logementIdField.getText()));
                stmt.setString(3, dateDebutField.getValue().toString());
                stmt.setString(4, dateFinField.getValue().toString());
                stmt.setDouble(5, Double.parseDouble(prixTotalField.getText()));
                stmt.executeUpdate();

                showAlert("Succès", "Réservation ajoutée !");
                loadReservations();
                clearFields();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void updateReservation() {
        Reservation selected = reservationTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner une réservation !");
            return;
        }

        Connection conn = DbConnection.getConnection();
        if (conn != null) {
            try {
                String query = "UPDATE reservations SET user_id=?, logement_id=?, date_debut=?, date_fin=?, prix_total=? WHERE id=?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, Integer.parseInt(userIdField.getText()));
                stmt.setInt(2, Integer.parseInt(logementIdField.getText()));
                stmt.setString(3, dateDebutField.getValue().toString());
                stmt.setString(4, dateFinField.getValue().toString());
                stmt.setDouble(5, Double.parseDouble(prixTotalField.getText()));
                stmt.setInt(6, selected.getId());
                stmt.executeUpdate();

                showAlert("Succès", "Réservation modifiée !");
                loadReservations();
                clearFields();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void deleteReservation() {
        Reservation selected = reservationTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner une réservation !");
            return;
        }

        Connection conn = DbConnection.getConnection();
        if (conn != null) {
            try {
                String query = "DELETE FROM reservations WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, selected.getId());
                stmt.executeUpdate();

                showAlert("Succès", "Réservation supprimée !");
                loadReservations();
                clearFields();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearFields() {
        userIdField.clear();
        logementIdField.clear();
        dateDebutField.setValue(null);
        dateFinField.setValue(null);
        prixTotalField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
