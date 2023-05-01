package p1;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class TextGeneratorFrame extends JFrame {
    JTextArea textArea;
    JTextField startingWordField, wordCountField;
    MainLinkedList mainLinkedList;

    public TextGeneratorFrame() {
        setTitle("Text Generator");
        setSize(700, 400);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem useWarAndPeace = new JMenuItem("Use War and Peace");
        JMenuItem openItem = new JMenuItem("Open");
        fileMenu.add(openItem);
        fileMenu.add(useWarAndPeace);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Starting word:"));
        startingWordField = new JTextField(10);
        startingWordField.setEnabled(false);
        inputPanel.add(startingWordField);
        inputPanel.add(new JLabel("Word count:"));
        wordCountField = new JTextField(5);
        wordCountField.setEnabled(false);
        inputPanel.add(wordCountField);
        JButton generateButton = new JButton("Generate");
        generateButton.setEnabled(false);
        inputPanel.add(generateButton);
        add(inputPanel, BorderLayout.NORTH);
        textArea = new JTextArea();
        textArea.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        Path readMEPath = Paths.get("src/main/java/p1/data/readME2.txt");

        try {
            String readmeText = new String(Files.readAllBytes(readMEPath));
            textArea.setText(readmeText);
        } catch (IOException e) {
            System.err.println("Error reading README file: " + e.getMessage());
        }


        openItem.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                mainLinkedList = new MainLinkedList();
                try {
                    processFile(chooser.getSelectedFile().toPath());
                    generateButton.setEnabled(true);
                    startingWordField.setEnabled(true);
                    wordCountField.setEnabled(true);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error reading file", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        useWarAndPeace.addActionListener(e -> {
            fileMenu.setEnabled(false);
            generateButton.setEnabled(false);
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    mainLinkedList = new MainLinkedList();
                    try {
                        System.out.println("trying");
                        processFile(Paths.get("src/main/java/p1/data/warAndPeace.txt"));
                        System.out.println("done");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(TextGeneratorFrame.this, "Error reading file", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    return null;
                }

                @Override
                protected void done() {
                    fileMenu.setEnabled(true);
                    generateButton.setEnabled(true);
                    startingWordField.setEnabled(true);
                    wordCountField.setEnabled(true);
                }
            };
            worker.execute();
        });

        generateButton.addActionListener(e -> {
            Boolean good2go = false;
            if(startingWordField.getText().equals("")){
                textArea.setText("Please enter starting word.");
            }
            if(wordCountField.getText().equals("")){
                textArea.setText("Please enter word count.");
            }
            if(startingWordField.getText().equals("") && wordCountField.getText().equals("")){
                textArea.setText("Please enter a starting word and a word count.");
            }
            if(!startingWordField.getText().equals("") && !wordCountField.getText().equals("")){
                good2go = true;
            }
            if(mainLinkedList != null && good2go) {
                String startingWord = startingWordField.getText();
                int wordCount = Integer.parseInt(wordCountField.getText());
                String generatedText = mainLinkedList.generateText(startingWord, wordCount);
                textArea.setText(generatedText);
                saveGeneratedText(generatedText);
            }
        });
    }

    private void processFile(Path filePath) throws IOException {
        Pattern wordPattern = Pattern.compile("\\b\\w+\\b");
        try (Stream<String> lines = Files.lines(filePath).parallel()) {
            lines.forEach(line -> {
                Matcher matcher = wordPattern.matcher(line);
                String prevWord = null;
                while (matcher.find()) {
                    String word = matcher.group();
                    if (prevWord != null) {
                        mainLinkedList.insert(prevWord, word);
                    }
                    prevWord = word;
                }
            });
        }
    }


    private void saveGeneratedText(String text) {
        File outputDir = new File("output");
        outputDir.mkdirs();
        try (FileWriter writer = new FileWriter("src/main/java/p1/Output/output.txt")) {
            writer.write(text);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
