package cnam.smb116.tp5.Model;

public class Enseignant {
    public int Id;
    public String Nom;
    public String Prenom;
    public String Courriel;

    @Override
    public String toString() {
        return  Id + "- " + Nom.toUpperCase() + " " + Prenom + ", " + Courriel;
    }

    public Enseignant(int id, String nom, String prenom, String courriel) {
        Id = id;
        Nom = nom;
        Prenom = prenom;
        Courriel = courriel;
    }

    public Enseignant(String nom, String prenom, String courriel) {
        Nom = nom;
        Prenom = prenom;
        Courriel = courriel;
    }

}
