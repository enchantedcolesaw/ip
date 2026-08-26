package gatsby.testutil;

import gatsby.ui.Ui;

import java.util.ArrayList;
import java.util.List;

/** Captures command messages without depending on console output in a test. */
public class RecordingUi extends Ui {
    private final List<String> messages = new ArrayList<>();

    /** Returns every message printed by the command so far. */
    public List<String> messages() {
        return List.copyOf(messages);
    }

    @Override
    public void printLine(String message) {
        messages.add(message);
    }
}
