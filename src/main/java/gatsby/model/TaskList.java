package gatsby.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gatsby.storage.Storage;

/**
 * Owns Gatsby's in-memory collection of tasks.
 *
 * Keeping list operations here gives the rest of the program one place to
 * manage task storage, while still allowing {@link Storage} to save a read-only
 * view of the current tasks.
 */
public class TaskList {
    /** The tasks currently known to Gatsby, in the order they were added. */
    private final ArrayList<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing the supplied tasks.
     *
     * @param initialTasks tasks recovered from storage
     */
    public TaskList(List<Task> initialTasks) {
        this.tasks = new ArrayList<>(initialTasks);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task the task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task at a zero-based position.
     *
     * @param index the zero-based position
     * @return the task at that position
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Removes and returns the task at a zero-based position.
     *
     * @param index the zero-based position
     * @return the removed task
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns a read-only view for persistence.
     *
     * @return the current tasks as an unmodifiable list
     */
    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }

    /**
     * Returns whether the list has no tasks.
     *
     * @return true when the list is empty
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return the task count
     */
    public int size() {
        return tasks.size();
    }
}
