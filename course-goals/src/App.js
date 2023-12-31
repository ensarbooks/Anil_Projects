// import React, { useState } from "react";

// import CourseGoalList from "./components/CourseGoals/CourseGoalList/CourseGoalList";
// import CourseInput from "./components/CourseGoals/CourseInput/CourseInput";
// import "./App.css";

// const App = () => {
//   const [courseGoals, setCourseGoals] = useState([
//     { text: "Do all exercises!", id: "g1" },
//     { text: "Finish the course!", id: "g2" },
//   ]);

//   const addGoalHandler = (enteredText) => {
//     setCourseGoals((prevGoals) => {
//       const updatedGoals = [...prevGoals];
//       updatedGoals.unshift({ text: enteredText, id: Math.random().toString() });
//       return updatedGoals;
//     });
//   };

//   const deleteItemHandler = (goalId) => {
//     setCourseGoals((prevGoals) => {
//       const updatedGoals = prevGoals.filter((goal) => goal.id !== goalId);
//       return updatedGoals;
//     });
//   };

//   let content = (
//     <p style={{ textAlign: "center" }}>No goals found. Maybe add one?</p>
//   );

//   if (courseGoals.length > 0) {
//     content = (
//       <CourseGoalList items={courseGoals} onDeleteItem={deleteItemHandler} />
//     );
//   }

//   return (
//     <div>
//       <section id="goal-form">
//         <CourseInput onAddGoal={addGoalHandler} />
//       </section>
//       <section id="goals">
//         {content}
//         {/* {courseGoals.length > 0 && (
//           <CourseGoalList
//             items={courseGoals}
//             onDeleteItem={deleteItemHandler}
//           />
//         ) // <p style={{ textAlign: 'center' }}>No goals found. Maybe add one?</p>
//         } */}
//       </section>
//     </div>
//   );
// };

// export default App;

import React, { useState } from "react";

import CourseGoalList from "./components/CourseGoals/CourseGoalList/CourseGoalList";
import CourseInput from "./components/CourseGoals/CourseInput/CourseInput";
import "./App.css";

const App = () => {
  const [courseGoals, setCourseGoals] = useState([
    { text: "Do all exercises!", id: "g1" },
    { text: "Finish the course!", id: "g2" },
  ]);

  const addGoalHandler = (enteredText) => {
    setCourseGoals((prevGoals) => {
      const updatedGoals = [...prevGoals];
      updatedGoals.unshift({ text: enteredText, id: Math.random().toString() });
      return updatedGoals;
    });
  };

  const deleteItemHandler = (goalId) => {
    setCourseGoals((prevGoals) => {
      const updatedGoals = prevGoals.filter((goal) => goal.id !== goalId);
      return updatedGoals;
    });
  };

  const editItemHandler = (goalId, editedText) => {
    setCourseGoals((prevGoals) => {
      const updatedGoals = prevGoals.map((goal) =>
        goal.id === goalId ? { ...goal, text: editedText } : goal
      );
      return updatedGoals;
    });
  };

  let content = (
    <p style={{ textAlign: "center" }}>No goals found. Maybe add one?</p>
  );

  if (courseGoals.length > 0) {
    content = (
      <CourseGoalList
        items={courseGoals}
        onDeleteItem={deleteItemHandler}
        onEditItem={editItemHandler}
      />
    );
  }

  return (
    <div>
      <section id="goal-form">
        <CourseInput onAddGoal={addGoalHandler} />
      </section>
      <section id="goals">{content}</section>
    </div>
  );
};

export default App;
