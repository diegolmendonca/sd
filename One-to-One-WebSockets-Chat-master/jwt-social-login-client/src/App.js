import React from "react";
import { BrowserRouter, Route, Switch } from "react-router-dom";
import Signin from "./signin/Signin";
import Signup from "./signup/Signup";
import Profile from "./profile/Profile";
import FileUpload from "./image/FileUpload"
import FileDownload from "./image/download/FileDownload"

import Chat from "./chat/Chat";
import "./App.css";

export const AppContext = React.createContext();
const App = (props) => {
  return (
    <div className="App">
      <BrowserRouter>
        <Switch>
          <Route exact path="/profile" render={(props) => <Profile {...props} />} />
          <Route
            exact
            path="/login"
            render={(props) => <Signin {...props} />}
          />
            <Route
            exact
            path="/upload"
            render={(props) => <FileUpload {...props} />}
          />

<Route
            exact
            path="/download"
            render={(props) => <FileDownload {...props} />}
          />
          <Route
            exact
            path="/signup"
            render={(props) => <Signup {...props} />}
          />
          <Route exact path="/chat" render={(props) => <Chat {...props} />} />
        </Switch>
      </BrowserRouter>
    </div>
  );
};

export default App;
