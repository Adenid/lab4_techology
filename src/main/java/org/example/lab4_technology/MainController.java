package org.example.lab4_technology;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.SneakyThrows;
import org.example.lab4_technology.data.Subjects;
import org.example.lab4_technology.data.Teachers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    @FXML private TextField updateSubject;
    @FXML private TextField updateTeacher;
    @FXML private TextField addSubject;
    @FXML private TextField addTeacher;
    @FXML private TableView<Teachers> teachersTable;
    @FXML private TableColumn<Teachers, Integer> teachersIdCol;
    @FXML private TableColumn<Teachers, String> fullNameCol;
    private Connection connection;
    @FXML private TableView<Subjects> subjectsTable;
    @FXML private TableColumn<Subjects,Integer> subjectsIdCol;
    @FXML private TableColumn<Subjects, String> nameCol;
    @FXML
    private Label welcomeText;

    @SneakyThrows
    public void initialize() {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/university", "admin", "Aa1234567890#");
        subjectsIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        teachersIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        fullNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        fillTable();
        fillTeacherTable();
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

    @SneakyThrows
    private void fillTeacherTable() {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM teachers");
            List<Teachers> teachersList = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("TeacherID");
                String fullName = resultSet.getString("FullName");
                teachersList.add(new Teachers(id, fullName));
            }
            teachersTable.setItems(FXCollections.observableList(teachersList));
        }
    }

    @SneakyThrows
    public void addTeacher(ActionEvent actionEvent) {
        String text = addTeacher.getText();
        try (PreparedStatement ps = connection.prepareStatement("insert into teachers (FullName) values (?)")) {
            ps.setString(1, text);
            ps.executeUpdate();
            fillTeacherTable();
            addTeacher.clear();
        }
    }

    @SneakyThrows
    public void addSubject(ActionEvent actionEvent) {
        String text = addSubject.getText();
        try (PreparedStatement ps = connection.prepareStatement("insert into subjects (Name) values (?)")) {
            ps.setString(1, text);
            ps.executeUpdate();
            fillTable();
            addSubject.clear();
        }
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
    public void deleteTeacher() {
        Teachers selectedItem = teachersTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            PreparedStatement ps = connection.prepareStatement("delete from teachers where teacherId = ?");
            ps.setInt(1, selectedItem.getId());
            ps.executeUpdate();
            fillTeacherTable();
        }
    }

    @SneakyThrows
    public void updateSubject(ActionEvent actionEvent) {
        Subjects selectedItem = subjectsTable.getSelectionModel().getSelectedItem();
        String newName = updateSubject.getText();

        if (selectedItem != null && !newName.isEmpty()) {
            try (PreparedStatement ps = connection.prepareStatement("UPDATE subjects SET Name = ? WHERE SubjectID = ?")) {
                ps.setString(1, newName);
                ps.setInt(2, selectedItem.getId());
                ps.executeUpdate();
                fillTable();
                updateSubject.clear();
            }
        }
    }

    @SneakyThrows
    public void updateTeacher(ActionEvent actionEvent) {
        Teachers selectedItem = teachersTable.getSelectionModel().getSelectedItem();
        String newName = updateTeacher.getText();

        if (selectedItem != null && !newName.isEmpty()) {
            try (PreparedStatement ps = connection.prepareStatement("UPDATE teachers SET FullName = ? WHERE TeacherID = ?")) {
                ps.setString(1, newName);
                ps.setInt(2, selectedItem.getId());
                ps.executeUpdate();
                fillTeacherTable();
                updateTeacher.clear();
            }
        }
    }

}