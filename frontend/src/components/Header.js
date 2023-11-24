import React from 'react'
import { Link } from 'react-router-dom'
import '../styles/Header.css'
import { useNavigate } from 'react-router-dom';

export default function Header({ }) {

        const navigate = useNavigate();
        const isLoggedIn = !!localStorage.getItem('token');

        const handleLogout = () => {
                localStorage.removeItem('token');
                localStorage.removeItem('user');
                navigate('/');
        };


        return (
                <header>
                        <nav>
                                <div className='nav-left'>
                                        <Link to={'/'} className='current'>Home</Link>
                                </div>
                                <div className='nav-right'>
                                        {isLoggedIn ? (
                                                <>
                                                        <a onClick={handleLogout}>Log out</a>
                                                </>
                                        ) : (
                                                <>
                                                        <Link to='/register'>Register</Link>
                                                        <Link to='/login'>Log in</Link>
                                                </>
                                        )}
                                </div>
                        </nav>
                </header>

        )
}