import { Redirect, Route } from 'react-router-dom';
import { IonApp, IonRouterOutlet, setupIonicReact } from '@ionic/react';
import { IonReactRouter } from '@ionic/react-router';

/* Core CSS required for Ionic components to work properly */
import '@ionic/react/css/core.css';

/* Basic CSS for apps built with Ionic */
import '@ionic/react/css/normalize.css';
import '@ionic/react/css/structure.css';
import '@ionic/react/css/typography.css';

/* Optional CSS utils that can be commented out */
import '@ionic/react/css/padding.css';
import '@ionic/react/css/float-elements.css';
import '@ionic/react/css/text-alignment.css';
import '@ionic/react/css/text-transformation.css';
import '@ionic/react/css/flex-utils.css';
import '@ionic/react/css/display.css';

/**
 * Ionic Dark Mode
 * -----------------------------------------------------
 * For more info, please see:
 * https://ionicframework.com/docs/theming/dark-mode
 */

/* import '@ionic/react/css/palettes/dark.always.css'; */
/* import '@ionic/react/css/palettes/dark.class.css'; */
import '@ionic/react/css/palettes/dark.system.css';

/* Theme variables */
import './theme/variables.css';
import MovieList from "./components/MovieList";
import {MovieProvider} from "./components/MovieProvider";
import MovieEdit from "./components/MovieEdit";

setupIonicReact();

const App: React.FC = () => (
  <IonApp>
    <MovieProvider>
      <IonReactRouter>
        <IonRouterOutlet>
          <Route path="/movies" component={MovieList} exact={true}/>
            <Route path="/movies/:id" component={MovieEdit} exact={true}/>
            <Route path="/movie" component={MovieEdit} exact={true}/>
          <Route exact path="/" render={() => <Redirect to="/movies"/>}/>
        </IonRouterOutlet>
      </IonReactRouter>
    </MovieProvider>
  </IonApp>
);

export default App;
