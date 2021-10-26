import React from "react";
import ReactDOM from "react-dom";
import "./App/styles/index.css";
import App from "./App";
import * as serviceWorker from "./serviceWorker";
import { Provider } from 'react-redux';
import store from "./redux/store";

const rootElement =  document.getElementById("root");
ReactDOM.render(
    <Provider store={store}>
        <App/>
    </Provider>,



    rootElement

);
serviceWorker.unregister();
