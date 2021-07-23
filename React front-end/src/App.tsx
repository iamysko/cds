import React, {Component, Suspense, useState} from "react";
import { CookiesProvider } from "react-cookie";
import { BrowserRouter } from "react-router-dom";
import { renderRoutes } from "react-router-config";
import { Provider as StoreProvider } from "react-redux";
import appRoutes from "./App/routes";
import defaultStore from "./App/store/configureStore";
import axios from "axios";
import { createStore } from 'redux'



interface UserType {
    userId: string
    warnings: number
    mutes: number
    banned: boolean
    userApiData: {
        id: number
        username: string
        avatar: string
        discriminator: string
        public_flags: number
    }
}


class App extends Component {



    render() {


        return (

            <StoreProvider store={defaultStore}>
                <CookiesProvider>
                    <BrowserRouter>
                        <Suspense fallback={<h2>Loading App contents..</h2>}>
                            {
                                renderRoutes(
                                    appRoutes
                                )
                            }
                        </Suspense>
                    </BrowserRouter>
                </CookiesProvider>
            </StoreProvider>
        );
    };
}

export default App