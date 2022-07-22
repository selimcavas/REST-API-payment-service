import React from 'react';
import GlobalStyle from './globalStyles';
import {Navbar} from './components';
import { Info } from './components';

function App() {
  return (
    <div className="App">
      <GlobalStyle />
      <Navbar />
      <Info />
    </div>
  );
}


export default App;
