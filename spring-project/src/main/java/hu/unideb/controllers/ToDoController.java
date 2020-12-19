package hu.unideb.controllers;

import hu.unideb.connection.MySQLConnection;
import hu.unideb.connection.ToDoRepository;
import hu.unideb.entities.ToDo;
import hu.unideb.exceptions.MySqlConnectionFailedException;
import hu.unideb.exceptions.RecordAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@CrossOrigin //("http://localhost:8081")
@RestController
@RequestMapping("/api/todo")
public class ToDoController {
    private ToDoRepository toDoRepository = new ToDoRepository();

    @GetMapping("/getAll")
    public ResponseEntity<List<ToDo>> getToDos()
    {
        try{
            return new ResponseEntity<>(toDoRepository.getAllToDos(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<ToDo> getToDo(@PathVariable long id)
    {
        ToDo toDo = null;
        try {
            toDo = toDoRepository.getToDoById(id);
            if(toDo == null)
            {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(toDo, HttpStatus.OK);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addToDo")
    public ResponseEntity addToDo(@RequestBody ToDo toDo)
    {
        try {
            toDoRepository.addToDo(toDo);
            return new ResponseEntity(HttpStatus.OK);
        }catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (RecordAlreadyExistsException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/markToDoDone/{id}")
    public ResponseEntity markToDoDone(@PathVariable long id)
    {
        try {
            toDoRepository.markToDoDone(id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/updateToDo")
    public ResponseEntity updateToDo(@RequestBody ToDo toDo)
    {
        try {
            toDoRepository.updateToDo(toDo);
            return new ResponseEntity(HttpStatus.OK);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }


}
