package models;

public class Reservation {
    private int id;
    private int userId;
    private int logementId;
    private String dateDebut;
    private String dateFin;
    private double prixTotal;

    public Reservation(int id, int userId, int logementId, String dateDebut, String dateFin, double prixTotal) {
        this.id = id;
        this.userId = userId;
        this.logementId = logementId;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.prixTotal = prixTotal;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getLogementId() { return logementId; }
    public String getDateDebut() { return dateDebut; }
    public String getDateFin() { return dateFin; }
    public double getPrixTotal() { return prixTotal; }
}
