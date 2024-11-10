import React, {useCallback, useContext, useEffect, useState} from 'react';
import {
  IonButton,
  IonButtons,
  IonContent,
  IonHeader,
  IonInput,
  IonLoading,
  IonPage,
  IonTitle,
  IonToolbar,
  IonBackButton,
  IonLabel,
  IonCheckbox
} from '@ionic/react';
import {getLogger} from '../core';
import {RouteComponentProps} from 'react-router';
import {MoviesContext} from './MovieProvider';
import {Movie} from './Movie';
import styles from './styles.module.css';

const log = getLogger('EditLogger');

interface MovieEditProps extends RouteComponentProps<{
  id?: string;
}> {
}

export const MovieEdit: React.FC<MovieEditProps> = ({history, match}) => {
  const {movies, updating, updateError, updateMovie, deleteMovie} = useContext(MoviesContext);
  const [title, setTitle] = useState('');
  const [rating, setRating] = useState(0);
  const [releaseDate, setReleaseDate] = useState<Date>(new Date(Date.now()));
  const [isViewed, setIsViewed] = useState<boolean>(false);

  const [movieToUpdate, setMovieToUpdate] = useState<Movie>();

  useEffect(() => {
    const routeId = match.params.id || '';
    const movie = movies?.find(it => it._id === routeId);
    setMovieToUpdate(movie);
    if (movie) {
      setTitle(movie.title);
      setReleaseDate(movie.releaseDate);
      setRating(movie.rating);
      setIsViewed(movie.isViewed);
    }
  }, [match.params.id, movies]);

  const handleUpdate = useCallback(() => {
    const editedMovie = {
      ...movieToUpdate,
      title: title,
      rating: rating,
      releaseDate: releaseDate,
      isViewed: isViewed
    };
    log(editedMovie);
    console.log(updateMovie);
    updateMovie && updateMovie(editedMovie).then(() => editedMovie.rating && history.goBack());
  }, [movieToUpdate, updateMovie, title, rating, isViewed, history]); // ?

  const handleDelete = useCallback(() => {
    console.log(movieToUpdate?._id);
    deleteMovie && deleteMovie(movieToUpdate?._id!).then(() => history.goBack());
  }, [movieToUpdate, deleteMovie, title, rating, history]);

  return (
    <IonPage>
      <IonHeader>
        <IonToolbar>
          <IonButtons slot="start">
            <IonBackButton></IonBackButton>
          </IonButtons>
          <IonTitle>Edit</IonTitle>
          <IonButtons slot="end">
            <IonButton onClick={handleUpdate}>
              Update
            </IonButton>
            {/* <IonButton onClick={handleDelete}>
              Delete
            </IonButton> */}
          </IonButtons>
        </IonToolbar>
      </IonHeader>
      <IonContent>
        <IonLabel><b>Title</b></IonLabel>
        <IonInput value={title} onIonChange={e => setTitle(e.detail.value || '')}/>
        <br/>
        <IonLabel><b>Is viewed?</b></IonLabel>
        <IonCheckbox checked={isViewed} onIonChange={e => {
          setIsViewed(e.detail.checked)
        }}/>
        <IonLoading isOpen={updating}/>
        {updateError && (
          <div className={styles.errorMessage}>{updateError.message || 'Failed to update item'}</div>
        )}
      </IonContent>
    </IonPage>
  );
}
