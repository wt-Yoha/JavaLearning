package ObjectSerialize;

import ObjectSerialize.googleProtoBuf.AddressProtos;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProtoMethod {
    @Test
    public void test() throws IOException {
        AddressProtos.Person john = AddressProtos.Person.newBuilder()
                .setId(123)
                .setName("John Doe")
                .setEmail("joe@gmail.com")
                .addPhones(
                        AddressProtos.Person.PhoneNumber.newBuilder()
                                .setNumber("555-4321")
                                .setType(AddressProtos.Person.PhoneType.HOME)
                ).build();

        System.out.println(john.toByteArray().length);
        System.out.println(john.toString().getBytes().length);

        FileOutputStream outputStream = new FileOutputStream("AddressProtos.txt");
        outputStream.write(john.toByteArray());
        outputStream.close();
    }

    @Test
    public void reload() throws IOException {
        FileInputStream inputStream = new FileInputStream("AddressProtos.txt");
        byte[] bytes = inputStream.readAllBytes();
        AddressProtos.Person person = AddressProtos.Person.parseFrom(bytes);
        System.out.println(person);
    }
}
