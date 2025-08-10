WordTracker

Overview

WordTracker reads one or more text files, stores all unique words in a binary search tree (BST), and tracks:
	•	Which file(s) each word appears in
	•	The line numbers where the word occurs
	•	The total number of occurrences

Supported Modes

Mode	Description
-pf	Print each word with the file(s) it appears in
-pl	Print each word with the file(s) and line numbers
-po	Print each word with total occurrence count and line numbers
-f<filename>	Optional flag to export the output to a file instead of printing

The BST is persisted in repository.ser so data from multiple runs can be combined.

⸻

Compilation

To compile all .java files into the bin/ directory:

javac -d bin src/appDomain/*.java


⸻

Usage

Syntax:

java -cp bin appDomain.WordTracker <inputFile> <mode> [-foutputFile]

Parameters:
	•	<inputFile> — Path to the text file to process
	•	<mode> — One of:
	•	-pf → Word + file names
	•	-pl → Word + file names + line numbers
	•	-po → Word + file names + line numbers + occurrence count
	•	-foutputFile — Optional, export results to the specified file instead of printing

⸻

Examples

1. First run — Create repository

java -cp bin appDomain.WordTracker res/test1.txt -pf

Output:

Displaying -pf format
Key : ===hello=== found in file: res/test1.txt
Key : ===is=== found in file: res/test1.txt
Key : ===it's=== found in file: res/test1.txt
...
Not exporting file.


⸻

2. Merge with existing repository

java -cp bin appDomain.WordTracker res/test2.txt -pl

Output:

Displaying -pl format
Key : ===again=== found in file: res/test2.txt on lines: 1,2
Key : ===hello=== found in file: res/test2.txt on lines: 1,2, found in file: res/test1.txt on lines: 1,2
...
Not exporting file.


⸻

3. Export results to a file

java -cp bin appDomain.WordTracker res/test3.txt -po -fresults.txt

Output is saved to:

results.txt


⸻

Repository File
	•	Stored as repository.ser in the project root
	•	Holds merged results between runs

Reset repository:

rm repository.ser


⸻

Expected Output Formats

-pf

Key : ===word=== found in file: filename

-pl

Key : ===word=== found in file: filename on lines: 1,2,3

-po

Key : ===word=== found in file: filename on lines: 1,2,3 [occurrences: N]

