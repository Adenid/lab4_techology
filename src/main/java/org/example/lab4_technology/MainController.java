package org.example.lab4_technology;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.SneakyThrows;
import org.example.lab4_technology.data.Subjects;
import org.example.lab4_technology.data.Teachers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    @FXML private TableView<Teachers> teachersTable;
    @FXML private TableColumn<Teachers, Integer> teachersIdCol;
    @FXML private TableColumn<Teachers, String> fullNameCol;
    private Connection connection;
    @FXML private TableView<Subjects> subjectsTable;
    @FXML private TableColumn<Subjects,Integer> subjectsIdCol;
    @FXML private TableColumn<Subjects, String> nameCol;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }


    @SneakyThrows
    public void initialize() {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/university", "admin", "Aa1234567890#");
        subjectsIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        fillTable();

    }

    @SneakyThrows
    public void deleteSubject() {
        Subjects selectedItem = subjectsTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            PreparedStatement ps = connection.prepareStatement("delete from subjects where subjectId = ?");
            ps.setInt(1, selectedItem.getId());
            ps.executeUpdate();
            fillTable();
        }
    }

    @SneakyThrows
    private void fillTable() {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM subjects");
            List<Subjects> subjectsList = new ArrayList<>();
            while (resultSet.next()) {
                int subjectId = resultSet.getInt("SubjectID");
                String subjectName = resultSet.getString("Name");
                subjectsList.add(new Subjects(subjectId, subjectName));
            }
            subjectsTable.setItems(FXCollections.observableList(subjectsList));
        }
    }
}