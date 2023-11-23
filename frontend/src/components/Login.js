import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { saveTokens } from './authService';
import axios from 'axios';
import '../styles/Login.css';

export default function Login() {
        const navigate = useNavigate();
        const [formData, setFormData] = useState({
                email: '',
                password: '',
        });

        const [errors, setErrors] = useState({});
        const [isFormValid, setIsFormValid] = useState(false);

        const handleChange = (e) => {
                const { name, value } = e.target;
                setFormData({
                        ...formData,
                        [name]: value,
                });
                validateForm();
        };

        const handleSubmit = async (e) => {
                e.preventDefault();

                try {
                        if (isFormValid) {
                                const response = await axios.post('http://localhost:8080/api/auth/login', formData);

                                saveTokens(response);

                                navigate('/');
                        } else {
                                console.log('Form is invalid!');
                        }
                } catch (error) {
                        console.error('Login failed:', error);
                }
        };

        const validateForm = () => {
                const newErrors = {};

                if (!formData.email.trim()) {
                        newErrors.email = 'Email is required';
                } else if (!/^\S+@\S+\.\S+$/.test(formData.email)) {
                        newErrors.email = 'Invalid email format';
                }

                if (!formData.password) {
                        newErrors.password = 'Password is required';
                } else if (formData.password.length < 6) {
                        newErrors.password = 'Password must be at least 6 characters long';
                }

                setErrors(newErrors);

                setIsFormValid(Object.keys(newErrors).length === 0);
        };

        return (
                <div className="login-container">
                        <h2>Login</h2>
                        <form onSubmit={handleSubmit} className="login-form">
                                <input
                                        type="email"
                                        id="email"
                                        name="email"
                                        value={formData.email}
                                        onChange={handleChange}
                                        placeholder="Email"
                                />
                                {errors.email && <p className="error">{errors.email}</p>}
                                <input
                                        type="password"
                                        id="password"
                                        name="password"
                                        value={formData.password}
                                        onChange={handleChange}
                                        placeholder="Password"
                                />
                                {errors.password && <p className="error">{errors.password}</p>}
                                <button type="submit" className="login-button" disabled={!isFormValid}>
                                        Login
                                </button>
                        </form>
                        <p>
                                Don't have an account? <Link to="/register">Register</Link>
                        </p>
                </div>
        );
}