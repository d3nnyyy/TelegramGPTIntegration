import React from 'react'
import Welcome from './Welcome'
import Clients from './Clients';

export default function Home() {

        const isLoggedIn = localStorage.getItem('token');

        return (
                <div>
                        {!isLoggedIn ?
                                <Welcome /> :
                                <Clients />
                        }
                </div>
        )
}
