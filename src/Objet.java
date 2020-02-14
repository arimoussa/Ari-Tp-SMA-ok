
public class Objet {
    private Character name;

    public Objet(Character name) {
        this.name = name;
    }

    public boolean equals(Objet o) {
        return (o.getName().equals(name));
    }

    public Character getName() {
        return name;
    }
}