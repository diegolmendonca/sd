import React from 'react';

class downloadComponent extends React.Component {
  componentDidMount() {
    const apiUrl = 'http://localhost:8081/photos';
    fetch(apiUrl)
      .then((response) => response.json())
      .then((data) => console.log('This is your data', data));
  }
  render() {
    return <h1>my Component has Mounted, Check the browser 'console' </h1>;
  }
}
export default downloadComponent;