package appDomain;

import implementations.BSTree;
import implementations.BSTreeNode;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import utilities.Iterator;

public class WordTracker {

    private static final String REPO = "repository.ser";

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: java -jar WordTracker.jar <input.txt> -pf|-pl|-po [-foutput.txt]");
            System.exit(1);
        }

        final String inputPath = args[0];
        final String mode = args[1];
        final String outFile = (args.length >= 3 && args[2].startsWith("-f")) ? args[2].substring(2) : null;

        BSTree<WordEntry> tree = loadRepo();

        Map<String, WordEntry> parsed = parseFile(inputPath);
        mergeIntoTree(tree, parsed);
        saveRepo(tree);

        String report = buildReport(tree, mode);

        if (outFile != null) {
            try (BufferedWriter w = Files.newBufferedWriter(Paths.get(outFile), StandardCharsets.UTF_8)) {
                w.write(report);
            }
        } else {
            System.out.print(report);
            System.out.println("Not exporting file.");
        }
    }

    // ---------- parsing ----------
    private static Map<String, WordEntry> parseFile(String path) throws IOException {
        Map<String, WordEntry> map = new HashMap<>();
        List<String> lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);

        for (int i = 0; i < lines.size(); i++) {
            int lineNo = i + 1;
            String norm = lines.get(i).toLowerCase(Locale.ROOT)
                    .replaceAll("[^a-z0-9']+", " ")
                    .trim();
            if (norm.isEmpty())
                continue;

            String[] tokens = norm.split("\\s+");
            for (String t : tokens) {
                if (t.isEmpty())
                    continue;
                WordEntry e = map.get(t);
                if (e == null) {
                    e = new WordEntry(t);
                    map.put(t, e);
                }
                e.addOccurrence(path, lineNo);
            }
        }
        return map;
    }

    // ---------- merge ----------
    private static void mergeIntoTree(BSTree<WordEntry> tree, Map<String, WordEntry> parsed) {
        for (WordEntry e : parsed.values()) {
            BSTreeNode<WordEntry> node = tree.search(e);
            if (node == null) {
                tree.add(e);
            } else {
                for (Map.Entry<String, List<Integer>> kv : e.getHits().entrySet()) {
                    for (int ln : kv.getValue())
                        node.getElement().addOccurrence(kv.getKey(), ln);
                }
            }
        }
    }

    // ---------- repo I/O ----------
    @SuppressWarnings("unchecked")
    private static BSTree<WordEntry> loadRepo() {
        File f = new File(REPO);
        if (!f.exists())
            return new BSTree<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            return (BSTree<WordEntry>) in.readObject();
        } catch (Exception e) {
            return new BSTree<>();
        }
    }

    private static void saveRepo(BSTree<WordEntry> tree) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(REPO))) {
            out.writeObject(tree);
        } catch (IOException ignored) {
        }
    }

    // ---------- reporting ----------
    private static String buildReport(BSTree<WordEntry> tree, String mode) {
        StringBuilder sb = new StringBuilder();
        sb.append("Displaying ").append(mode).append(" format").append(System.lineSeparator());

        Iterator<WordEntry> it = tree.inorderIterator(); // alphabetical
        while (it.hasNext()) {
            WordEntry w = it.next();
            sb.append("Key : ===").append(w.getWord()).append("=== ");

            if ("-pf".equals(mode)) {
                // Key : ===word=== found in file: test1.txt
                boolean first = true;
                for (String file : w.getHits().keySet()) {
                    if (!first)
                        sb.append(", ");
                    sb.append("found in file: ").append(file);
                    first = false;
                }
            } else if ("-pl".equals(mode)) {
                // Key : ===word=== found in file: test1.txt on lines: 1,2, found in file:
                // test2.txt on lines: 1
                boolean first = true;
                for (Map.Entry<String, List<Integer>> e : w.getHits().entrySet()) {
                    if (!first)
                        sb.append(", ");
                    sb.append("found in file: ").append(e.getKey())
                            .append(" on lines: ").append(joinInts(e.getValue()));
                    first = false;
                }
            } else if ("-po".equals(mode)) {
                // Key : ===word=== number of entries: N found in file: ... on lines: ...
                sb.append("number of entries: ").append(w.totalCount()).append(" ");
                boolean first = true;
                for (Map.Entry<String, List<Integer>> e : w.getHits().entrySet()) {
                    if (!first)
                        sb.append(", ");
                    sb.append("found in file: ").append(e.getKey())
                            .append(" on lines: ").append(joinInts(e.getValue()));
                    first = false;
                }
            } else {
                sb.append("[unknown mode]");
            }

            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    private static String joinInts(List<Integer> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0)
                sb.append(",");
            sb.append(list.get(i));
        }
        return sb.toString();
    }
}