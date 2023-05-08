package p1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class TextGenerator extends Application {

    private Stage stage;
    private Scene welcomeScene, instructionScene, appScene;
    private MainHashTable mainHashTable;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        Pane loginGui = welcome();
        welcomeScene = new Scene(loginGui, 500, 450);
        welcomeScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setScene(welcomeScene);
        primaryStage.show();
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
                        mainHashTable.insert(prevWord, word);
                    }
                    prevWord = word;
                }
            });
        }
    }

    private void saveGeneratedText(String text) {
        File outputDir = new File("output");
        outputDir.mkdirs();
        try (FileWriter writer = new FileWriter("src/main/java/p1/output/output.txt")) {
            writer.write(text);
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }
    }

    private Pane welcome() {
        Pane welcome = new StackPane(); // Use StackPane instead of Pane
        // Welcome Screen
        VBox welcomeScreen = new VBox(20);
        welcomeScreen.setAlignment(Pos.CENTER);
        welcomeScreen.setPadding(new Insets(10));
        Label welcomeLabel = new Label("Welcome to Synonym Swing");
        welcomeLabel.setStyle("-fx-font-size: 20;");
        Button returningUserButton = new Button("Returning user");
        Button firstTimeUserButton = new Button("First-time user");
        welcomeScreen.getChildren().addAll(welcomeLabel, returningUserButton, firstTimeUserButton);

        // Button actions
        returningUserButton.setOnAction(e -> {
            appScene = new Scene(buildApp(), 500, 450);
            appScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            stage.setScene(appScene);
        });

        firstTimeUserButton.setOnAction(e -> {
            instructionScene = new Scene(instructions(), 500, 450);
            instructionScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            stage.setScene(instructionScene);
        });

        // Add VBox to the StackPane and center it
        welcome.getChildren().add(welcomeScreen);
        StackPane.setAlignment(welcomeScreen, Pos.CENTER);

        return welcome;
    }


    private Pane instructions() {
        BorderPane instructions = new BorderPane();
        // Instructions
        VBox instructionBox = new VBox(10);
        instructionBox.setAlignment(Pos.CENTER);
        instructionBox.setPadding(new Insets(10));
        Label instructionLabel = new Label("Instructions:");
        Label step1 = new Label("1. Click 'File' in the menu bar to select a text file.");
        Label step2 = new Label("2. Enter a starting word and a word count.");
        Label step3 = new Label("3. Click 'Generate' to generate new text.");
        Button proceedButton = new Button("Proceed");
        instructionBox.getChildren().addAll(instructionLabel, step1, step2, step3, proceedButton);


        proceedButton.setOnAction(e -> {
            instructions.setVisible(false);
            appScene = new Scene(buildApp(), 500, 450);
            appScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            stage.setScene(appScene);
        });

        instructions.setCenter(instructionBox);
        return instructions;
    }

    private Pane buildApp() {
        TextArea textArea;
        TextField startingWordField, wordCountField;

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem useREADME = new MenuItem("Use README");
        MenuItem useWarAndPeace = new MenuItem("Use War and Peace");
        MenuItem openItem = new MenuItem("Open");
        fileMenu.getItems().addAll(openItem, useWarAndPeace, useREADME);
        menuBar.getMenus().add(fileMenu);

        HBox inputPanel = new HBox(10);
        inputPanel.setPadding(new Insets(10));
        inputPanel.getChildren().add(new Label("Starting word:"));
        startingWordField = new TextField();
        startingWordField.setPrefWidth(100);
        startingWordField.setDisable(true);
        inputPanel.getChildren().add(startingWordField);
        inputPanel.getChildren().add(new Label("Word count:"));
        wordCountField = new TextField();
        wordCountField.setPrefWidth(69);
        wordCountField.setDisable(true);
        inputPanel.getChildren().add(wordCountField);
        Button generateButton = new Button("Generate");
        generateButton.setDisable(true);
        inputPanel.getChildren().add(generateButton);

        textArea = new TextArea();
        textArea.setPrefWidth(500);
        textArea.setPrefHeight(369);
        textArea.setDisable(false); //scroll or not
        textArea.setWrapText(true);

        VBox root = new VBox(menuBar, inputPanel, textArea);

        BorderPane app = new BorderPane();
        app.setTop(menuBar);
        app.setCenter(inputPanel);
        app.setBottom(textArea);
        Path readMEPath = Paths.get("src/main/java/p1/data/readME2.txt");

        try {
            String readmeText = new String(Files.readAllBytes(readMEPath));
            textArea.setText(readmeText);
        } catch (IOException e) {
            System.err.println("Error reading README file: " + e.getMessage());
        }

        openItem.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                mainHashTable = new MainHashTable();
                try {
                    processFile(selectedFile.toPath());

                    String newTextItem = new String(Files.readAllBytes(selectedFile.toPath()));
                    textArea.setText(newTextItem);
                    generateButton.setDisable(false);
                    startingWordField.setDisable(false);
                    wordCountField.setDisable(false);
                    fileMenu.setDisable(false);
                } catch (IOException ex) {
                    System.err.println("Error reading file: " + ex.getMessage());
                }
            }
        });

        useWarAndPeace.setOnAction(e -> {
            fileMenu.setDisable(true);
            generateButton.setDisable(true);
            mainHashTable = new MainHashTable();
            try {
                Path filePath = Paths.get("src/main/java/p1/data/warAndPeace.txt");
                processFile(filePath);
                String newTextItem = new String(Files.readAllBytes(filePath));
                textArea.setText(newTextItem);
                generateButton.setDisable(false);
                startingWordField.setDisable(false);
                wordCountField.setDisable(false);
                fileMenu.setDisable(false);
            } catch (IOException ex) {
                System.err.println("Error reading file: " + ex.getMessage());
            }

        });

        useREADME.setOnAction(e -> {
            fileMenu.setDisable(true);
            generateButton.setDisable(true);
             mainHashTable = new MainHashTable();
            try {
                Path path = Paths.get("src/main/java/p1/data/readME2.txt");
                processFile(path);
                String newTextItem = new String(Files.readAllBytes(path));
                textArea.setText(newTextItem);
                generateButton.setDisable(false);
                startingWordField.setDisable(false);
                wordCountField.setDisable(false);
                fileMenu.setDisable(false);
            } catch (IOException ex) {
                System.err.println("Error reading file: " + ex.getMessage());
            }
        });

        generateButton.setOnAction(e -> {
            Boolean good2go = false;
            if (startingWordField.getText().isEmpty()) {
                textArea.setText("Please enter a starting word.");
            } else if (wordCountField.getText().isEmpty()) {
                textArea.setText("Please enter a word count.");
            } else if (startingWordField.getText().isEmpty() && wordCountField.getText().isEmpty()) {
                textArea.setText("Please enter a starting word and a word count.");
            } else {
                good2go = true;
            }
            if (mainHashTable != null && good2go) {
                String startingWord = startingWordField.getText();
                int wordCount = Integer.parseInt(wordCountField.getText());
                String generatedText = mainHashTable.generateText(startingWord, wordCount);
                textArea.setText(generatedText);
                saveGeneratedText(generatedText);
            }
        });

        menuBar.setPrefWidth(Double.MAX_VALUE);
        inputPanel.setPrefWidth(Double.MAX_VALUE);
        root.setPrefWidth(Double.MAX_VALUE);

        return app;
    }
}