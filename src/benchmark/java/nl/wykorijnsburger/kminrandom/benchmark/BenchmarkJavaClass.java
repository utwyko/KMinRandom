package nl.wykorijnsburger.kminrandom.benchmark;

@SuppressWarnings("unused")
public class BenchmarkJavaClass {

    private final String string;
    private final int number;
    private long timestamp = 0L;

    public BenchmarkJavaClass(String string, int number, long timestamp) {
        this.string = string;
        this.number = number;
        this.timestamp = timestamp;
    }

    public BenchmarkJavaClass(String string, int number) {
        this.string = string;
        this.number = number;
    }

    public String getString() {
        return string;
    }

    public int getNumber() {
        return number;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
