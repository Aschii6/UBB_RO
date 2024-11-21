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
  IonCheckbox, createAnimation
} from '@ionic/react';
import {getLogger} from '../core';
import {RouteComponentProps} from 'react-router';
import {MoviesContext} from './MovieProvider';
import {Movie} from './Movie';
import styles from './styles.module.css';
import {MyPhoto} from "./usePhotos";
import {useCamera} from "./useCamera";
import {useMyLocation} from "./useMyLocation";
import {usePreferences} from "./usePreferences";
import MyMap from "./MyMap";

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
  const [latitude, setLatitude] = useState<number>();
  const [longitude, setLongitude] = useState<number>();

  const [photo, setPhoto] = useState<MyPhoto | null>(null);
  const [photoPath, setPhotoPath] = useState<string | undefined>(undefined);
  // const { takePhoto } = usePhotos();

  const [movieToUpdate, setMovieToUpdate] = useState<Movie>();

  const {getPhoto} = useCamera();

  const {get, set} = usePreferences();

  const myLocation = useMyLocation();
  const {latitude: lat, longitude: lng} = myLocation?.position?.coords || {};

  useEffect(() => {
    const routeId = match.params.id || '';
    const movie = movies?.find(it => it._id === routeId);
    setMovieToUpdate(movie);
    if (movie) {
      setTitle(movie.title);
      setReleaseDate(movie.releaseDate);
      setRating(movie.rating);
      setIsViewed(movie.isViewed);
      // if (movie.photo)
      //   setPhoto(movie.photo);
      if (movie.photoPath) {
        setPhotoPath(movie.photoPath);

        get(movie.photoPath).then(data => {
          if (data) {
            const parsedPhoto = JSON.parse(data);
            setPhoto(parsedPhoto);
          }
        });
      }
      if (movie.latitude)
        setLatitude(movie.latitude);
      else
        setLatitude(lat);
      if (movie.longitude)
        setLongitude(movie.longitude);
      else
        setLongitude(lng);
    }
  }, [match.params.id, movies]);

  const handleUpdate = useCallback(() => {
    const editedMovie = {
      ...movieToUpdate,
      title: title,
      rating: rating,
      releaseDate: releaseDate || new Date(Date.now()),
      isViewed: isViewed,
      // photo: photo || undefined,
      photoPath: photo?.filepath,
      latitude: latitude,
      longitude: longitude,
    };
    log(editedMovie);
    console.log(updateMovie);
    updateMovie && updateMovie(editedMovie).then(() => editedMovie.rating && history.goBack());
  }, [movieToUpdate, updateMovie, title, rating, isViewed, releaseDate, photo, latitude, longitude, history]); // ?

  const handleDelete = useCallback(() => {
    console.log(movieToUpdate?._id);
    deleteMovie && deleteMovie(movieToUpdate?._id!).then(() => history.goBack());
  }, [movieToUpdate, deleteMovie, title, rating, history]);

  async function takePhoto() {
    const data = await getPhoto();
    const filepath = new Date().getTime() + '.jpeg';
    const webviewPath = `data:image/jpeg;base64,${data.base64String}`;
    return {filepath, webviewPath};
  }

  const handleTakePhoto = useCallback(async () => {
    const photo = await takePhoto();
    setPhotoPath(photo.filepath);
    setPhoto(photo);

    await set(photo.filepath, JSON.stringify(photo));
    animatePhoto();
  }, [takePhoto]);

  const animatePhoto = async () => {
    const photoElement = document.querySelector('img');
    if (photoElement) {
      const photoAnimation = createAnimation()
        .addElement(photoElement)
        .duration(1500)
        .iterations(Infinity)
        .fromTo('transform', 'scale(1) rotate(0deg)', 'scale(1.1) rotate(45deg)')
      await photoAnimation.play();
    }
  }

  const handleClick = async (event: React.MouseEvent<HTMLIonButtonElement>) => {
    const button = event.currentTarget;

    const animation = createAnimation()
      .addElement(button).duration(1000)
      .keyframes([
        { offset: 0, transform: 'scale(1)', opacity: '1' },
        { offset: 0.5, transform: 'scale(1.5)', opacity: '0.5' },
        { offset: 1, transform: 'scale(1)', opacity: '1' }
      ]);

    await animation.play();
  }

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
        <IonLabel><b>Rating</b></IonLabel>
        <IonInput type="number" value={rating} onIonChange={e => setRating(parseInt(e.detail.value || '0'))}/>
        <br/>
        <IonLabel><b>Is viewed?</b></IonLabel>
        <IonCheckbox checked={isViewed} onIonChange={e => {
          setIsViewed(e.detail.checked)
        }}/>

        <br/>
        <IonLabel><b>Photo</b></IonLabel>
        <IonButton onClick={handleTakePhoto}>Take photo</IonButton>
        {photo && (
          <img src={photo.webviewPath} alt="movie photo"/>
        )}

        <br/>
        <div>latitude: {latitude}</div>
        <div>longitude: {longitude}</div>
        {latitude && longitude &&
            <MyMap
                lat={latitude}
                lng={longitude}
                onMapClick={
                  ({latitude, longitude}) => {
                    setLatitude(latitude);
                    setLongitude(longitude);
                  }
                }
                onMarkerClick={getLogger('onMarker')}
            />}

        <br/>
        <IonButton onClick={handleClick}> Click me </IonButton>
        <br/>

        <IonLoading isOpen={updating}/>
        {updateError && (
          <div className={styles.errorMessage}>{updateError.message || 'Failed to update item'}</div>
        )}
      </IonContent>
    </IonPage>
  );
}
