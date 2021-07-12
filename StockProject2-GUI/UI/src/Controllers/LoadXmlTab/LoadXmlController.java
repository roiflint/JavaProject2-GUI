package Controllers.LoadXmlTab;

import Controllers.MainWindow.LayoutController;
import com.ErrorDto;
import com.LoadFileTask;
import com.Main;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static com.Main.eng;

public class LoadXmlController {
    private LayoutController mainController;

    public void setMainController(LayoutController controller) {
        this.mainController = controller;
    }

    @FXML
    private BorderPane BorderPaneID;

    @FXML
    private Button LoadXmlButton;

    @FXML
    private Button ChooseFileButton;

    @FXML
    private ProgressBar progressID;

    @FXML
    private Label LoadXmlPath;

    @FXML
    private Label XmlMessageLabel;

    private SimpleStringProperty Message;
    private SimpleStringProperty File;

    public LoadXmlController() {
        Message = new SimpleStringProperty();
        File = new SimpleStringProperty();
    }

    public SimpleStringProperty getFILE() {
        return this.File;
    }

    @FXML
    private void initialize() {
        LoadXmlPath.textProperty().bind(File);

    }


    public void BindTaskToUIComponents(Task<Boolean> task) {
        XmlMessageLabel.textProperty().bind(task.messageProperty());
        progressID.progressProperty().bind(task.progressProperty());
    }


    @FXML
    public void openFileButtonAction() {        //open file chooser for xml file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Xml file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Xml files", "*.xml"));
        Stage s = mainController.getPrimaryStage();
        java.io.File selectedFile = fileChooser.showOpenDialog(s);
        if (selectedFile == null) {
            return;
        }

        String absolutePath = selectedFile.getAbsolutePath();
        File.set(absolutePath);
    }

    @FXML
    public void LoadXmlToSystem() {     //load with a task the xml file into the system
        String s = LoadXmlPath.getText();
        if (s != null && s.endsWith(".xml")) {
            LoadFileTask tsk = new LoadFileTask(s);
            BindTaskToUIComponents(tsk);
            tsk.valueProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue)
                {
                Main.eng.generateObservables();
                mainController.setIsXmlLoaded(true);
                mainController.toggleTabs(true);
                mainController.BindChoiceBoxes();}});
            new Thread(tsk).start();
        } else {
            XmlMessageLabel.setText("File must end with .xml");
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();

        }

    }
}


