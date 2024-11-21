import {MyPhoto} from "./usePhotos";

export interface Movie {
    _id?: string;
    title: string;
    releaseDate: Date;
    rating: number;
    isViewed: boolean;
    isSaved?: boolean;
    // photo?: MyPhoto;
    photoPath?: string;
    latitude?: number;
    longitude?: number;
  }
  