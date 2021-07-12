package Controllers.MainWindow;

import Controllers.AdminTab.AdminController;
import Controllers.LoadXmlTab.LoadXmlController;
import Controllers.TransactionTab.TransactionController;
import Controllers.UsersTab.UsersController;
import com.EngineInter;
import com.ErrorDto;
import com.Main;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class LayoutController {

    @FXML private BorderPane loadXmlComponent;
    @FXML private LoadXmlController loadXmlComponentController;
    @FXML private BorderPane transactionComponent;
    @FXML private TransactionController transactionComponentController;
    @FXML private GridPane usersComponent;
    @FXML private UsersController usersComponentController;
    @FXML private GridPane adminComponent;
    @FXML private AdminController adminComponentController;

    private final SimpleBooleanProperty isXmlLoaded = new SimpleBooleanProperty(false);
    public void setIsXmlLoaded(boolean loaded){this.isXmlLoaded.set(loaded);}
    private boolean initialized = false;

    private SimpleBooleanProperty XmlLoaded = new SimpleBooleanProperty(false);
    private Stage primaryStage;

    @FXML
    public void initialize(){
            loadXmlComponentController.setMainController(this);
            transactionComponentController.setMainController(this);
            usersComponentController.setMainController(this);
            adminComponentController.setMainController(this);
            this.initialized = true;

    }
    public void setPrimaryStage(Stage primaryStage) { this.primaryStage = primaryStage; }

    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

    @FXML
    private HBox hbox;

    @FXML
    private TabPane TabPaneID;

    @FXML
    private Tab LoadXmlTabID;

    @FXML
    private Tab TransactionTabID;

    @FXML
    private Tab UsersTabID;

    @FXML
    private Tab AdminTabID;

    @FXML
    private RadioButton LightModeRadio;

    @FXML
    private ToggleGroup THEME;

    @FXML
    private RadioButton GreyModeRadio;

    @FXML
    private RadioButton DarkModeRadio;

    public LayoutController(){
        XmlLoaded = new SimpleBooleanProperty(false);
    }


    public void toggleTabs(boolean isActive){       //toggle tabs to active
        TransactionTabID.setDisable(!isActive);
        UsersTabID.setDisable(!isActive);
        AdminTabID.setDisable(!isActive);
    }

    public void BindChoiceBoxes(){      //bind all choice boxes with their observable lists
        transactionComponentController.bindUsersSelector(Main.eng.getObservableUsers());
        usersComponentController.bindUserSelector(Main.eng.getObservableUsers());
        adminComponentController.bindStockSelector(Main.eng.getObservableStocks());
    }

    @FXML
    public void clearUsersTab(){usersComponentController.backToDefault();}
    @FXML
    public void clearAdminTab(){adminComponentController.backToDefault();}
    @FXML
    public void clearTransactionsTab(){transactionComponentController.backToDefault();}

    @FXML
    public void updateTabsContent(){        //update tables of each tab
        if(initialized) {
            clearTransactionsTab();
            usersComponentController.setTableData();
            adminComponentController.setTableData();
        }
    }

    @FXML
    public void LightMode(){        //load light mode
        if (primaryStage.getScene().getStylesheets().contains("/Controllers/MainWindow/Main_Grey.css")){
            primaryStage.getScene().getStylesheets().remove("/Controllers/MainWindow/Main_Grey.css");
        }
        else {
            primaryStage.getScene().getStylesheets().remove("/Controllers/MainWindow/Main_Dark.css");
        }
        primaryStage.getScene().getStylesheets().add("/Controllers/MainWindow/Main_Light.css");
    }

    @FXML
    public void GreyMode(){     //load grey mode
        if (primaryStage.getScene().getStylesheets().contains("/Controllers/MainWindow/Main_Dark.css")){
            primaryStage.getScene().getStylesheets().remove("/Controllers/MainWindow/Main_Dark.css");
        }
        else {
            primaryStage.getScene().getStylesheets().remove("/Controllers/MainWindow/Main_Light.css");
        }
        primaryStage.getScene().getStylesheets().add("/Controllers/MainWindow/Main_Grey.css");
    }

    @FXML
    public void DarkMode(){     //load dark mode
        if (primaryStage.getScene().getStylesheets().contains("/Controllers/MainWindow/Main_Grey.css")){
            primaryStage.getScene().getStylesheets().remove("/Controllers/MainWindow/Main_Grey.css");
        }
        else {
            primaryStage.getScene().getStylesheets().remove("/Controllers/MainWindow/Main_Light.css");
        }
        primaryStage.getScene().getStylesheets().add("/Controllers/MainWindow/Main_Dark.css");
    }
}
