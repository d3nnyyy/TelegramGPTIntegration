import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../styles/Register.css';

export default function Register() {
        const navigate = useNavigate();
        const [errors, setErrors] = useState({});

        const [formData, setFormData] = useState({
                firstName: '',
                lastName: '',
                email: '',
                password: '',
        });

        const [isFormValid, setIsFormValid] = useState(false);

        useEffect(() => {
                validateForm();
        }, [formData]);

        const handleChange = (e) => {
                const { name, value } = e.target;
                setFormData({
                        ...formData,
                        [name]: value,
                });
        };

        const handleSubmit = async (e) => {
                e.preventDefault();

                try {
                        if (isFormValid) {
                                const response = await axios.post('http://localhost:8080/api/auth/register', formData);

                                const accessToken = response.data.accessToken;
                                const refreshToken = response.data.refreshToken;

                                localStorage.setItem('token', accessToken);
                                localStorage.setItem('refreshToken', refreshToken);

                                navigate('/');
                        } else {
                                console.log('Form is invalid!');
                        }
                } catch (error) {
                        console.error('Registration failed:', error);
                }
        };

        const validateForm = () => {
                const newErrors = {};

                if (!formData.firstName.trim()) {
                        newErrors.firstName = 'First Name is required';
                }

                if (!formData.lastName.trim()) {
                        newErrors.lastName = 'Last Name is required';
                }

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
                <div className="register-container">
                        <h2>Register</h2>
                        <form onSubmit={handleSubmit} className="register-form">
                                <input
                                        type="text"
                                        id="firstName"
                                        name="firstName"
                                        value={formData.firstName}
                                        onChange={handleChange}
                                        placeholder="First Name"
                                />
                                {errors.firstName && <p className="error">{errors.firstName}</p>}
                                <input
                                        type="text"
                                        id="lastName"
                                        name="lastName"
                                        value={formData.lastName}
                                        onChange={handleChange}
                                        placeholder="Last Name"
                                />
                                {errors.lastName && <p className="error">{errors.lastName}</p>}
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
                                <button type="submit" className="register-button" disabled={!isFormValid}>
                                        Register
                                </button>
                        </form>
                </div>
        );
}