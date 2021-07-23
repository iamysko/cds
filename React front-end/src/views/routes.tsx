import { RouteConfig } from "react-router-config";
import LandingSection from "./pages/landing/LandingSection";
import DashboardSection from "./pages/dashboard";
import InfractionSection from "./pages/infractions";
import UserSection from "./pages/users";


const viewsRoutes: RouteConfig[] = [
    {
        path: "/",
        exact: true,
        component: LandingSection
    },
    {
        path: "/dashboard/",
        exact: true,
        component: DashboardSection
    },
    {
        path: "/infractions/",
        exact: true,
        component: InfractionSection
    },
    {
        path: "/users/:id",
        exact: true,
        component: UserSection
    },
];


export default viewsRoutes;
