import React, { memo } from "react";
import { IonItem, IonLabel } from "@ionic/react";
import { Movie } from "./Movie";
import styles from "./styles.module.css";

interface MoviePropsExtended extends Movie {
    onEdit: (_id?: string) => void;
}

const MovieComponent: React.FC<MoviePropsExtended> = ({_id, title, releaseDate, rating, isViewed, isSaved, onEdit }) => (
    <IonItem onClick={()=> onEdit(_id)}>
        <div className={styles.movieContainer}>
            <div className={styles.movieCover}>
                <div className={styles.square}>
                </div>
            </div>
            <div className={styles.movieInfo}>
                <IonLabel className={styles.movieTitle}>
                </IonLabel>
                <div className={styles.component}>
                    <p id={"title"}>Title: {title} </p>
                    <p>Rating: {rating} </p>
                    {releaseDate && (
                        <p>Released on: {new Date(releaseDate).toDateString()} </p>
                    )}
                     <p>Is viewed: {isViewed ? "Yes" : "No"}</p>
                </div>
            </div>
        </div>
    </IonItem>
);

export default memo(MovieComponent);
