import React, { useCallback, useEffect, useReducer, useContext } from 'react';
import PropTypes from 'prop-types';
import { getLogger } from '../core';
import { getAllMovies, updateMovieAPI, createMovieAPI, newWebSocket, deleteMovieAPI } from './MovieApi';
import { Movie } from './Movie';
import { AuthContext } from '../auth';
import { useNetwork } from '../pages/useNetwork';
import {useIonToast} from "@ionic/react";
import { Preferences } from '@capacitor/preferences';

const log = getLogger('MovieProvider');

type UpdateMovieFn = (movie: Movie) => Promise<any>;
type DeleteMovieFn = (id: string) => Promise<any>;

interface MoviesState {
  movies?: Movie[];
  fetching: boolean;
  fetchingError?: Error | null;
  updating: boolean,
  updateError?: Error | null,
  updateMovie?: UpdateMovieFn,
  addMovie?: UpdateMovieFn,
  deleteMovie?: DeleteMovieFn;
  successMessage?: string;
  closeShowSuccess?: () => void;
}

interface ActionProps {
  type: string,
  payload?: any,
}

const initialState: MoviesState = {
  fetching: false,
  updating: false,
};

const FETCH_MOVIE_STARTED = 'FETCH_MOVIE_STARTED';
const FETCH_MOVIE_SUCCEEDED = 'FETCH_MOVIE_SUCCEEDED';
const FETCH_MOVIE_FAILED = 'FETCH_MOVIE_FAILED';
const UPDATE_MOVIE_STARTED = 'UPDATE_MOVIE_STARTED';
const UPDATE_MOVIE_SUCCEEDED = 'UPDATE_MOVIE_SUCCEEDED';
const UPDATE_MOVIE_FAILED = 'UPDATE_MOVIE_FAILED';
const SHOW_SUCCESS_MESSAGE = 'SHOW_SUCCESS_MESSAGE';
const HIDE_SUCCESS_MESSAGE = 'HIDE_SUCCESS_MESSAGE';
const CREATE_MOVIE_STARTED = 'CREATE_MOVIE_STARTED';
const CREATE_MOVIE_SUCCEEDED = 'CREATE_MOVIE_SUCCEEDED';
const CREATE_MOVIE_FAILED = 'CREATE_MOVIE_FAILED';
const DELETE_MOVIE_STARTED = 'DELETE_MOVIE_STARTED';
const DELETE_MOVIE_SUCCEEDED = 'DELETE_MOVIE_SUCCEEDED';
const DELETE_MOVIE_FAILED = 'DELETE_MOVIE_FAILED';

const reducer: (state: MoviesState, action: ActionProps) => MoviesState
  = (state, { type, payload }) => {
  switch(type){
    case FETCH_MOVIE_STARTED:
      return { ...state, fetching: true, fetchingError: null };
    case FETCH_MOVIE_SUCCEEDED:
      return {...state, movies: payload.movies, fetching: false };
    case FETCH_MOVIE_FAILED:
      return { ...state, fetchingError: payload.error, fetching: false };
    case UPDATE_MOVIE_STARTED:
      return { ...state, updateError: null, updating: true };
    case UPDATE_MOVIE_FAILED:
      return { ...state, updateError: payload.error, updating: false };
    case UPDATE_MOVIE_SUCCEEDED:
      const movies = [...(state.movies || [])];
      const movie = payload.movie;
      const index = movies.findIndex(it => it._id === movie._id);
      movies[index] = movie;
      return { ...state,  movies, updating: false };
    case CREATE_MOVIE_FAILED:
      console.log(payload.error);
      return { ...state, updateError: payload.error, updating: false };
    case CREATE_MOVIE_STARTED:
      return { ...state, updateError: null, updating: true };
    case CREATE_MOVIE_SUCCEEDED:
      const beforeMovies = [...(state.movies || [])];
      const createdMovies = payload.movie;
      console.log(createdMovies);
      const indexOfAdded = beforeMovies.findIndex(it => it._id === createdMovies._id || it.title === createdMovies.title);
      console.log("index: ", indexOfAdded);
      if (indexOfAdded === -1) {
        beforeMovies.splice(0, 0, createdMovies);
      } else {
        beforeMovies[indexOfAdded] = createdMovies;
      }
      console.log(beforeMovies);
      console.log(payload);
      return { ...state,  movies: beforeMovies, updating: false, updateError: null };
    case DELETE_MOVIE_FAILED:
      console.log(payload.error);
      return { ...state, updateError: payload.error, updating: false };
    case DELETE_MOVIE_STARTED:
      return { ...state, updateError: null, updating: true };
    case DELETE_MOVIE_SUCCEEDED:
      const originalMovies = [...(state.movies || [])];
      const deletedMovies = payload.movie;
      const indexOfDeleted = originalMovies.findIndex(it => it._id === deletedMovies._id);
      if (indexOfDeleted > -1) {
        originalMovies.splice(indexOfDeleted, 1);
      }
      console.log(originalMovies);
      console.log(payload);
      return { ...state,  movies: originalMovies, updating: false };
    case SHOW_SUCCESS_MESSAGE:
      const allMovies = [...(state.movies || [])];
      const updatedMovies = payload.updatedMovies;
      console.log(updatedMovies);
      const indexOfMovies = allMovies.findIndex(it => it._id === updatedMovies._id);
      allMovies[indexOfMovies] = updatedMovies;
      console.log(payload);
      return {...state, movies: allMovies, successMessage: payload.successMessage }
    case HIDE_SUCCESS_MESSAGE:
      return {...state, successMessage: payload }

    default:
      return state;
  }
};

export const MoviesContext = React.createContext(initialState);

interface MoviesProviderProps {
  children: PropTypes.ReactNodeLike,
}

export const MoviesProvider: React.FC<MoviesProviderProps> = ({ children }) => {
  const [state, dispatch] = useReducer(reducer, initialState);
  const { movies, fetching, fetchingError, updating, updateError, successMessage } = state;
  const { token } = useContext(AuthContext);
  const { networkStatus } = useNetwork();
  const [toast] = useIonToast();

  useEffect(getItemsEffect, [token]);
  useEffect(wsEffect, [token]);
  useEffect(executePendingOperations, [networkStatus.connected, token, toast]);

  const updateMovie = useCallback<UpdateMovieFn>(updateMoviesCallback, [token]);
  const addMovie = useCallback<UpdateMovieFn>(addMoviesCallback, [token]);
  const deleteMovie = useCallback<DeleteMovieFn>(deleteMoviesCallback, [token]);

  log('returns');

  function getItemsEffect() {
    let canceled = false;
    fetchItems();
    return () => {
      canceled = true;
    }

    async function fetchItems() {
      if(!token?.trim()){
        return;
      }

      try{
        log('fetchMovies started');
        dispatch({ type: FETCH_MOVIE_STARTED });
        const movies = await getAllMovies(token);
        log('fetchItems succeeded');
        if (!canceled) {
          dispatch({ type: FETCH_MOVIE_SUCCEEDED, payload: { movies } });
        }
      } catch (error) {
        log('fetchItems failed');
        if (!canceled) {
          dispatch({ type: FETCH_MOVIE_FAILED, payload: { error } });
        }
      }
    }
  }

  async function updateMoviesCallback(movie: Movie) {
    try {
      log('updateMovies started');
      dispatch({ type: UPDATE_MOVIE_STARTED });
      const updatedMovies = await updateMovieAPI(token, movie);
      log('saveMovies succeeded');
      dispatch({type: UPDATE_MOVIE_SUCCEEDED, payload: { movie: updatedMovies } });
    } catch (error: any) {
      log('updateMovies failed');
      // save item to storage
      console.log('Updating Movie locally...');

      movie.isSaved = true;
      await Preferences.set({
        key: `upd-${movie.title}`,
        value: JSON.stringify({token, movie })
      });
      dispatch({type: UPDATE_MOVIE_SUCCEEDED, payload: { movie: movie } });
      toast("You are offline... Updating Movie locally!", 3000);

      if(error.toJSON().message === 'Network Error')
        dispatch({ type: UPDATE_MOVIE_FAILED, payload: { error: new Error(error.response) } });
    }
  }

  async function addMoviesCallback(movie: Movie){
    try{
      log('addMovies started');
      dispatch({ type: CREATE_MOVIE_STARTED });
      console.log(token);
      const addedMovies = await createMovieAPI(token, movie);
      console.log(addedMovies);
      log('saveMovies succeeded');
      dispatch({ type: CREATE_MOVIE_SUCCEEDED, payload: { movie: addedMovies } });
    }catch(error: any){
      log('addMovies failed');
      console.log(error.response);
      // save item to storage
      console.log('Saving Movie locally...');
      const { keys } = await Preferences.keys();
      const matchingKeys = keys.filter(key => key.startsWith('sav-'));
      const numberOfItems = matchingKeys.length + 1;
      console.log(numberOfItems);

      movie._id = numberOfItems.toString(); // ii adaug si id...
      movie.isSaved = true;
      await Preferences.set({
        key: `sav-${movie.title}`,
        value: JSON.stringify({token, movie })
      });
      dispatch({ type: CREATE_MOVIE_SUCCEEDED, payload: { movie: movie } });
      toast("You are offline... Saving Movie locally!", 3000);

      if(error.toJSON().message === 'Network Error')
        dispatch({ type: CREATE_MOVIE_FAILED, payload: { error: new Error(error.response || 'Network error') } });
    }
  }

  async function deleteMoviesCallback(id: string){
    try{
      log('deleteMovies started');
      dispatch({ type: DELETE_MOVIE_STARTED });
      const deletedMovies = await deleteMovieAPI(token, id);
      console.log('deleted Movie: '+ deletedMovies);
      log('deleteMovies succeeded');
      dispatch({ type: DELETE_MOVIE_SUCCEEDED, payload: { movie: deletedMovies } });
    }catch(error: any){
      log('addMovies failed');
      console.log(error.response.data.message);
      dispatch({ type: DELETE_MOVIE_FAILED, payload: { error: new Error(error.response.data.message) } });
    }
  }

  function executePendingOperations(){
    async function helperMethod(){
      if(networkStatus.connected && token?.trim()){
        log('executing pending operations')
        const { keys } = await Preferences.keys();
        for(const key of keys) {
          if(key.startsWith("sav-")){
            const res = await Preferences.get({key: key});
            console.log("Result", res);
            if (typeof res.value === "string") {
              const value = JSON.parse(res.value);
              value.movie._id=undefined;
              log('creating item from pending', value);
              await addMoviesCallback(value.movie);
              await Preferences.remove({key: key});
            }
          }
        }
        for(const key of keys) {
          if(key.startsWith("upd-")){
            const res = await Preferences.get({key: key});
            console.log("Result", res);
            if (typeof res.value === "string") {
              const value = JSON.parse(res.value);
              log('updating item from pending', value);
              await updateMoviesCallback(value.movie);
              await Preferences.remove({key: key});
            }
          }
        }
      }
    }
    helperMethod();
  }

  function wsEffect() {
    let canceled = false;
    log('wsEffect - connecting');
    let closeWebSocket: () => void;
    if(token?.trim()){
      closeWebSocket = newWebSocket(token, message => {
        if (canceled) {
          return;
        }
        const { event, payload } = message;
        console.log('Provider message: ', message);

        log(`ws message, item ${event}`);
        if (event === 'updated') {
          console.log(payload);
          dispatch({ type: SHOW_SUCCESS_MESSAGE, payload: {successMessage: payload.successMessage, updatedMovies: payload.updatedMovie } });
        }
        else if(event == 'created'){
          console.log(payload);
          dispatch({ type: CREATE_MOVIE_SUCCEEDED, payload: { movie: payload.updatedMovie } });
        }
        else if(event === 'deleted'){
          console.log(payload);
          dispatch({ type: DELETE_MOVIE_SUCCEEDED, payload: { movie: payload.updatedMovie } });
        }
      });
    }
    return () => {
      log('wsEffect - disconnecting');
      canceled = true;
      closeWebSocket?.();
    }
  }

  function closeShowSuccess(){
    dispatch({ type: HIDE_SUCCESS_MESSAGE, payload: null });
  }

  const value = { movies, fetching, fetchingError, updating, updateError, updateMovie, addMovie, deleteMovie, successMessage, closeShowSuccess };

  return (
    <MoviesContext.Provider value={value}>
      {children}
    </MoviesContext.Provider>
  );
};

