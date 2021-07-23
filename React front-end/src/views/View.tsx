import { renderRoutes } from "react-router-config";
import Navbar from './components/Navbar/Navbar'
import viewsRoutes from "./routes";


export default function View () {
    return (
        <>
            {<Navbar/>}

            <div className={'content'}>
                {
                    renderRoutes(
                        viewsRoutes
                    )
                }
            </div>

            {/* Footer*/}
        </>
    );
};
