import React, {useEffect, useState } from 'react'
import './infractions.css';
import axios from "axios";
import {Link} from "react-router-dom";

const Infractions = () => {

    interface InfractionsType {
        id: number
        created: string
        type: string
        userId: string
        moderatorId: string
        userData: {
            id: number
            username: string
            avatar: string
            discriminator: string
            public_flags: number
        }
        moderatorData: {
            id: number
            username: string
            avatar: string
            discriminator: string
            public_flags: number
        }
    }

    const [infractions, setInfractions] = useState<InfractionsType[]>([]);

    useEffect(() => {
        fetchData()
    }, [])

    const fetchData = () => {
        console.log('test')
            axios({
                method: 'get',
                url: ``,
                headers: {
                    'Content-Type': 'application/json',
                }
            })
                .then(response => {
                   setInfractions(response.data)
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

                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Created</th>
                        <th>Type</th>
                        <th>User</th>
                        <th>Moderator</th>
                        <th>Active</th>
                        <th>Reason</th>
                    </tr>
                    </thead>
                    <tbody>
                    {infractions.length > 0 ?
                        infractions.map((item ,i) => {
                                console.log(infractions)
                                return (
                                    <tr>
                                        <td>{item.id}</td>
                                        <td>{item.created}</td>
                                        <td>{item.type}</td>

                                        <td>
                                            <Link to={`/users/${item.userId}`}>
                                                Username#0000
                                            </Link>

                                        </td>
                                        <td>

                                        </td>
                                        <td>

                                        </td>
                                        <td>

                                        </td>


                                    </tr>
                                )
                            }

                        )
                        :
                        null }
                    </tbody>
                </table>
        </>
    );
};

export default Infractions
