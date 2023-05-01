package p1;

import javafx.application.Application;
import javafx.stage.Stage;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class TextGenerator extends Application {
        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                JFrame frame = new TextGeneratorFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            });
        }


    @Override
    public void start(Stage stage) throws Exception {

    }
}