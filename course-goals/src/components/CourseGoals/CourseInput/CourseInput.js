


// import React, { useState } from "react";

// import Button from "../../UIButton/Button";
// import styles from "./CourseInput.module.css";

// const CourseInput = (props) => {
//   const [enteredValue, setEnteredValue] = useState("");
//   const [isValid, setIsValid] = useState(true);

//   const goalInputChangeHandler = (event) => {
//     if (event.target.value.trim().length > 0) {
//       setIsValid(true);
//     }
//     setEnteredValue(event.target.value);
//   };

//   const formSubmitHandler = (event) => {
//     event.preventDefault();
//     if (enteredValue.trim().length === 0) {
//       setIsValid(false);
//       return;
//     }
//     props.onAddGoal(enteredValue);
//     setEnteredValue(""); // Reset the entered value
//   };

//   return (
//     <form onSubmit={formSubmitHandler}>
//       <div className={`${styles["form-control"]} ${!isValid && styles.invalid}`}>
//         <label>Course Goal</label>
//         <input type="text" onChange={goalInputChangeHandler} value={enteredValue} />
//       </div>
//       <Button type="submit">Add Goal</Button>
//     </form>
//   );
// };

// export default CourseInput;
import React, { useState } from "react";
import axios from "axios";
import Button from "../../UIButton/Button";
import styles from "./CourseInput.module.css";

const CourseInput = (props) => {
  const [enteredValue, setEnteredValue] = useState("");
  const [isValid, setIsValid] = useState(true);

  const goalInputChangeHandler = (event) => {
    if (event.target.value.trim().length > 0) {
      setIsValid(true);
    }
    setEnteredValue(event.target.value);
  };

  const formSubmitHandler = async (event) => {
    event.preventDefault();
    if (enteredValue.trim().length === 0) {
      setIsValid(false);
      return;
    }

    const data = {
      goal: enteredValue,
      // You can add more fields if needed
    };

    try {
      // Use Axios to make a POST request to Firebase Realtime Database
      const response = await axios.post(
        "https://react-getting-started-65151-default-rtdb.firebaseio.com//Goals.json",
        data
      );

      console.log(response);
      props.onAddGoal(enteredValue);
      setEnteredValue(""); // Reset the entered value
    } catch (error) {
      console.error("Error adding data:", error);
    }
  };

  return (
    <form onSubmit={formSubmitHandler}>
      <div className={`${styles["form-control"]} ${!isValid && styles.invalid}`}>
        <label>Course Goal</label>
        <input type="text" onChange={goalInputChangeHandler} value={enteredValue} />
      </div>
      <Button type="submit">Add Goal</Button>
    </form>
  );
};

export default CourseInput;
