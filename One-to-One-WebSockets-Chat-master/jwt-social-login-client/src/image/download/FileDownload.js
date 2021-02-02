import React, { useEffect, useState } from 'react';
import List from './MyList';
import WithListLoading from './withListLoading';

const Filedownload = () => {

  const ListLoading = WithListLoading(List);
  const [appState, setAppState] = useState({
    loading: false,
    images: null,
  });

  useEffect(() => {
    setAppState({ loading: true });
    const apiUrl = `http://localhost:8081/photos`;
    fetch(apiUrl)
      .then((res) => res.json())
      .then((images) => {
        setAppState({ loading: false, images: images });
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