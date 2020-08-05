package ObjectSerialize;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class Student implements Serializable{
    public static int classId;
    public String name;
    public char gender;
    public double score;
    public Teacher teacher;
    private static final long serialVersionUID = 123123132L;

    public Student() {

    }

    public Student(String name, char gender) {
        this.name = name;
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", gender=" + gender +
                ", score=" + score +
                ", teacher=" + teacher +
                '}';
    }

    private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        inputStream.defaultReadObject();
        if (gender != '男' && gender != '女') {
            throw new IllegalArgumentException("性别只能是男或女");
        }
    }
}
