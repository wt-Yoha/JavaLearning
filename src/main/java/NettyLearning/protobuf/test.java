package NettyLearning.protobuf;

interface Func{
    public void func();
}

public class test {
    static int id = 1192;
    String name;

    static class son1 implements Func{
        public void func() {
            System.out.println(id);
        }
    }

    class son2 implements Func{
        public void func() {
            System.out.println(id);
            System.out.println(name);
        }
    }

    public static void main(String[] args) {
        test t = new test();
        t.name = "qer";
        Func son1 = new son1();
        Func son2 = t.new son2();

        son1.func();
        son2.func();
    }
}
