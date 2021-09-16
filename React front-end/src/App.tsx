import React, {Component, Suspense} from "react";
import { CookiesProvider } from "react-cookie";
import { BrowserRouter } from "react-router-dom";
import { renderRoutes } from "react-router-config";
import { Provider as StoreProvider } from "react-redux";
import appRoutes from "./App/routes";
import defaultStore from "./App/store/configureStore";
import { Provider } from 'react-redux'


class App extends Component {

    render() {
        return (
            <Provider store={defaultStore}>
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
            </Provider>
        );
    };
}

export default App