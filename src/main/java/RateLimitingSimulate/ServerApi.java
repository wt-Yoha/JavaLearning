package RateLimitingSimulate;

public abstract class ServerApi {
    String path;
    int count = 0;

    ServerApi(String path) {
        this.path = path;
    }

    abstract public void invoke();

    public void api() {
        invoke();
        count++;
    }
}
