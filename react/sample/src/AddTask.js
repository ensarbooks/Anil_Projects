import { useState } from "react"
import "./AddTask.css";

export const AddTask = () => {
    const [taskValue, setTasksValue] =useState ("");

    const handleChange = (event) => {
        setTasksValue(event.target.value);
    }


    return(
        <section className="addtask">
            <form>
                <input className="i-tag" onChange={handleChange} type="text" name="task" id="task" placeholder=" taskname" />
                <button className="add-tag" type="submit">Add Task</button>
                <span className="reset">Reset</span>
            </form>
            <p>{taskValue}</p>
        </section>
    )
}