export interface Movie {
    _id?: string;
    title: string;
    releaseDate: Date;
    rating: number;
    isViewed: boolean;
    isSaved?: boolean;
  }
  