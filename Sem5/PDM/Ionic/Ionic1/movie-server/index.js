const Koa = require('koa');
const app = new Koa();

const server = require('http').createServer(app.callback());
const WebSocket = require('ws');
const wss = new WebSocket.Server({server});
const Router = require('koa-router');
const cors = require('koa-cors');
const bodyParser = require('koa-bodyparser');

app.use(bodyParser());
app.use(cors());

app.use(async (ctx, next) => {
    const start = new Date();
    await next();
    const ms = new Date() - start;
    console.log(`${ctx.method} ${ctx.url} ${ctx.response.status} - ${ms} ms`);
});

app.use(async (ctx, next) => {
    await new Promise(resolve => setTimeout(resolve, 1000));
    await next();
});

app.use(async (ctx, next) => {
    try {
        await next();
    } catch (err) {
        ctx.response.status = 500;
        ctx.response.body = {message: err.message || "unknown error"};
    }
});

class Movie {
    constructor({id, title, rating, releaseDate, isViewed}) {
        this.id = id;
        this.title = title;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.isViewed = isViewed;
    }
}

const movies = [];

// baza de date @.@
// chestia cu versiuni ptr offline

movies.push(new Movie({
    id: "1", title: "Titanic", rating: 10,
    releaseDate: new Date("10/12/2023"), isViewed: true
}));
movies.push(new Movie({
    id: "2", title: "Transformers One", rating: 9,
    releaseDate: new Date("10/12/2024"), isViewed: false
}));

// vreo 3 linii de cod le am sarit

const broadcast = data =>
    wss.clients.forEach(client => {
        if (client.readyState === WebSocket.OPEN) {
            client.send(JSON.stringify(data))
        }
    });

const router = new Router();

router.get("/movies", ctx => {
    ctx.response.body = movies;
    ctx.response.status = 200;
});

router.get("/movies/:id", async (ctx) => {
    const id = ctx.request.params.id;
    const movie = movies.find(m => m.id === id);
    if (movie) {
        ctx.response.body = movie;
        ctx.response.status = 200;
    } else {
        ctx.response.body = {message: "Movie not found"};
        ctx.response.status = 404;
    }
});

const createMovie = async (ctx) => {
    const movie = ctx.request.body;

    console.log(movie);

    if (!movie.title || !movie.releaseDate) {
        ctx.response.status = 400;
        ctx.response.body = {message: "Attributes missing"};
        return;
    }

    movie.id = `${parseInt(movies[movies.length - 1].id) + 1}`; // gresit cred
    movies.push(movie);
    ctx.response.body = movie;
    ctx.response.status = 201;
    broadcast({event: "created", payload: {movie}});
};

router.post("/movie", async (ctx) => {
    await createMovie(ctx);
});

router.put("/movies/:id", async (ctx) => {
    const id = ctx.params.id;
    const movie = ctx.request.body;

    const movieId = movie.id;

    if (movieId && id !== movieId) {
        ctx.response.status = 400;
        ctx.response.body = {message: "Param id and body id should be the same"};
        return;
    }

    if (!movieId) {
        await createMovie(ctx);
    } else {
        const index = movies.findIndex(m => m.id === id);
        if (index !== -1) {
            movies[index] = movie;
            ctx.response.body = movie;
            ctx.response.status = 200;
            broadcast({event: "updated", payload: {movie}});
        } else {
            ctx.response.body = {message: "Movie not found"};
            ctx.response.status = 404;
        }
    }
});

setInterval(() => {
    const id = `${parseInt(movies[movies.length - 1].id) + 1}`;
    const movie = new Movie({id, title: `Movie ${id}`, rating: 5,
        releaseDate: new Date(), isViewed: false});

    movies.push(movie);

    console.log(`New movie: ${movie.title}`);
    broadcast({event: "created", payload: {movie}});
}, 10000);


app.use(router.routes());
app.use(router.allowedMethods());

server.listen(3000);
