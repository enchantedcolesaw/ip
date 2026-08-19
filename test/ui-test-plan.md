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
