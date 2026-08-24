# Gatsby UI test plan

The tests below exercise Gatsby through standard input. Each expected-output line must appear in the program output in the listed order. The test runner ignores the banner, separators, and indentation, while still checking the meaningful output lines.

## Test 1: Create and list all Level-4 task types

Aim: Verify that ToDos, deadlines, and events are instantiated with the correct descriptions and date/time strings.

Input:
```text
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
list
bye
```

Expected output:
```text
[T][ ] borrow book
[D][ ] return book (by: Sunday)
[E][ ] project meeting (from: Mon 2pm to: 4pm)
```

## Test 2: Preserve multi-word descriptions

Aim: Verify that the description can contain more than two words and is not truncated by command parsing.

Input:
```text
todo borrow a really thick book
deadline submit the final software engineering report /by Friday 5pm
event team project discussion /from Monday afternoon /to Tuesday morning
list
bye
```

Expected output:
```text
[T][ ] borrow a really thick book
[D][ ] submit the final software engineering report (by: Friday 5pm)
[E][ ] team project discussion (from: Monday afternoon to: Tuesday morning)
```

## Test 3: Mark and unmark typed tasks

Aim: Verify that inherited mark and unmark behavior works for a deadline and an event.

Input:
```text
todo read book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
mark 2
unmark 2
mark 3
list
bye
```

Expected output:
```text
[D][X] return book (by: Sunday)
[E][X] project meeting (from: Mon 2pm to: 4pm)
```

## Test 4: Saving to disk does not change the console UI

Aim: Verify that automatically saving the task list after every change (add, mark, delete) is silent, so the normal add/mark/delete/list output is unchanged and no error message about saving appears.

Input:
```text
todo read book
deadline return book /by June 6th
event project meeting /from Aug 6th 2pm /to 4pm
mark 1
delete 2
list
bye
```

Expected output:
```text
Got it. I've added this task:
[T][ ] read book
Now you have 1 task in the list.
Nice! I've marked this task as done:
[T][X] read book
Noted. I've removed this task:
[D][ ] return book (by: June 6th)
Now you have 2 tasks in the list.
Here are the tasks in your list:
1. [T][X] read book
2. [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
```

## Test 5: Start with no save file

Aim: Verify the first-run state, as on a freshly cloned copy of the project: neither the `data` folder nor `data/gatsby.txt` exists. The list must start empty with no error or warning, and the first added task must create the folder and the file. The test runner deletes the whole `data` folder before each case, so every case starts from this state.

Input:
```text
list
todo read book
list
bye
```

Expected output:
```text
Here are the tasks in your list:
There's nothing here yet! Go ahead and add any tasks you'd like! :)
Got it. I've added this task:
[T][ ] read book
Here are the tasks in your list:
1. [T][ ] read book
```

## Test 6: Reject empty and malformed task details

Aim: Verify that commands missing their description, date, or keyword are rejected with a specific message, and that the chatbot keeps running afterwards.

Input:
```text
todo
todo    
deadline return book
deadline /by Sunday
deadline return book /by
event project meeting
event project meeting /from Mon 2pm
list
bye
```

Expected output:
```text
son the description of a todo cannot be empty -_-!
son the description of a todo cannot be empty -_-!
son there's no name or deadline for this deadline -_-!
son this deadline has no description -_-!
son this deadline has no date after /by -_-!
son there's no event name/timing for this event -_-!
son this event has no end time, it's infinite! -_-!
There's nothing here yet! Go ahead and add any tasks you'd like! :)
```

## Test 7: Reject bad task numbers

Aim: Verify that mark, unmark, and delete report a missing number, a non-numeric number, an out-of-range number, and an empty list, each with its own message, without ending the session.

Input:
```text
mark 1
mark
todo read book
mark abc
mark 0
mark 5
unmark
delete
delete 99
list
bye
```

Expected output:
```text
OOPS! Your list is empty, so there's no task 1!
OOPS! We can't be marking nothing as done!
Got it. I've added this task:
OOPS! "abc" isn't a task number! :(
OOPS! There's no task 0! Pick a number from 1 to 1.
OOPS! There's no task 5! Pick a number from 1 to 1.
OOPS! We can't be marking nothing as undone!
OOPS! How do I even delete nothing??
OOPS! There's no task 99! Pick a number from 1 to 1.
1. [T][ ] read book
```

## Test 8: Handle blank input, unknown commands, and repeated marking

Aim: Verify that an unrecognised command is answered helpfully, and that marking an already-done task says so instead of pretending it just changed. (Blank input is covered by manual check C, because the runner drops empty lines from the input block.)

Input:
```text
blah blah
todo read book
mark 1
mark 1
unmark 1
unmark 1
bye
```

Expected output:
```text
Wait I don't recognise that yet :(
I know: todo, deadline, event, list, mark, unmark, delete, bye.
Nice! I've marked this task as done:
That one was already done, but sure:
OK, I've marked this task as not done yet:
That one wasn't done yet, but sure:
```

## Test 9: Reject the field separator in descriptions

Aim: Verify that a description containing the save file's field separator is rejected, so a saved line can never become ambiguous.

Input:
```text
todo read book | and nap
list
bye
```

Expected output:
```text
OOPS! Please leave out the "|" character; I use it to separate fields in my save file.
There's nothing here yet! Go ahead and add any tasks you'd like! :)
```

# Manual checks

These are not run by the automated runner, because the runner deletes `data/gatsby.txt` before every case to keep cases independent.

### Manual check A: Reload the saved tasks in a new run

Aim: Verify that tasks saved by an earlier run are read back at startup, including done status and date/time fields.

Setup: put this in `data/gatsby.txt`, then start Gatsby and enter `list`.

```text
T | 1 | read book
D | 0 | return book | June 6th
E | 0 | project meeting | Aug 6th 2pm | 4pm
```

Expected output:
```text
1. [T][X] read book
2. [D][ ] return book (by: June 6th)
3. [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
```

### Manual check B: Skip a corrupted line

Aim: Verify that one unreadable line does not discard the rest of the saved list.

Setup: put this in `data/gatsby.txt`, then start Gatsby and enter `list`.

```text
T | 1 | read book
X | this line is broken
D | 0 | return book | June 6th
```

Expected output:
```text
OOPS! I skipped a line I couldn't read in my save file: X | this line is broken
1. [T][X] read book
2. [D][ ] return book (by: June 6th)
```

### Manual check C: Blank input

Aim: Verify that pressing Enter on an empty line is answered helpfully instead of being treated as an unknown command. Run Gatsby and press Enter twice, once on an empty line and once after typing only spaces.

Expected output (once per blank line):
```text
You didn't type anything! Try "todo read book" or "list".
```

### Manual check D: The data folder is blocked

Aim: Verify that Gatsby explains itself instead of crashing when it cannot create its data folder.

Setup: from the project root, remove the folder and put a plain file in its place, then start Gatsby and add a task.

```bash
rm -rf data && touch data
```

Expected output:
```text
OOPS! "data" already exists as a file, so I have nowhere to save. Rename or remove it and I'll save again.
Got it. I've added this task:
```

The task is still added to the in-memory list, so the session stays usable; only saving is skipped. Remember to `rm data` afterwards.
