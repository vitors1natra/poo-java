package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Products;
import model.services.ProductsService;
import model.services.SellerService;

public class ProductsListController implements Initializable, DataChangeListener	 {
	
	private ProductsService service;
	
	@FXML
	private TableView<Products> tableViewProducts;
	
	@FXML
	private TableColumn<Products, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Products, String> tableColumnName;
	
	@FXML
	private TableColumn<Products, String> tableColumnCategory;
	
	@FXML
	private TableColumn<Products, Date> tableColumnReleaseDate;
	
	@FXML
	private TableColumn<Products, Double> tableColumnPrice;
	
	@FXML
	private TableColumn<Products, Products> tableColumnEDIT;
	
	@FXML
	private TableColumn<Products, Products> tableColumnREMOVE;
	

	@FXML
	private Button btNew;
	
	private ObservableList<Products> obsList;
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Products obj = new Products();
		createDialogForm(obj, "/gui/ProductsForm.fxml", parentStage);
	}
	
	public void setProductsService(ProductsService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		intializeNodes();
	}

	private void intializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
		tableColumnReleaseDate.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));
		Utils.formatTableColumnDate(tableColumnReleaseDate, "dd/MM/yyyy");
		tableColumnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
		Utils.formatTableColumnDouble(tableColumnPrice, 2);

		
		Stage stage = (Stage) Main.getMainSecene().getWindow();
		tableViewProducts.prefHeightProperty().bind(stage.heightProperty());
		
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Servico nulo");
		}
		List<Products> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewProducts.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}
	
	private void createDialogForm(Products obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			ProductsFormController controller = loader.getController();
			controller.setProducts(obj);
			controller.setServices(new ProductsService(), new SellerService());
			controller.loadAssociatedObjects();
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Entre com dados do colaborador");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
			
			
		} 
		catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Erro ao carregar a view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
		
	}
	
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Products, Products>() {
			private final Button button = new Button("editar");
			
			@Override
			protected void updateItem(Products obj, boolean empty) {
				super.updateItem(obj, empty);
				
				if (obj == null) {
					setGraphic(null);
					return;
				}
				
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(
						obj, "/gui/ProductsForm.fxml", Utils.currentStage(event)));
			}
	
	});
  }
	
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Products, Products>() {
			private final Button button = new Button("Remove");
			
			@Override
			protected void updateItem(Products obj, boolean empty) {
				super.updateItem(obj, empty);
				
				if (obj == null) {
					setGraphic(null);
					return;
				}
				
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
	
	});
  
	}

	private void removeEntity(Products obj) {
		Optional<ButtonType> result = Alerts.showConformation("Confirmar", "Tem certeaza que quer deletar?");
		
		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Servico nulo");
			}
			try {
				service.remove(obj);
				updateTableView();
			} 
			catch (DbIntegrityException e) {
				Alerts.showAlert("Erro ao remover o objeto", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}

