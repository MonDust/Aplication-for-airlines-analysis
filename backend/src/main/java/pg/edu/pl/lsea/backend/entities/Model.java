package pg.edu.pl.lsea.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A class representing a tracked model of an aircraft
 */
@Setter
@Getter
@Entity
@Table(
        name = "models",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name"})
)
public class Model implements Cloneable, Comparable<Model> {
    /**
     * This value is generated automatically during object creation
     * and is added by the JPA (Java Persistence API).
     */
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Aircraft> aircrafts = new ArrayList<>();

    /**
     * Creates a model object.
     */
    public Model(String name) {
        this.name = name;
    }

    /**
     * Empty constructor needed for cloning
     */
    public Model() {
        this.name = "";
    }

    /**
     * Class for comparing models based on aircrafts size.
     */
    @Override
    public int compareTo(Model o) {
        return o.aircrafts.size() - aircrafts.size();
    }


    /**
     * Printing model.
     * @return Model with all fields in a string format.
     */
    @Override
    public String toString() {
        return "Model{" +
               "name='" + name + "'" +
               ", aircrafts.size() =" + aircrafts.size() +
               "}";
    }



    @Override
    public Model clone() {
        Model newModel = new Model();
        newModel.setId(id);
        newModel.setName(name);
        newModel.setAircrafts(aircrafts);
        return newModel;
    }

    /**
     * A method that compares model to another object and determines if they are equal based on its name.
     * @param o the objects to compare the model to
     * @return true if the compared objects are considered equal, false if they are different
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Model model = (Model) o;
        return Objects.equals(name, model.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

