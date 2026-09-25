package main.java.Running.type;

public class Choice {

    private final String text;
    private final String target;

    public Choice(String text, String target) {
        this.text = text;
        this.target = target;
    }

    public String getText() {
        return text;
    }

    public String getTarget() {
        return target;
    }
}