import React, {memo} from "react";
import {MovieProps} from "./MovieProps";
import {IonItem, IonLabel} from "@ionic/react";

interface MoviePropsExt extends MovieProps {
    onEdit: (id?: string) => void;
}

const Movie: React.FC<MoviePropsExt> = ({id, title, releaseDate, rating, isViewed, onEdit}) => {
    return (
        <IonItem onClick={() => onEdit(id)}>
            <IonLabel>{title}</IonLabel>
        </IonItem>
    );
};

export default memo(Movie);