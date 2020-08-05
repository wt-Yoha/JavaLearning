package ObjectSerialize;

import com.sun.security.jgss.GSSUtil;
import org.junit.Test;

import java.io.*;

public class JavaMethod {
    public static String filename = "student.txt";
    public static void serialize(Student student, String filename) throws IOException {
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename));
        outputStream.writeObject(student);
        System.out.println("Task done");
    }

    public static void deserialize(String filename) throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename));
        Object o = inputStream.readObject();
        if (o instanceof Student) {
            System.out.println(o);
            System.out.println(((Student) o).score);
        }
    }

    @Test
    public void pipeline1() throws IOException, ClassNotFoundException {
        Student student = new Student("老王", '男');
        Student.classId = 2;
        Pencil pencil = new Pencil();
        pencil.brand = "派克";
        Teacher teacher = new Teacher();
        teacher.name = "张老师";
        teacher.pencil = pencil;
        student.teacher = teacher;

        serialize(student, filename);
        deserialize(filename);
    }

    @Test
    public void pipeline2() throws IOException, ClassNotFoundException {
        deserialize(filename);
    }

}
