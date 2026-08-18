public class Task {
    private String taskName;
    private boolean isDone;

    public Task(String taskName) {
        this.taskName = taskName;
        this.isDone = false;
    }

    public void markDone(){
        this.isDone = true;
    }

    public void markUndone(){
        this.isDone = false;
    }

    @Override
    public String toString(){
        if (this.isDone){
            return "[X] " + this.taskName;
        } else{
            return "[ ] " + this.taskName;
        }
    }
}
