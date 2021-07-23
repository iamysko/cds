import { RouteConfig } from "react-router-config";
import { lazy } from "react";


const appRoutes: RouteConfig[] = [
    {
        path: "/",
        exact: false,
        component: lazy(() => import("../views/View"))
    }
];

export default appRoutes;
