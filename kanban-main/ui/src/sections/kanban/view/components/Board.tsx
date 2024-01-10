import { Draggable, Droppable } from "react-beautiful-dnd";
import Task from "./Task";
import styled from "styled-components";
import { ITask, taskState } from "../models/atoms";
import { useForm } from "react-hook-form";
import { useSetRecoilState } from "recoil";
import React from "react";


interface IForm {
  task: string;
}

interface IBoard {
  boardId: string;
  tasks: ITask[];
  index: number;
}

const Wrapper = styled.div`
  // Wrapping Handle, Area
  display: flex;
  flex-direction: column;
  width: 315px;
  background-color: #f4f6f8;
  border-radius:20px;
  padding-top:20px;
  padding-bottom:30px;
  margin-right:30px;
  
 
  
`;
const Handle = styled.div`
  // Handle of board when dragging
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  height: 57px;
  align-items: center;
  
  
  

  img {
    // display: none;
    width: 33px;
    margin-right:20px;
    padding: 5px;
  }

  &:hover {
    img {
      display: block;
    }
  }
`;
const Header = styled.div`
  // Head of Board
  display: flex;
  flex-direction: row;
  align-items: center;
  
`;
const Label = styled.div<{ boardColor: string }>`
  // Showing board Name
  width: 122px;
  margin-left: 10px;
  font-size: 22px;
  text-align: center;
  line-height: 2;
  padding: 4px 8px;
  border-radius: 5px;
  // background-color: ${(props) => props.boardColor ?? "#333333"};
`;
const TaskCount = styled.div`
  // Showing how many tasks in the board.
  padding-left: 10px;
  font-size: 18px;
  color: ${(props) => props.theme.textBlurColor};
`;
const DeleteImgButton = styled.img``;
const NewTaskForm = styled.form`
  width: 100%;
  
`;
const NewTaskInput = styled.input`
  width: 274px;
  padding: 10px 22px 10px 22px;
  //styling
  border-style: solid;
  border-radius: 5px;
  border-color: transparent;
  outline: none;
  background-color: inherit;
  //font
  font-size: 14px;
  line-height: 2;
  text-align: left;
  color: ${(props) => props.theme.textColor};

  &:hover,
  &:focus {
    outline: none;
    background-color:#ffffff;
  }

  &:focus::placeholder {
    color: transparent;
  }

  &::placeholder {
    color: ${(props) => props.theme.textBlurColor};
  }
`;
const Area = styled.div<{ isDraggingOver: boolean }>`
  display: flex;
  // min-height: 200px;
  flex-direction: column;
  flex-grow: 1;
  // padding-right:50px;
  grid-column:1/3;
  align-items: center;
  row-gap: 14px;
  background-color: ${(props) =>
    props.isDraggingOver ? props.theme.boardDraggingOverColor : "none"};
`;

function Board({ boardId, tasks, index }: IBoard) {
  const boardColorInId = boardId.slice(
    boardId.length - 21,
    boardId.length - 14
  );
  const boardNameInId = boardId.slice(0, boardId.length - 22);
  const setTasks = useSetRecoilState(taskState);
  const { register, handleSubmit, setValue } = useForm<IForm>();
  const onValid = ({ task }: IForm) => {
    const newTask = { id: Date.now(), text: task }; // unique ID for new task by Date.now()
    setTasks((allTasks) => ({
      ...allTasks,
      [boardId]: [newTask, ...allTasks[boardId]],
    }));
    setValue("task", "");
  };
  const onClick = () => {
    // Remove board
    if (
      window.confirm(
        `️Are you sure to remove board "${boardId.slice(
          0,
          boardId.length - 22
        )}" ⁉`
      )
    ) {
      setTasks((allTasks) => {
        let boardEntries = Object.entries(allTasks);
        boardEntries.splice(index, 1);
        return boardEntries.reduce(
          (modifiedBoards, [boardId, tasks]) => ({
            ...modifiedBoards,
            [boardId]: tasks,
          }),
          {}
        );
      });
    }
  };
  return (
    <Draggable draggableId={boardId} index={index}>
      {(provided) => (
        <Wrapper ref={provided.innerRef} {...provided.draggableProps}>
          <Handle {...provided.dragHandleProps}>
            <Header>
              <Label boardColor={boardColorInId}>{boardNameInId}</Label>
              <TaskCount>{tasks.length}</TaskCount>
            </Header>
            <DeleteImgButton onClick={onClick} alt={"binImg"} src={"https://icon-library.com/images/delete-icon-transparent-background/delete-icon-transparent-background-4.jpg"} />
          </Handle>
          <Droppable droppableId={boardId}>
            {(provided, snapshot) => (
              <Area
                ref={provided.innerRef}
                {...provided.droppableProps}
                isDraggingOver={snapshot.isDraggingOver}
              >
                {tasks.map((task, index) => (
                  <Task
                    key={task.id}
                    id={task.id}
                    index={index}
                    text={task.text}
                  />
                ))}
                {provided.placeholder}
                <NewTaskForm onSubmit={handleSubmit(onValid)}>
                  <NewTaskInput
                    autoComplete={"off"}
                    {...register("task", { required: true })}
                    placeholder={"+ New"}
                  />
                </NewTaskForm>
              </Area>
            )}
          </Droppable>
        </Wrapper>
      )}
    </Draggable>
  );
}

export default React.memo(Board);
