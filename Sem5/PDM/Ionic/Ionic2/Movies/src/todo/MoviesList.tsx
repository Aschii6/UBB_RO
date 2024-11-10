import React, { useContext, useEffect, useState } from 'react';
import { RouteComponentProps } from 'react-router';
import MovieComponent from './MovieComponent';
import { getLogger } from '../core';
import { MoviesContext } from './MovieProvider';
import { IonContent, 
         IonHeader, 
         IonList, 
         IonLoading, 
         IonPage, 
         IonTitle, 
         IonToolbar,
         IonToast, 
         IonFab,
         IonFabButton,
         IonIcon,
         IonButton, 
         IonButtons,
         IonInfiniteScroll,
         IonInfiniteScrollContent,
         IonSearchbar,
         IonSelect, IonSelectOption } from '@ionic/react';

import { add } from 'ionicons/icons';
import { AuthContext } from '../auth';
import { NetworkState } from '../pages/NetworkState';
import { Movie } from './Movie';
import styles from "./styles.module.css";

const log = getLogger('moviesList');
const moviesPerPage = 3;
const filterValues = ["All", "Is viewed", "Isn't viewed"];

export const MoviesList: React.FC<RouteComponentProps> = ({ history }) => {
  const { movies, fetching, fetchingError, successMessage, closeShowSuccess } = useContext(MoviesContext);
  const { logout } = useContext(AuthContext);
  const [isOpen, setIsOpen]= useState(false);
  const [index, setIndex] = useState<number>(0);
  const [moviesAux, setMoviesAux] = useState<Movie[] | undefined>([]);
  const [more, setHasMore] = useState(true);
  const [searchText, setSearchText] = useState('');
  const [filter, setFilter] = useState("All");
  
  useEffect(()=>{
    if(fetching) setIsOpen(true);
    else setIsOpen(false);
  }, [fetching]);

  log('render');
  console.log(movies);

  function handleLogout(){
    logout?.();
    history.push('/login');
  }

  //pagination
  useEffect(()=>{
    fetchData();
  }, [movies]);

  // searching
  useEffect(()=>{
    if (searchText === "") {
      setMoviesAux(movies);
    }
    if (movies && searchText !== "") {
      setMoviesAux(movies.filter(movie => movie.title.toLocaleLowerCase()!.includes(searchText.toLocaleLowerCase())));
    }
  }, [searchText]);

   // filtering
   useEffect(() => {
    if (movies && filter) {
      setMoviesAux(movies.filter(movie => {
          if (filter === "All")
            return movie;
          else if (filter === "Is viewed")
            return movie.isViewed;
          else
            return !movie.isViewed;
        }));
    }
}, [filter]);

  function fetchData() {
    if(movies){
      const newIndex = Math.min(index + moviesPerPage, movies.length);
      if( newIndex >= movies.length){
          setHasMore(false);
      }
      else{
          setHasMore(true);
      }
      setMoviesAux(movies.slice(0, newIndex));
      setIndex(newIndex);
    }
  }

  async function searchNext($event: CustomEvent<void>){
    await fetchData();
    await ($event.target as HTMLIonInfiniteScrollElement).complete();
  }

  return (
    <IonPage>
      <IonHeader>
        <IonToolbar>
          <IonTitle>
            Movie app - <NetworkState/>
          </IonTitle>
          <IonSearchbar className={styles.customSearchBar} placeholder="Search by title" value={searchText} debounce={200} onIonInput={(e) => {
                        setSearchText(e.detail.value!);
                    }} slot='end'>
          </IonSearchbar>
          <IonSelect 
            className={styles.selectBar} 
            slot="end" 
            value={filter} 
            placeholder="Filter" 
            onIonChange={(e) => setFilter(e.detail.value)}>
                        {filterValues.map((each) => (
                            <IonSelectOption key={each} value={each}>
                                {each}
                            </IonSelectOption>
                        ))}
          </IonSelect>
          <IonButtons slot='end'>
             <IonButton onClick={handleLogout}>Logout</IonButton>
          </IonButtons>
        </IonToolbar>
      </IonHeader>
      <IonContent fullscreen>
        <IonLoading isOpen={isOpen} message="Fetching movies..." />
        {moviesAux && (
          <IonList>
            {moviesAux.map(movie => 
              <MovieComponent key={movie._id} _id={movie._id} 
              title={movie.title}
              releaseDate={movie.releaseDate}
              rating={movie.rating}
              isViewed={movie.isViewed}
              isSaved={movie.isSaved}
              onEdit={id => history.push(`/movie/${id}`)} /> 
            )}
          </IonList>
        )}
        <IonInfiniteScroll threshold="100px" disabled={!more} onIonInfinite={(e:CustomEvent<void>) => searchNext(e)} >
          <IonInfiniteScrollContent loadingText="Loading more movies..." >
          </IonInfiniteScrollContent>
        </IonInfiniteScroll>
        {fetchingError && (
          <div>{fetchingError.message || 'Failed to fetch movies'}</div>
        )}
        <IonFab vertical="bottom" horizontal="end" slot="fixed">
          <IonFabButton onClick={() => history.push('/movie')}>
            <IonIcon icon={add} />
          </IonFabButton>
        </IonFab>
        <IonToast
          isOpen={!!successMessage}
          message={successMessage}
          position='bottom'
          buttons={[
            {
              text: 'Dismiss',
              role: 'cancel',
              handler: () => {
                console.log('More Info clicked');
              },
            }]}
          onDidDismiss={closeShowSuccess}
          duration={5000}
          />
      </IonContent>
    </IonPage>
  );
};