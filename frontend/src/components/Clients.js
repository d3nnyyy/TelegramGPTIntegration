import axios from 'axios'
import React, { useEffect, useState } from 'react'
import ClientProfile from './ClientProfile'

export default function Clients() {

        const [clients, setClients] = useState([])

        useEffect(() => {
                loadClients()
        }, [])

        const loadClients = async () => {
                try {

                        const token = localStorage.getItem('token');
                        const result = await axios.get('http://localhost:8080/api/clients', {
                                headers: {
                                        Authorization: `Bearer ${token}`,
                                },
                        });

                        setClients(result.data);
                } catch (error) {
                        console.error('Error loading clients:', error);
                }
        };

        return (
                <div className='clients-container'>
                        {clients.map(client => (
                                <ClientProfile key={client.id} client={client} />
                        ))}
                </div>
        )
}
