import * as React from "react";
import ToDoRow from "./ToDoRow.js";
import "./ToDoList.css";

class ToDoList extends React.Component
{
    constructor(props){
        super(props);
        this.state = {
            data: null,
            isLoading: false,
            error: null
        }
    }

    componentDidMount(){
        this.setState({isLoading: true})
        window.fetch("http://localhost:8080/api/todo/getAll").then(response => response.json()).then(result => {
            console.log(result);
            this.setState({
            data: result,
            isLoading: false
        })}).catch(error => {
            console.log(error);
            this.setState({
            error,
            isLoading: false
        })});
    }

    render(){
        return (
            <table>
                <thead>
                    <tr>
                        <td>Rövid leírás</td>
                        <td>Hosszú leírás</td>
                        <td>Fontosság</td>
                        <td>Határidő</td>
                        <td>Elkészült?</td>
                    </tr>
                </thead>
                <tbody>
                    {this.state.data?  this.state.data.map((todo, i) => {
                        console.log(todo);
                        return <ToDoRow shortDesc={todo.shortDescription} longDesc={todo.longDescription} 
                        deadline={new Date(todo.deadline).toLocaleString().substring(0,14)} level={todo.level} isDone={todo.done} />
                    }) : <tr><td colSpan="5">Nincs megjelenítendő adat.</td></tr>}
                    {/* <ToDoRow shortDesc="TEJ" longDesc="Tejet" deadline="2021.05.20." isDone="false" level="HIGH" />
                    <ToDoRow shortDesc="TEJ" longDesc="Tejet" deadline="2021.05.20." isDone="false" level="MEDIUM" />
                    <ToDoRow shortDesc="TEJ" longDesc="Tejet" deadline="2021.05.20." isDone="false" level="LOW" /> */}
                </tbody>
            </table>
        );
    }
}

export default ToDoList;
