import React, {useEffect, useState } from 'react'
import axios from "axios";
import './Users.css'
import DiscordDefaultProfilePhoto from "../../../assets/images/DiscordDefault.png"




const Users = () => {

    interface Roles{
        name: string;
        red: number
        green: number
        blue: number
    }

    interface UserType {
        banned: boolean
        joined_at :string
        mutes: number
        userApiData: {
            id: number
            username: string
            avatar: string;
            discriminator: string
            public_flags: number
            banner: string
            banner_color: string
            accent_color: string
        }
        userId: string
        nickName: string
        userRoles:Roles[]
        warnings: number
    }

    const [user, setUser] = useState<UserType>();

    const id = window.location.href.split('/')[4]

    useEffect(() => {
        fetchData(id);
        const interval = setInterval(() => {
            fetchData(id)
        }, 2500);
        return () => clearInterval(interval);
    }, [])

    const fetchData = (id: string) => {
        axios({
            method: 'get',
            url: `http://localhost:8080/cds/rdss/get-member-data/${id}`,
            headers: {
                'Content-Type': 'application/json',
            }
        })
            .then(response => {

                setUser(response.data)

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
                        {user.userApiData.avatar && user.userApiData.avatar !== "Unknown User" ?
                        <img
                            className={'responsiveImage'}
                            alt={"Discord Profile"}
                            src={`https://cdn.discordapp.com/avatars/${user.userId}/${user.userApiData.avatar}.${user.userApiData.avatar.startsWith("a_") ?'gif':'png'}?size=512`}
                        />
                        :
                            <img
                                className={'responsiveImage'}
                                alt={"Discord Profile"}
                                src={DiscordDefaultProfilePhoto}
                            />
                        }
                        <h2 onClick={() => CopyUsername(user.userId)}>{user.userApiData.username}#{user.userApiData.discriminator}</h2>
                        <h2>{user.nickName ? user.nickName : "Unverified User"}</h2>
                        <p>Warnings: {user.warnings}</p>
                        <p>Mutes: {user.mutes}</p>
                        <p>Status: Muted</p>
                    </div>

                    <div className={'UserServerDetails UserBox'}>
                        <h1>Server Details</h1>
                        <p>Joined at: {user.joined_at}</p>
                        <p>roles:</p>
                        <div className={"memberRoles"}>
                        {
                            user.userRoles.map((role, i)=> {

                                return(<p style={{color: `rgb(${role.red}, ${role.green}, ${role.blue})`}} className={"memberRole"}>{role.name}</p>)
                            })
                        }
                        </div>
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
