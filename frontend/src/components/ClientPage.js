// ClientPage.js
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';
import '../styles/ClientPage.css';
import profilePlaceholder from '../imgs/profile-placeholder.jpg';
import ChatMessage from './ChatMessage';
import MessageInput from './MessageInput';
import { AZURE_URL } from './authService';


export default function ClientPage() {

        const [client, setClient] = useState({});
        const { clientId } = useParams();
        const [chatLogs, setChatLogs] = useState([]);
        const [newMessage, setNewMessage] = useState('');

        const getFullName = () => {
                if (client.firstName && client.lastName)
                        return `${client.firstName} ${client.lastName}`;
                else if (client.firstName) return client.firstName;
                else if (client.lastName) return client.lastName;
        };

        const imgUrl = client.imgUrl ? client.imgUrl : profilePlaceholder;

        const fetchChatLogs = async () => {
                try {
                        const token = localStorage.getItem('token');
                        const chatLogsResult = await axios.get(`${AZURE_URL()}/chatlogs/${clientId}`, {
                                headers: {
                                        Authorization: `Bearer ${token}`,
                                },
                        });
                        setChatLogs(chatLogsResult.data.reverse());
                } catch (error) {
                        console.error('Error fetching chat logs:', error);
                }
        };

        const fetchClientData = async () => {
                try {
                        const token = localStorage.getItem('token');
                        const clientResult = await axios.get(`${AZURE_URL()}/clients/${clientId}`, {
                                headers: {
                                        Authorization: `Bearer ${token}`,
                                },
                        });
                        setClient(clientResult.data);
                } catch (error) {
                        console.error('Error fetching client data:', error);
                }
        };

        useEffect(() => {

                fetchClientData();
        }, [clientId]);

        useEffect(() => {
                fetchChatLogs();
        }, [clientId]);

        const handleSendMessage = async () => {
                try {
                        const token = localStorage.getItem('token');
                        await axios.post(
                                `${AZURE_URL()}/clients/send-message`,
                                { clientId: clientId, message: newMessage },
                                {
                                        headers: {
                                                Authorization: `Bearer ${token}`,
                                        },
                                }
                        );

                        fetchChatLogs();
                        setNewMessage('');
                } catch (error) {
                        console.error('Error sending message:', error);
                }
        };

        return (
                <div className="user-page-container">
                        <div className="user-container">
                                <div className='user-img'>
                                        <img className='user-img' src={imgUrl} alt={client.username} />
                                </div>
                                <div className='user-text'>
                                        <h2 className='user-full-name'>{getFullName()}</h2>
                                        <p className='user-username'>{client.username}</p>
                                </div>
                        </div>
                        <div className="chat-logs-container">
                                <h2>Chat Logs</h2>
                                <ul className="chat-logs">
                                        {chatLogs.map((log) => (
                                                <ChatMessage key={log.id} log={log} clientImg={imgUrl} />
                                        ))}
                                </ul>
                                <MessageInput
                                        value={newMessage}
                                        onChange={(e) => setNewMessage(e.target.value)}
                                        onKeyPress={(e) => {
                                                if (e.key === 'Enter') {
                                                        handleSendMessage();
                                                }
                                        }}
                                        onClick={handleSendMessage}
                                />
                        </div>
                </div>
        );
}
