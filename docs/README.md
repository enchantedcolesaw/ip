# Gatsby User Guide

// Update the title above to match the actual product name

// Product screenshot goes here

// Product intro goes here

## Adding deadlines

// Describe the action and its outcome.

// Give examples of usage

Example: `keyword (optional arguments)`

// A description of the expected outcome goes here

```
expected output
```

## Feature ABC

// Feature details


## Feature XYZ

// Feature details

## Finding tasks

Use `find <query>` to search task descriptions.

Search is case-insensitive. A query may contain one or more words; every query
word must appear somewhere in the description, and the words may appear in any
order. Partial matches are supported, so `find boo` and `find ook` both match
`read book`.

Exact word matches appear before partial matches. Results retain their original
task-list numbers, which are also used by commands such as `mark` and `delete`.

Examples:

```text
find BOOK
find club book
find b
```

Search considers descriptions only. Task types, completion status, and
deadline or event dates are not searched. Search does not modify tasks or the
save file.
