
import React, { useState, useEffect } from "react";

import "./CourseGoalItem.css";

const CourseGoalItem = (props) => {
  const [isDeleted, setIsDeleted] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [editedText, setEditedText] = useState(props.children);

  console.log(props);
  const deleteHandler = () => {
    setIsDeleted(true);
    props.onDelete(props.id);
  };

  const editHandler = () => {
    setIsEditing(true);
  };

  const saveEditHandler = () => {
    // Save the edited text and exit edit mode
    props.onEdit(props.id, editedText);
    setIsEditing(false);
  };

  const cancelEditHandler = () => {
    // Cancel the edit and revert to the original text
    setIsEditing(false);
    setEditedText(props.children);
  };

  useEffect(() => {
    // Retrieve deleted goals from localStorage
    const deletedGoals = JSON.parse(localStorage.getItem("deletedGoals")) || [];

    // Check if the current goal is deleted
    if (deletedGoals.includes(props.id)) {
      setIsDeleted(true);
    }
  }, [props.id]);

  useEffect(() => {
    if (isDeleted) {
      // Perform any additional cleanup or persistence logic here
      // For example, you can store the deleted state in localStorage
      const deletedGoals = JSON.parse(localStorage.getItem("deletedGoals")) || [];
      localStorage.setItem("deletedGoals", JSON.stringify([...deletedGoals, props.id]));
    }
  }, [isDeleted, props.id]);

  if (isDeleted) {
    return null; // Don't render the item if it's deleted
  }

  return (
    <li className="goal-item">
      <div>
        {isEditing ? (
          <input
            type="text"
            value={editedText}
            onChange={(e) => setEditedText(e.target.value)}
          />
        ) : (
          props.children
        )}
      </div>
      <div className="styling">
        {isEditing ? (
          <>
            <button className="btn" onClick={saveEditHandler}>Save</button>
            <button className="btn" onClick={cancelEditHandler}>Cancel</button>
          </>
        ) : (
          <>
            <button className="btn" onClick={editHandler}>Edit</button>
            <button className="btn" onClick={deleteHandler}>Delete</button>
          </>
        )}
      </div>
    </li>
  );
};

export default CourseGoalItem;


