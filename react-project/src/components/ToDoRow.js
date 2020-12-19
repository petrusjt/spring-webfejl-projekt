import * as React from "react";

class ToDoRow extends React.Component
{
    render()
    {
        
        return (
            <tr>
                <td>{this.props.shortDesc}</td>
                <td>{this.props.longDesc}</td>
                <td>{this.props.level}</td>
                <td>{this.props.deadline}</td>
                <td><input type="checkbox" defaultChecked={this.props.isDone === true}></input></td>
            </tr>
        );
    }
    
}

export default ToDoRow;