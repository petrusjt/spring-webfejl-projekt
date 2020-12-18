package hu.unideb.connection;

import hu.unideb.entities.Level;
import hu.unideb.entities.ToDo;
import hu.unideb.exceptions.MySqlConnectionFailedException;
import hu.unideb.exceptions.RecordAlreadyExistsException;

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
    private PreparedStatement updateToDoQuery;

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
            addToDoQuery = conn.prepareStatement("INSERT INTO `ToDo` (`Id`, `shortDescription`, `longDescription`, `deadline`, `level`, `done`) " +
                    "VALUES (NULL, ?, ?, ?, ?, NULL);");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            markToDoDoneQuery = conn.prepareStatement("update `ToDo` SET `done` = 1 where `ToDo`.`Id` = ?;");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            updateToDoQuery = conn.prepareStatement("update `ToDo` SET `shortDescription` = ?, `longDescription` = ?, `level` = ?, `deadline` = ? where `ToDo`.`Id` = ?;");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<ToDo> getAllToDos() throws SQLException {
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
            throw throwables;
        }
        return toDos;
    }

    public ToDo getToDoById(long id) throws SQLException {
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
            throw throwables;
        }
        return toDo;
    }

    public void addToDo(ToDo toDo) throws SQLException, RecordAlreadyExistsException {
        try {
            List<ToDo> toDos = getAllToDos();
            if(toDos.contains(toDo))
            {
                throw new RecordAlreadyExistsException();
            }

            addToDoQuery.setString(1, toDo.getShortDescription());
            addToDoQuery.setString(2, toDo.getLongDescription());
            addToDoQuery.setDate(3, new java.sql.Date(toDo.getDeadline().getTime()));
            addToDoQuery.setInt(4, toDo.getLevel().getOrdinal());
            addToDoQuery.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw throwables;
        }
    }

    public void updateToDo(ToDo toDo) throws SQLException {
        try
        {
            updateToDoQuery.setString(1, toDo.getShortDescription());
            updateToDoQuery.setString(2, toDo.getLongDescription());
            updateToDoQuery.setInt(3, toDo.getLevel().getOrdinal());
            updateToDoQuery.setDate(4, new java.sql.Date(toDo.getDeadline().getTime()));
            updateToDoQuery.setLong(5, toDo.getId());
            updateToDoQuery.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw throwables;
        }
    }

    public void markToDoDone(long id) throws SQLException {
        try {
            markToDoDoneQuery.setLong(1, id);
            markToDoDoneQuery.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw throwables;
        }
    }
}
