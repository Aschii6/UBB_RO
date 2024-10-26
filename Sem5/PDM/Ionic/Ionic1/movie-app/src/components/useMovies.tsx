import {getLogger} from "../core";
import {useCallback, useEffect, useReducer} from "react";
import {MovieProps} from "./MovieProps";
import {getMovies} from "./moviesApi";

const logger = getLogger('useMovies');

export interface MoviesState {
    movies?: MovieProps[],
    fetching: boolean,
    fetchingError?: Error,
}

export interface MoviesProps extends MoviesState {
    addMovie: () => void,
}

interface ActionProps {
    type: string,
    payload?: any,
}

const initialState: MoviesState = {
    movies: undefined,
    fetching: false,
    fetchingError: undefined,
};

const FETCH_MOVIES_STARTED = 'FETCH_MOVIES_STARTED';
const FETCH_MOVIES_SUCCEEDED = 'FETCH_MOVIES_SUCCEEDED';
const FETCH_MOVIES_FAILED = 'FETCH_MOVIES_FAILED';

const reducer: (state: MoviesState, action: ActionProps) => MoviesState = (state, {type, payload}) => {
    switch (type) {
        case FETCH_MOVIES_STARTED:
            return {...state, fetching: true};
        case FETCH_MOVIES_SUCCEEDED:
            return {...state, movies: payload.movies, fetching: false};
        case FETCH_MOVIES_FAILED:
            return {...state, fetchingError: payload.error, fetching: false};
        default:
            return state;
    }
};

export const useMovies: () => MoviesProps = () => {
    const [state, dispatch] = useReducer(reducer, initialState);
    const {movies, fetching, fetchingError} = state;

    const addMovie = useCallback(() => {
        logger('addMovie');
    }, []);

    useEffect(getMoviesEffect, [dispatch]);
    logger(`returns - fetching = ${fetching}, movies = ${JSON.stringify(movies)}`);

    return {
        movies,
        fetching,
        fetchingError,
        addMovie,
    };

    function getMoviesEffect() {
        let canceled = false;
        fetchMovies();

        return () => {
            canceled = true;
        };

        async function fetchMovies() {
            try {
                dispatch({type: FETCH_MOVIES_STARTED});
                const movies = await getMovies();

                if (!canceled) {
                    dispatch({type: FETCH_MOVIES_SUCCEEDED, payload: {movies}});
                }
            } catch (error) {
                if (!canceled) {
                    dispatch({type: FETCH_MOVIES_FAILED, payload: {error}});
                }
            }
        }
    }
};