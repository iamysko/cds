import { RouteConfig } from "react-router-config";
import LandingSection from "./pages/landing/LandingSection";
import DashboardSection from "./pages/dashboard";
import InfractionSection from "./pages/infractions";
import UserSection from "./pages/users";
import PropertiesSection from "./pages/properties"


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
    {
        path: "/properties/",
        exact: true,
        component: PropertiesSection
    },
];


export default viewsRoutes;
