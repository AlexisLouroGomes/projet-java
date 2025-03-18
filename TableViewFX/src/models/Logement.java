package models;

public class Logement {
    private int id;
    private String nom;
    private int chambres;
    private int sallesDeBain;
    private double prixParNuit;

    public Logement(int id, String nom, int chambres, int sallesDeBain, double prixParNuit) {
        this.id = id;
        this.nom = nom;
        this.chambres = chambres;
        this.sallesDeBain = sallesDeBain;
        this.prixParNuit = prixParNuit;
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public int getChambres() { return chambres; }
    public int getSallesDeBain() { return sallesDeBain; }
    public double getPrixParNuit() { return prixParNuit; }
}
