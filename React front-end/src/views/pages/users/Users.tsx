import React, {useEffect, useState } from 'react'
import axios from "axios";
import './Users.css'




const Users = () => {

    interface UserType {
        userId: string
        warnings: number
        mutes: number
        banned: boolean
        userApiData: {
            id: number
            username: string
            avatar: string
            discriminator: string
            public_flags: number
        }
    }

    const [user, setUser] = useState<UserType>();

    const id = window.location.href.split('/')[4]

    useEffect(() => {
        fetchData(id)
    }, [])

    const fetchData = (id: string) => {
        axios({
            method: 'get',
            url: `http://localhost:8080/cds/rdss/panel/get-user-data`,
            headers: {
                'Content-Type': 'application/json',
            }
        })
            .then(response => {
                    
                console.log(response.data)
                }
            )
    }


    const CopyUsername = (str: string) => {
        const el = document.createElement('textarea');
        el.value = str;
        document.body.appendChild(el);
        el.select();
        document.execCommand('copy');
        document.body.removeChild(el);
    }

    return (
        <>
            {user && user.userApiData.id !== 0 ?
                <div className={'UserInfoContainer'}>
                    <div className={'UserDetails UserBox'}>
                        <img
                            className={'responsiveImage'}
                            src={`https://cdn.discordapp.com/avatars/${user.userId}/${user.userApiData.avatar}.${user.userApiData.avatar.startsWith("a_") ?'gif':'png'}?size=512`}/>
                        <h2 onClick={() => CopyUsername(user.userId)}>{user.userApiData.username}#{user.userApiData.discriminator}</h2>
                        <p>Warnings: {user.warnings}</p>
                        <p>Mutes: {user.mutes}</p>
                        <p>Status: Muted</p>
                    </div>

                    <div className={'UserServerDetails UserBox'}>
                        <h1>Server Details</h1>
                        <p>Joined at:</p>
                        <p>left at:</p>
                        <p>roles:</p>
                    </div>

                    <div className={'RobloxDetails UserBox'}>
                        <h1>Roblox Details</h1>
                    </div>


                </div>
                :
                <div className={'UserInfoContainer UserBox'}>
                    <h1>Oops! Looks like this user doesn't exist!</h1>
                </div>
            }

        </>
    );
}

export default Users
