package nl.wykorijnsburger.kminrandom;

public class SampleJavaClass {

    private String string1;
    private String string2 = null;

    public SampleJavaClass(String string1, String string2) {
        this.string1 = string1;
        this.string2 = string2;
    }

    public SampleJavaClass(String string1) {
        this.string1 = string1;
    }

    public String getString1() {
        return string1;
    }

    public String getString2() {
        return string2;
    }
}
