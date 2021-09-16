import axios from "axios";
import React, {useEffect, useState} from "react";
import { Form } from 'react-bootstrap';

const PropertiesSection = () => {

    interface GuildChannel{
        id: string
        name: string
    }

    interface Properties{
        guildChannels:GuildChannel[]
        properties:{
            alertModsCooldown: string
            channelBanRequestsQueueId: string
            channelCensoredAndSpamLogsId: string
            guildRobloxDiscordId: string
            telegramChatId: string
        }
    }

    const [properties, setProperties] = useState<Properties>();

    useEffect(() => {
        fetchData()
        const interval = setInterval(() => {
            fetchData()
        }, 2500);
        return () => clearInterval(interval);
    }, [])

    const fetchData = () => {
        axios({
            method: 'get',
            url: `http://localhost:8080/cds/rdss/getServerConfigurations`,
            headers: {
                'Content-Type': 'application/json',
            }
        })
            .then(response => {
                console.log("Loading")

                    setProperties(response.data)

                }
            )
    }

    return (
        <>
            {properties ?
                <Form>
                   <h1>
                       Ban requests
                   </h1>
                    <Form.Control as="select" className={"form-select"} onChange={e => {
                    }}>
                        {properties.guildChannels.filter(name => name.id.includes(properties.properties.channelBanRequestsQueueId)).map(id => (
                            <option selected value={properties.properties.channelBanRequestsQueueId}>{id.name}</option>
                        ))}

                        {properties ? properties.guildChannels.map(channel => <option value={channel.id}>{channel.name}</option>) : <p>ok</p>}
                    </Form.Control>
                </Form>
                : <p> sad </p>
            }



        </>
    );
};

export default PropertiesSection;
