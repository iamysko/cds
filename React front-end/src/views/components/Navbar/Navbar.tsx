import React, {useState} from 'react'
import {Link} from "react-router-dom";
import './Navbar.css';
import SideDrawerToggleButton from "../SideDrawer/SideDrawerToggleButton";
import SideDrawer from "../SideDrawer/SideDrawer";
import Backdrop from '../Backdrop/Backdrop';
import DiscordLogo from '../../../assets/images/Discord-Logo.png';

const Navbar = () => {


    const [State, setState] = useState({
        sideDrawerOpen: false
    })

    const drawerToggleClickHandler = () => {
        setState({ sideDrawerOpen: !State.sideDrawerOpen })
    }

    const backDropClickHandler = () => {
        setState({ sideDrawerOpen: false })
    }

        return (
            <>
                <header className={'Header'}>
                    <nav className={'Navbar'}>
                        <div className={'Navbar-Toggle-Button'}><SideDrawerToggleButton Click={() => drawerToggleClickHandler()}/></div>
                        <div className={'Navbar__Logo'}>
                            <Link to={"/dashboard"}>
                                The Logo
                            </Link>
                        </div>
                        <div className={'spacer'}/>
                        <div className={'Navbar__Items'}>
                            <ul>
                                <li><Link to={"/dashboard"}>Dashboard</Link></li>
                                <li><Link to={"/"}>Landing Section</Link></li>
                                <li className={'Discord__Button'}><a href={""}>
                                    Login with Discord

                                    <img className={'Discord__Button__Logo'} height={25} width={25} src={DiscordLogo} alt={'Discord Logo'}/>

                                </a></li>
                            </ul>
                        </div>
                    </nav>
                </header>
                <SideDrawer Show={State.sideDrawerOpen}/>
                {
                    State.sideDrawerOpen && (
                        <>
                            <Backdrop Click={() => backDropClickHandler()}/>
                        </>
                    )
                }


            </>
        );
};

export default Navbar
