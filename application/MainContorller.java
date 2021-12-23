package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class MainContorller implements Initializable {

	@FXML
	private Button btnDelete;

	@FXML
	private Button btnInsert;

	@FXML
	private Button btnUpdate;

	@FXML
	private TextField tfID;

	@FXML
	private TextField tfAuthor;

	@FXML
	private TextField tfPages;

	@FXML
	private TextField tfTitle;

	@FXML
	private TextField tfYear;

	@FXML
	private TableView<Books> tvBooks;

	@FXML
	private TableColumn<Books, String> colAuthor;

	@FXML
	private TableColumn<Books, Integer> colID;

	@FXML
	private TableColumn<Books, Integer> colPages;

	@FXML
	private TableColumn<Books, String> colTitle;

	@FXML
	private TableColumn<Books, Integer> colYear;

	@FXML
	void handleButtonAction(ActionEvent event) {
		if (event.getSource() == btnInsert) {
			insertRecord();

		} else if (event.getSource() == btnUpdate) {
			updateRecord();
		} else if (event.getSource() == btnDelete) {
			deleteButton();
		}

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		showBooks();

	}

	public Connection getConnection() {
		Connection conn;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","c##ino","1234");
			return conn;
		} catch (Exception e) {
			System.out.println("Error :" + e.getMessage());
			return null;
		}

	}

	public ObservableList<Books> getBookList() {
		ObservableList<Books> bookList = FXCollections.observableArrayList();
		Connection conn = getConnection();
		String query = "select * from books order by id";
		Statement st;
		ResultSet rs;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(query);
			Books books;
			while (rs.next()) {
				books = new Books(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getInt("year"), rs.getInt("pages"));
				bookList.add(books);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bookList;
	}

	private void showBooks() {
		ObservableList<Books> list = getBookList();
		colID.setCellValueFactory(new PropertyValueFactory<Books, Integer>("id"));
		colTitle.setCellValueFactory(new PropertyValueFactory<Books, String>("title"));
		colAuthor.setCellValueFactory(new PropertyValueFactory<Books, String>("author"));
		colYear.setCellValueFactory(new PropertyValueFactory<Books, Integer>("year"));
		colPages.setCellValueFactory(new PropertyValueFactory<Books, Integer>("pages"));
		
tvBooks.setItems(list);
	}


	private void deleteButton() {
		String query = "delete from books where id ="+ tfID.getText() + "";
		executeQuery(query);
		showBooks();
	}

	private void updateRecord() {
		String query = "update books set title = '" + tfTitle.getText() + "', author ='" + tfAuthor.getText() + "', year = " + tfYear.getText() + ", pages=" + tfPages.getText() + "Where id = " + tfID.getText() + "";
		executeQuery(query);
		showBooks();
	}

	private void insertRecord() {
		String query = "insert into books values('" + tfID.getText()+ "','"+ tfTitle.getText() + "','" + tfAuthor.getText() + "','"+ tfYear.getText()+"','"+tfPages.getText()+"')";
		executeQuery(query);
		showBooks();
	}

	private void executeQuery(String query) {
		Connection conn = getConnection();
		Statement st;
		try {
			st = conn.createStatement();
			st.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void handleMouseAction(MouseEvent event) {
		Books books = tvBooks.getSelectionModel().getSelectedItem();
		tfID.setText(""+ books.getId());
		tfTitle.setText(books.getTitle());
		tfAuthor.setText(books.getAuthor());
		tfYear.setText(""+ books.getYear());
		tfPages.setText(""+ books.getPages());
	}
	
	

}
