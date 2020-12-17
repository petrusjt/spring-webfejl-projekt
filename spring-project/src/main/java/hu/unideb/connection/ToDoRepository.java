package hu.unideb.connection;

import hu.unideb.entities.Level;
import hu.unideb.entities.ToDo;
import hu.unideb.exceptions.MySqlConnectionFailedException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ToDoRepository {
    private Connection conn;

    private PreparedStatement getAllQuery;
    private PreparedStatement getByIdQuery;
    private PreparedStatement addToDoQuery;
    private PreparedStatement markToDoDoneQuery;

    {
        try {
            conn = MySQLConnection.GetConnection();
        } catch (MySqlConnectionFailedException e) {
            e.printStackTrace();
        }
        try {
            getAllQuery = conn.prepareStatement("select * from ToDo;");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            getByIdQuery = conn.prepareStatement("select * from ToDo where id = ?;");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            addToDoQuery = conn.prepareStatement("INSERT INTO `ToDo` (`Id`, `shortDescription`, `longDescription`, `deadline`, `level`) " +
                    "VALUES (NULL, ?, ?, ?, ?, NULL);");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            markToDoDoneQuery = conn.prepareStatement("update `ToDo` SET `done` = 1 where `ToDo`.`Id` = ?;");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<ToDo> getAllToDos()
    {
        List<ToDo> toDos = new ArrayList<>();
        try {
            ResultSet resultSet = getAllQuery.executeQuery();

            while(resultSet.next()) {
                ToDo toDo = ToDo.builder()
                        .id(resultSet.getLong("id"))
                        .shortDescription(resultSet.getString("shortDescription"))
                        .longDescription(resultSet.getString("longDescription"))
                        .level(Level.getLevelFromInt(resultSet.getInt("level")))
                        .deadline(new Date(resultSet.getDate("deadline").getTime()))
                        .done(resultSet.getBoolean("done"))
                        .build();
                toDos.add(toDo);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return toDos;
    }

    public ToDo getToDoById(long id) {
        ToDo toDo = null;
        try {
            getByIdQuery.setLong(1, id);
            ResultSet resultSet = getByIdQuery.executeQuery();

            if(resultSet.next()) {
                toDo = ToDo.builder()
                        .id(resultSet.getLong("id"))
                        .shortDescription(resultSet.getString("shortDescription"))
                        .longDescription(resultSet.getString("longDescription"))
                        .deadline(new Date(resultSet.getDate("deadline").getTime()))
                        .level(Level.getLevelFromInt(resultSet.getInt("level")))
                        .done(resultSet.getBoolean("done"))
                        .build();
                System.out.println(toDo);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return toDo;
    }

    public void addToDo(ToDo toDo)    {
        try {
            addToDoQuery.setString(1, toDo.getShortDescription());
            addToDoQuery.setString(2, toDo.getLongDescription());
            addToDoQuery.setDate(3, new java.sql.Date(toDo.getDeadline().getTime()));
            addToDoQuery.setInt(4, toDo.getLevel().getOrdinal());
            addToDoQuery.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void markToDoDone(long id)
    {
        try {
            markToDoDoneQuery.setLong(1, id);
            markToDoDoneQuery.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
