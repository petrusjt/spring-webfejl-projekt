package hu.unideb.controllers;

import hu.unideb.connection.MySQLConnection;
import hu.unideb.connection.ToDoRepository;
import hu.unideb.entities.ToDo;
import hu.unideb.exceptions.MySqlConnectionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.util.List;

@RestController
@RequestMapping("/api/todo")
public class ToDoController {
    private ToDoRepository toDoRepository = new ToDoRepository();

    @GetMapping("/getAll")
    public ResponseEntity<List<ToDo>> getToDos()
    {
        return new ResponseEntity<>(toDoRepository.getAllToDos(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ToDo> getToDo(@PathVariable long id)
    {
        ToDo toDo = toDoRepository.getToDoById(id);
        if(toDo == null)
        {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(toDo, HttpStatus.OK);
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


}
