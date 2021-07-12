package Controllers.UsersTab;

import Controllers.MainWindow.LayoutController;
import com.Main;
import com.ObservableUserStocks;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

public class UsersController {

    private LayoutController mainController;
    public void setMainController(LayoutController controller){
        this.mainController = controller;
    }

    @FXML
    private GridPane GridPaneID;

    @FXML
    private ChoiceBox<String> UsersUserSelector;

    @FXML
    private TableView<ObservableUserStocks> UsersTable;

    @FXML
    private TableColumn<ObservableUserStocks, String> UserSymbolCollum;

    @FXML
    private TableColumn<ObservableUserStocks, String> UserCompanyCollum;

    @FXML
    private TableColumn<ObservableUserStocks, String> UserPriceCollum;

    @FXML
    private TableColumn<ObservableUserStocks, String> UserQuantityCollum;

    @FXML
    private Label UserCycleLabel;

    public void bindUserSelector(ObservableList<String> lst){
        UsersUserSelector.setItems(lst);
        UsersUserSelector.setValue(lst.get(0));
    }

    @FXML
    public void initialize(){
        UserSymbolCollum.setCellValueFactory(new PropertyValueFactory<>("Symbol"));
        UserCompanyCollum.setCellValueFactory(new PropertyValueFactory<>("CompanyName"));
        UserPriceCollum.setCellValueFactory(new PropertyValueFactory<>("Price"));
        UserQuantityCollum.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
    }

    public void backToDefault(){
        UsersTable.setItems(clearTable);
        UsersUserSelector.setValue(Main.eng.getObservableUsers().get(0));
        UserCycleLabel.setText("0");
    }

    public void setTableData(){
        if (UsersUserSelector.getItems().size() > 1 &&!UsersUserSelector.getValue().equals("") && !UsersUserSelector.getValue().equals("--SELECT--")) {
            ObservableList<ObservableUserStocks> lst = Main.eng.getObservableUserStocks(UsersUserSelector.getValue());
            UserCycleLabel.setText(Main.eng.CalculateTotalCycle(UsersUserSelector.getValue()));
            UsersTable.setItems(lst);
        }
        else {
            UsersTable.setItems(clearTable);
            UserCycleLabel.setText("0");
        }

    }
    private final ObservableList<ObservableUserStocks> clearTable = FXCollections.observableArrayList();
}
