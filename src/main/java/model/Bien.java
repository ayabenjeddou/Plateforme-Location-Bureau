package model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name = "bien")
public class Bien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;
    
    private String categorie;

    private int capacite;

    private String localisation;

    private String equipements;

    private boolean active;

    private String imageUrl;
    
    @Column(length = 1000)
    private String description;

    public Bien() {
    }

    public Bien(Long id, String nom, int capacite, String localisation, String equipements, boolean active, String imageUrl, String description) {
        this.id = id;
        this.nom = nom;
        this.capacite = capacite;
        this.localisation = localisation;
        this.equipements = equipements;
        this.active = active;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getEquipements() {
        return equipements;
    }

    public void setEquipements(String equipements) {
        this.equipements = equipements;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Column(name = "prix_par_heure")
    private Double prixParHeure;

    public Double getPrixParHeure() {
        return prixParHeure == null ? 0.0 : prixParHeure;
    }

    public void setPrixParHeure(Double prixParHeure) {
        this.prixParHeure = prixParHeure;
    }

	public String getCategorie() {
		return categorie;
	}

	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}