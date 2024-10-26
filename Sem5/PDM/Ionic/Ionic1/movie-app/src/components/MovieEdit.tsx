import {getLogger} from "../core";
import {RouteComponentProps} from "react-router";
import React, {useCallback, useContext, useEffect, useState} from "react";
import {MovieContext} from "./MovieProvider";
import {MovieProps} from "./MovieProps";
import {
    IonButton,
    IonButtons, IonCheckbox,
    IonContent, IonDatetime,
    IonHeader,
    IonInput, IonLabel,
    IonLoading,
    IonPage,
    IonTitle,
    IonToolbar
} from "@ionic/react";

const logger = getLogger('MovieEdit');

interface MovieEditProps extends RouteComponentProps<{
    id?: string,
}> {}

const MovieEdit: React.FC<MovieEditProps> = ({history, match}) => {
    const {movies, saving, savingError, saveMovie} = useContext(MovieContext);
    const [title, setTitle] = useState('');
    const [releaseDate, setReleaseDate] = useState<Date>(new Date(Date.now()));
    const [rating, setRating] = useState<number>(1);
    const [isViewed, setIsViewed] = useState<boolean>(false);

    const [movie, setMovie] = useState<MovieProps>();

    useEffect(() => {
        logger('useEffect');
        const routeId = match.params.id || '';
        logger(routeId);
        const movie = movies?.find(it => it.id === routeId);
        setMovie(movie);
        if (movie) {
            setTitle(movie.title);
            setReleaseDate(movie.releaseDate);
            setRating(movie.rating);
            setIsViewed(movie.isViewed);
        }
    }, [match.params.id, movies]);

    const handleSave = useCallback(() => {
        const editedMovie = movie ? {...movie, title, releaseDate, rating, isViewed} : {
            title,
            releaseDate,
            rating,
            isViewed
        };
        saveMovie && saveMovie(editedMovie).then(() => history.goBack());
    }, [movie, saveMovie, title, releaseDate, rating, isViewed, history]);

    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonTitle>Edit movie</IonTitle>
                    <IonButtons slot="end">
                        <IonButton onClick={handleSave}>
                            Save
                        </IonButton>
                    </IonButtons>
                </IonToolbar>
            </IonHeader>
            <IonContent>
                <IonLabel><b>Title</b></IonLabel>
                <IonInput value={title} onIonChange={e => setTitle(e.detail.value || '')}/>

                <IonLabel><b>Release date</b></IonLabel>
                <IonDatetime presentation="date" value={releaseDate.toString()} onIonChange={e => {
                    setReleaseDate(new Date(Date.parse(e.detail.value?.toString() || new Date().toString())));
                }}/>

                <IonLabel><b>Rating</b></IonLabel>
                <IonInput type="number" value={rating} onIonChange={e => setRating(parseInt(e.detail.value || '0'))}/>

                <IonLabel><b>Is viewed</b></IonLabel>
                <IonCheckbox checked={isViewed} onIonChange={e => setIsViewed(e.detail.checked)}/>

                <IonLoading isOpen={saving}/>

                {savingError && (
                    <div>{savingError.message || 'Failed to save movie'}</div>
                )}
            </IonContent>
        </IonPage>
    );
};

export default MovieEdit;