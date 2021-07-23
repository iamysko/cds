import React from 'react';
import {Link} from "react-router-dom";
import './SideDrawer.css';

const SideDrawer = (props: { Show: any; }) => {
    let drawerClasses = 'Side-Drawer';
    if(props.Show){
        drawerClasses = 'Side-Drawer Open';
    }
    return(
        <nav className={drawerClasses}>
            <ul>
                <li><Link to={"/dashboard"}>Dashboard</Link></li>
                <li><Link to={"/"}>Landing Section</Link></li>
            </ul>
        </nav>
    );
};

export default SideDrawer;



