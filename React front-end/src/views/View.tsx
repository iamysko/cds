import { renderRoutes } from "react-router-config";
import Navbar from './components/Navbar/Navbar'
import viewsRoutes from "./routes";
import {useEffect, useState} from "react";
import axios from "axios";
import {createStore} from "redux";
import {configureStore} from "../App/store/configureStore";

export default function View () {

    interface AuthUser {
        id: number
        username: string
        avatar: string;
        discriminator: string
        public_flags: number
        banner: string
        banner_color: string
        accent_color: string
    }



    useEffect(() => {
        fetchData();
    }, [])

    const fetchData = () => {
        axios({
            method: 'get',
            withCredentials: true,
            url: `http://localhost:8080/cds/rdss/checkAuthorization`,
            headers: {

            }
        })
            .then(response => {
                console.log(response.data)
                if (response.data.toString() === "false") {
                    setAuthorized(0)
                } else {
                }
                }
            )
    }

    const [authorized, setAuthorized] = useState(0);

    return (

        <>
                    {<Navbar/>}
            {
                authorized === 0 ?
                    <div className={"Unauthorized"}>
                        <h1> Not authorized</h1>
                    </div>
                    :
                    <div className={'content'}>

                        {
                            renderRoutes(
                                viewsRoutes
                            )
                        }
                    </div>
            }
            {/* Footer*/}


        </>
    );
};
