import React, {useCallback, useContext, useState} from 'react';
import {
  IonBackButton,
  IonButton,
  IonButtons,
  IonCheckbox,
  IonContent,
  IonDatetime,
  IonHeader,
  IonInput,
  IonLabel,
  IonLoading,
  IonPage,
  IonTitle,
  IonToolbar
} from '@ionic/react';
import {getLogger} from '../core';
import {RouteComponentProps} from 'react-router';
import {MoviesContext, MoviesProvider} from './MovieProvider';
import {Movie} from './Movie';
import styles from './styles.module.css';

const log = getLogger('SaveLogger');

interface MovieEditProps extends RouteComponentProps<{
  id?: string;
}> {
}

export const MovieAdd: React.FC<MovieEditProps> = ({history, match}) => {
  const {movies, updating, updateError, addMovie} = useContext(MoviesContext);
  const [title, setTitle] = useState('');
  const [releaseDate, setReleaseDate] = useState<Date>(new Date(Date.now()));
  const [rating, setRating] = useState(0);
  const [isViewed, setIsViewed] = useState<boolean>(false);
  const [movieToUpdate, setMovieToUpdate] = useState<Movie>();

  const handleAdd = useCallback(() => {
    const editedMovie = {
      ...movieToUpdate,
      title: title,
      releaseDate: releaseDate,
      rating: rating,
      isViewed: isViewed,
    };

    log(editedMovie);
    console.log(updateError);
    addMovie && addMovie(editedMovie).then(() => editedMovie.rating && history.goBack());
  }, [movieToUpdate, addMovie, title, releaseDate, rating, isViewed, history]);

  const dateChanged = (value: any) => {
    setReleaseDate(value);
  };

  return (
    <IonPage>
      <IonHeader>
        <IonToolbar>
          <IonButtons slot="start">
            <IonBackButton></IonBackButton>
          </IonButtons>
          <IonTitle>Edit</IonTitle>
          <IonButtons slot="end">
            <IonButton onClick={handleAdd}>
              Add
            </IonButton>
          </IonButtons>
        </IonToolbar>
      </IonHeader>
      <IonContent>
        <IonInput label="Title:" className={styles.customInput} placeholder="New Title" value={title}
                  onIonInput={e => setTitle(prev => e.detail.value || '')}/>

        <IonInput label="Release date:" className={styles.customInput} placeholder="Choose date"
                  value={new Date(releaseDate).toDateString()}/>
        <IonDatetime onIonChange={(e) => dateChanged(e.detail.value)}> </IonDatetime>

        <IonInput label="Rating:" className={styles.customInput} placeholder="New Height" value={rating}
                  onIonInput={e => e.detail.value ? setRating(prev => parseInt(e.detail.value!)) : setRating(0)}/>

        <IonLabel>Is viewed: </IonLabel>
        <IonCheckbox checked={isViewed} onIonChange={e => setIsViewed(e.detail.checked)}></IonCheckbox>

        <IonLoading isOpen={updating}/>
        {updateError && (
          <div className={styles.errorMessage}>{updateError.message || 'Failed to save item'}</div>
        )}
      </IonContent>
    </IonPage>
  );
}
