package kis.covid19;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.nio.file.Path;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javax.swing.JFrame;

/**
 *
 * @author naoki
 */
public class MapCapture {
    
    static void init() {
        
        var frame = new JFrame("Browser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        var size = Toolkit.getDefaultToolkit().getScreenSize();
        int w = 600;
        int h = 500;
        frame.setBounds((size.width - w) / 2, (size.height - h) / 2, w, h);
        
        var jfxp = new JFXPanel();
        frame.add(jfxp, BorderLayout.CENTER);
        Platform.runLater(() -> {
            var view = new WebView();
            var engine = view.getEngine();
            var root = new Group();
            root.getChildren().add(view);
            var scene = new Scene(root);
            
            jfxp.setScene(scene);
            
            engine.load(Path.of("docs/index.html").toAbsolutePath().toUri().toString());
        });
        
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(MapCapture::init);
    }
}
