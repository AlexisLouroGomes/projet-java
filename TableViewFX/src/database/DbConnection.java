package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/cvven?serverTimezone=UTC";
    private static final String USER = "root"; // Remplace par ton utilisateur MySQL si différent
    private static final String PASSWORD = ""; // Remplace par ton mot de passe MySQL si nécessaire

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connexion réussie !");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver MySQL introuvable !");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Erreur de connexion : " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }
}
