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

        return (
                <div className='user-info'>
                        <Link
                                to={`/users/${client.id}`}
                                className='user-link'>
                                <div className='user-info-img'>
                                        <img className='user-img' src={imgUrl} alt={client.username} />
                                </div>
                                <div className='user-info-text'>
                                        <h2 className='user-info-fullname'>{getFullName()}</h2>
                                        <p className='user-info-username'>{client.username}</p>
                                </div>
                        </Link>
                </div>
        );
}
