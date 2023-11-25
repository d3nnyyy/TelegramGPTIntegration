// ClientProfile.js
import React from 'react';
import { Link } from 'react-router-dom';
import profilePlaceholder from '../imgs/profile-placeholder.jpg';
import '../styles/ClientProfile.css';

export default function ClientProfile({ client }) {
        const getFullName = () => {
                if (client.firstName && client.lastName)
                        return `${client.firstName} ${client.lastName}`;
                else if (client.firstName) return client.firstName;
                else if (client.lastName) return client.lastName;
        };

        const imgUrl = client.imgUrl ? client.imgUrl : profilePlaceholder;
        console.log('imgUrl:', imgUrl)

        return (
                <Link
                        to={`/clients/${client.id}`}
                        className='client'>
                        <img className='client-img' src={imgUrl} alt={client.username} />
                        <div className='client-info'>
                                <h2 className='client-fullname'>{getFullName()}</h2>
                                <p className='client-username'>{client.username}</p>
                        </div>
                </Link>
        );
}
