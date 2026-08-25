import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task{
    private LocalDateTime by;

    public Deadline(String description, LocalDateTime by){
        super(description);
        this.by = by;
    }

    @Override
    public String toString(){
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm:ss");
        String formattedDeadline = this.by.format(outputFormatter);
        return "[D]" + super.toString() + " (by: " + formattedDeadline + ")";
    }

    @Override
    public String toFileFormat(){
        // Save the ISO value so Storage can reload it without depending on the
        // human-readable format used by toString().
        return "D | " + super.toFileFormat() + " | " + this.by;
    }
}
