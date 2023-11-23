import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/Welcome.css';

export default function Welcome() {
        return (
                <div className="welcome-container">
                        <h1>Welcome to Our Website!</h1>
                        <p>
                                If you're new, please register below. If you already have an account,
                                please log in.
                        </p>
                        <div className="welcome-links">
                                <Link className='register-link' to="/register">
                                        Register
                                </Link>
                                <p>Already have an account?</p>
                                <Link className='login-link' to="/login">
                                        Login here
                                </Link>
                        </div>
                </div>
        );
};
