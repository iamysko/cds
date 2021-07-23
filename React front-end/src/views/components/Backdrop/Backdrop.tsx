import React from "react";

import './Backdrop.css';

const Backdrop = (props: { Click: React.MouseEventHandler<HTMLDivElement > }) => {
    return(
        <div className={'Backdrop'} onClick={props.Click}/>
    );
};

export default Backdrop;
