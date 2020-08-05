package ObjectSerialize;

import java.io.Serializable;

public class Pencil implements Serializable {
    public String brand;

    @Override
    public String toString() {
        return "Pencil{" +
                "brand='" + brand + '\'' +
                '}';
    }
}
