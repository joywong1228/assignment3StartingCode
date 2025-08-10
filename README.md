<h1>WordTracker</h1>

<h2>Overview</h2>
<p>
<b>WordTracker</b> reads one or more text files, stores all unique words in a <b>binary search tree</b> (BST), and tracks:
<ul>
<li>Which file(s) each word appears in</li>
<li>The line numbers where the word occurs</li>
<li>The total number of occurrences</li>
</ul>
</p>

<h3>Supported Modes</h3>
<table>
<thead>
<tr><th>Mode</th><th>Description</th></tr>
</thead>
<tbody>
<tr><td><code>-pf</code></td><td>Print each word with the file(s) it appears in</td></tr>
<tr><td><code>-pl</code></td><td>Print each word with the file(s) and line numbers</td></tr>
<tr><td><code>-po</code></td><td>Print each word with total occurrence count and line numbers</td></tr>
<tr><td><code>-f&lt;filename&gt;</code></td><td>Optional flag to export the output to a file instead of printing</td></tr>
</tbody>
</table>

<p>
The BST is persisted in <code>repository.ser</code> so data from multiple runs can be combined.
</p>

<hr>

<h2>Compilation</h2>
<p>To compile all <code>.java</code> files into the <code>bin/</code> directory:</p>
<pre>
javac -d bin src/appDomain/*.java
</pre>

<hr>

<h2>Usage</h2>
<p><b>Syntax:</b></p>
<pre>
java -cp bin appDomain.WordTracker &lt;inputFile&gt; &lt;mode&gt; [-foutputFile]
</pre>

<p><b>Parameters:</b></p>
<ul>
<li><code>&lt;inputFile&gt;</code> — Path to the text file to process</li>
<li><code>&lt;mode&gt;</code> — One of:
  <ul>
    <li><code>-pf</code> → Word + file names</li>
    <li><code>-pl</code> → Word + file names + line numbers</li>
    <li><code>-po</code> → Word + file names + line numbers + occurrence count</li>
  </ul>
</li>
<li><code>-foutputFile</code> — Optional, export results to the specified file instead of printing</li>
</ul>

<hr>

<h2>Examples</h2>

<h3>1. First run — Create repository</h3>
<pre>
java -cp bin appDomain.WordTracker res/test1.txt -pf
</pre>
<pre>
Displaying -pf format
Key : ===hello=== found in file: res/test1.txt
Key : ===is=== found in file: res/test1.txt
Key : ===it's=== found in file: res/test1.txt
...
Not exporting file.
</pre>

<h3>2. Merge with existing repository</h3>
<pre>
java -cp bin appDomain.WordTracker res/test2.txt -pl
</pre>
<pre>
Displaying -pl format
Key : ===again=== found in file: res/test2.txt on lines: 1,2
Key : ===hello=== found in file: res/test2.txt on lines: 1,2, found in file: res/test1.txt on lines: 1,2
...
Not exporting file.
</pre>

<h3>3. Export results to a file</h3>
<pre>
java -cp bin appDomain.WordTracker res/test3.txt -po -fresults.txt
</pre>
<p>Output is saved to: <code>results.txt</code></p>

<hr>

<h2>Repository File</h2>
<ul>
<li>Stored as <code>repository.ser</code> in the project root</li>
<li>Holds merged results between runs</li>
</ul>

<p><b>Reset repository:</b></p>
<pre>
rm repository.ser
</pre>

<hr>

<h2>Expected Output Formats</h2>

<h3>-pf</h3>
<pre>
Key : ===word=== found in file: filename
</pre>

<h3>-pl</h3>
<pre>
Key : ===word=== found in file: filename on lines: 1,2,3
</pre>

<h3>-po</h3>
<pre>
Key : ===word=== found in file: filename on lines: 1,2,3 [occurrences: N]
</pre>
