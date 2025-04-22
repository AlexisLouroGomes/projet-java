package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.User;
import database.DbConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class FXMLUserController {
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> idColumn;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, String> createdAtColumn;

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;

    @FXML private Button addUserButton;
    @FXML private Button updateUserButton;
    @FXML private Button deleteUserButton;

    private ObservableList<User> userList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        roleComboBox.setItems(FXCollections.observableArrayList("user", "admin"));
        roleComboBox.setValue("user");

        loadUsers();

        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                usernameField.setText(newSelection.getUsername());
                emailField.setText(newSelection.getEmail());
                roleComboBox.setValue(newSelection.getRole());
                passwordField.setText(""); // Ne jamais afficher un mot de passe hashé
            }
        });
    }

    private void loadUsers() {
        userList.clear();
        Connection conn = DbConnection.getConnection();
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM users");

                while (rs.next()) {
                    userList.add(new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("created_at"),
                            rs.getString("role")
                    ));
                }

                idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
                emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
                createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

                userTable.setItems(userList);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void addUser() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        Connection conn = DbConnection.getConnection();
        if (conn != null) {
            try {
                String query = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, username);
                stmt.setString(2, email);
                stmt.setString(3, hashedPassword);
                stmt.setString(4, role);
                stmt.executeUpdate();

                showAlert("Succès", "Utilisateur ajouté !");
                loadUsers();
                clearFields();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void updateUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert("Erreur", "Veuillez sélectionner un utilisateur !");
            return;
        }

        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();

        if (username.isEmpty() || email.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        Connection conn = DbConnection.getConnection();
        if (conn != null) {
            try {
                String query;
                PreparedStatement stmt;

                if (!password.isEmpty()) {
                    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                    query = "UPDATE users SET username = ?, email = ?, password = ?, role = ? WHERE id = ?";
                    stmt = conn.prepareStatement(query);
                    stmt.setString(1, username);
                    stmt.setString(2, email);
                    stmt.setString(3, hashedPassword);
                    stmt.setString(4, role);
                    stmt.setInt(5, selectedUser.getId());
                } else {
                    query = "UPDATE users SET username = ?, email = ?, role = ? WHERE id = ?";
                    stmt = conn.prepareStatement(query);
                    stmt.setString(1, username);
                    stmt.setString(2, email);
                    stmt.setString(3, role);
                    stmt.setInt(4, selectedUser.getId());
                }

                stmt.executeUpdate();

                showAlert("Succès", "Utilisateur modifié !");
                loadUsers();
                clearFields();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void deleteUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert("Erreur", "Veuillez sélectionner un utilisateur !");
            return;
        }

        Connection conn = DbConnection.getConnection();
        if (conn != null) {
            try {
                String query = "DELETE FROM users WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, selectedUser.getId());
                stmt.executeUpdate();

                showAlert("Succès", "Utilisateur supprimé !");
                loadUsers();
                clearFields();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearFields() {
        usernameField.clear();
        emailField.clear();
        passwordField.clear();
        roleComboBox.setValue("user");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
