import React, {useContext} from "react";
import {
    IonContent,
    IonFab,
    IonFabButton,
    IonHeader,
    IonIcon, IonList,
    IonLoading,
    IonPage,
    IonTitle,
    IonToolbar
} from "@ionic/react";
import Movie from "./Movie";
import {add} from "ionicons/icons";
import {RouteComponentProps} from "react-router";
import {MovieContext} from "./MovieProvider";

const MovieList: React.FC<RouteComponentProps> = ({history}) => {
    const {movies, fetching, fetchingError} = useContext(MovieContext);

    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonTitle>Movie app</IonTitle>
                </IonToolbar>
            </IonHeader>
            <IonContent>
                <IonLoading isOpen={fetching} message="Fetching movies"/>
                {movies && (
                    <IonList>
                        {movies.map(({id, title, releaseDate, rating, isViewed}) =>
                            <Movie key={id} id={id} title={title} releaseDate={releaseDate} rating={rating}
                                   isViewed={isViewed} onEdit={id => history.push(`/movies/${id}`)}/>)}
                    </IonList>
                )}

                {fetchingError && (
                    <div>{fetchingError.message || 'Failed to fetch movies'}</div>
                )}

                <IonFab vertical="bottom" horizontal="end" slot="fixed">
                    <IonFabButton onClick={() => history.push("/movie")}>
                        <IonIcon icon={add}/>
                    </IonFabButton>
                </IonFab>
            </IonContent>
        </IonPage>
    );
};

export default MovieList;