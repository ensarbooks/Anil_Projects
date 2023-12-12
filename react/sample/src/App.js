import { Header } from "./Header";
import { TaskList } from "./TaskList";
import { AddTask } from "./AddTask";
import'./App.css';

 function App() {
  
  
  return(
    <div className="App">
      <header />
      <AddTask />
      <TaskList />
    </div>
  );
}

export default App;
