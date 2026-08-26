package gatsby.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {
    private LocalDateTime from;
    private LocalDateTime to;

    public Event(String description, LocalDateTime from, LocalDateTime to){
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString(){
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm:ss");
        String formattedStart = this.from.format(outputFormatter);
        String formattedEnd = this.to.format(outputFormatter);
        return "[E]" + super.toString() + " (from: " + formattedStart + " to: " + formattedEnd + ")";
    }

    @Override
    public String toFileFormat(){
        return "E | " + super.toFileFormat() + " | " + this.from + " | " + this.to;
    }
}
