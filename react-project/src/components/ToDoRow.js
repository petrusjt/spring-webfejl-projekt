import * as React from "react";

class ToDoRow extends React.Component
{
    constructor(props)
    {
        super(props);
        //console.log(props);
        this.state = {
            data: props
        };
        this.handleChange = () => {
            console.log(this.state.data.id);
        };
    }

    render()
    {
        
        return (
            <tr id={"todo-" + this.props.id}>
                <td>{this.props.shortDesc}</td>
                <td>{this.props.longDesc}</td>
                <td>{this.props.level}</td>
                <td>{this.props.deadline}</td>
                <td><input type="checkbox" defaultChecked={this.props.isDone === true} onChange={this.handleChange}></input></td>
            </tr>
        );
    }

    
}

export default ToDoRow;