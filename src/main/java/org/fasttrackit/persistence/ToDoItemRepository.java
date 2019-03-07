package org.fasttrackit.persistence;

import org.fasttrackit.domain.ToDoItem;
import org.fasttrackit.transfer.SaveToDoItemRequest;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ToDoItemRepository {

    public void createToDoItem(SaveToDoItemRequest request) throws SQLException, IOException, ClassNotFoundException {
        try (Connection connection = DatabaseConfiguration.getConnection()) {

            String insertSql = "INSERT INTO to_do_items (description, deadline, done) VALUES (?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
            preparedStatement.setString(1, request.getDescription());
            preparedStatement.setDate(2, request.getDeadline());
            preparedStatement.setBoolean(3, request.isDone());

            preparedStatement.executeUpdate();
        }
    }
    public List<ToDoItem> getToDoItems() throws SQLException, IOException, ClassNotFoundException {
        try (Connection connection = DatabaseConfiguration.getConnection()) {

            String query = "SELECT id, description, deadline, done FROM to_do_items ORDER BY deadline DESC"; //tilda (`) reserved keyword for name

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            List<ToDoItem> response = new ArrayList<>();

            while (resultSet.next()) {
                ToDoItem toDoItem = new ToDoItem();
                toDoItem.setId(resultSet.getLong("id"));
                toDoItem.setDescription(resultSet.getString("description"));
                toDoItem.setDeadline(resultSet.getDate("deadline"));
                toDoItem.setDone(resultSet.getBoolean("done"));

                response.add(toDoItem);
            }
            return response;
        }
    }
}