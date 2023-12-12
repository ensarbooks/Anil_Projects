import React from "react";
import Movie from "./Movie";
import classes from "./MovieList.module.css";

const MovieList = (props) => {
  return (
    <ul className={classes["movies-List"]}>
      {props.movies.map((movie) => (
        <Movie
          title={movie.title}
          releaseDate={movie.release}
          openingText={movie.openingText}
        />
      ))}
    </ul>
  );
};

export default MovieList;
