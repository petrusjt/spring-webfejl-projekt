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
            setupConnection();
        }/* catch (MySqlConnectionFailedException e) {
            e.printStackTrace();
        }*/
        catch (SQLException e){
            e.printStackTrace();
        }
        
    }

    public List<ToDo> getAllToDos() throws SQLException {
        setupConnection();
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
        setupConnection();
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
        setupConnection();
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
        setupConnection();
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
        setupConnection();
        try {
            markToDoDoneQuery.setLong(1, id);
            markToDoDoneQuery.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw throwables;
        }
    }

    private void setupConnection() throws SQLException
    {
        try{
            conn = MySQLConnection.GetConnection();
        }
        catch(MySqlConnectionFailedException e)
        {
            e.printStackTrace();
        }
        getAllQuery = conn.prepareStatement("select * from ToDo;");
        getByIdQuery = conn.prepareStatement("select * from ToDo where id = ?;");
        addToDoQuery = conn.prepareStatement("INSERT INTO `ToDo` (`Id`, `shortDescription`, `longDescription`, `deadline`, `level`, `done`) " +
        "VALUES (NULL, ?, ?, ?, ?, NULL);");
        markToDoDoneQuery = conn.prepareStatement("update `ToDo` SET `done` = 1 where `ToDo`.`Id` = ?;");
        updateToDoQuery = conn.prepareStatement("update `ToDo` SET `shortDescription` = ?, `longDescription` = ?, `level` = ?, `deadline` = ? where `ToDo`.`Id` = ?;");

    }
}
