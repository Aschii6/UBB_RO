import axios from 'axios';
import {getLogger} from '../core';
import {MovieProps} from "./MovieProps";

const logger = getLogger('moviesApi');

const baseUrl = 'localhost:3000';
const movieUrl = `http://${baseUrl}/movie`;
const moviesUrl = `http://${baseUrl}/movies`;

interface ResponseProps<T> {
    data: T;
}

function withLogs<T>(promise: Promise<ResponseProps<T>>, fnName: string): Promise<T> {
    logger(`${fnName} - started`);

    return promise
        .then(res => {
            logger(`${fnName} - succeeded`);
            return Promise.resolve(res.data);
        })
        .catch(err => {
            logger(`${fnName} - failed`);
            return Promise.reject(err);
        });
}

const config = {
    headers: {
        'Content-Type': 'application/json'
    }
};

export const getMovies: () => Promise<MovieProps[]> = () => {
    return withLogs(axios.get(moviesUrl, config), 'getMovies');
}

export const createMovie: (movie: MovieProps) => Promise<MovieProps[]> = movie => {
    return withLogs(axios.post(movieUrl, movie, config), 'createMovie');
}

export const updateMovie: (movie: MovieProps) => Promise<MovieProps[]> = movie => {
    return withLogs(axios.put(`${moviesUrl}/${movie.id}`, movie, config), 'updateMovie');
}

interface MessageData {
    event: string;
    payload: {
        movie: MovieProps;
    };
}

export const newWebSocket = (onMessage: (data: MessageData) => void) => {
    const ws = new WebSocket(`ws://${baseUrl}`);

    ws.onopen = () => {
        logger('web socket onopen');
    };
    ws.onclose = () => {
        logger('web socket onclose');
    };
    ws.onerror = error => {
        logger('web socket onerror', error);
    };
    ws.onmessage = message => {
        logger('web socket onmessage');
        onMessage(JSON.parse(message.data));
    };

    return () => {
        ws.close();
    }
}