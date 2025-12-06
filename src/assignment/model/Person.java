package assignment.model;

import java.io.File;
import java.util.Objects;

/**
 * Base class for people (staff, members).
 */
public abstract class Person {

    private String name, ic;
    private int id;

    public Person() {
    }

    public Person(String name, String ic, int id) {
        this.name = name;
        this.ic = ic;
        this.id = id;
    }

    public void setName(String name) {
        this.name = name.toUpperCase();
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getIc() {
        return ic;
    }

    public int getId() {
        return id;
    }

    public boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Person other = (Person) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return Objects.equals(this.ic, other.ic);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.name);
        hash = 37 * hash + Objects.hashCode(this.ic);
        hash = 37 * hash + this.id;
        return hash;
    }
}


