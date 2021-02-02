import React, { useEffect, useState } from 'react';
import List from './MyList';
import WithListLoading from './withListLoading';
import axios from "axios";


const Filedownload = () => {

  const ListLoading = WithListLoading(List);
  const [appState, setAppState] = useState({
    loading: false,
    images: null,
  });

  useEffect(() => {
    setAppState({ loading: true });
    const apiUrl = `http://localhost:8081/photos`;
    axios.get(apiUrl).then((images) => {
      const allImages = images.data;
      setAppState({ loading: false, images: allImages });
    });
  }, [setAppState]);

  return (
    <div className='App'>
      <div className='container'>
        <h1>My Repositories</h1>
      </div>
      <div className='repo-container'>
        <ListLoading isLoading={appState.loading} images={appState.images} />
      </div>
      <footer>
        <div className='footer'>
          Built{' '}
          <span role='img' aria-label='love'>
            💚
          </span>{' '}
          with by Shedrack Akintayo
        </div>
      </footer>
    </div>
  );
}


export default Filedownload;