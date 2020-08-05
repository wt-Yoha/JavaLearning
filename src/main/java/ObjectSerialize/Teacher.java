package ObjectSerialize;

import java.io.Serializable;

public class Teacher implements Serializable {
    public String name;
    public Pencil pencil;

    @Override
    public String toString() {
        return "Teacher{" +
                "name='" + name + '\'' +
                ", pencil=" + pencil +
                '}';
    }
}
