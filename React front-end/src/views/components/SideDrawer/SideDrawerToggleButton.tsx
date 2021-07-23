import React, {MouseEvent, FunctionComponent} from "react";

import './DrawerToggleButton.css';



const SideDrawerToggleButton = (props: { Click: React.MouseEventHandler<HTMLButtonElement> }) => {
    return(
        <button className={'Toggle-Button'} onClick={props.Click}>
            <div className={'Toggle-Button__Line'}/>
            <div className={'Toggle-Button__Line'}/>
            <div className={'Toggle-Button__Line'}/>
        </button>
    );
};

export default SideDrawerToggleButton;



