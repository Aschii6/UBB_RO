import React from 'react';
import { useAppState } from "./useAppState";
import { useNetwork } from "./useNetwork";
import { IonLabel } from "@ionic/react";


export const NetworkState: React.FC = () => {
    useAppState();
    const {networkStatus} = useNetwork();
    
    return (
      networkStatus.connected ? 
        <IonLabel style={{ margin: '5px'}}>
            <span style={{color: 'green'}}>ONLINE</span>
        </IonLabel> :
        <IonLabel style={{ margin: '5px'}}>
            <span style={{color: 'red'}}>OFFLINE</span>
        </IonLabel>
    );
}