import React from 'react'
import gptImg from '../imgs/gpt.webp';
import '../styles/ChatMessage.css';

export default function ChatMessage({ log, clientImg }) {

        const timestamp = new Date(log.timestamp).toLocaleString();
        const clientSentMessage = log.message !== null;
        const gptSentMessage = log.response !== null;

        return (
                <li className={"chat-message"}>
                        <div className={"message-container"}>
                                <div className="message-content">
                                        {clientSentMessage && <div className="message">
                                                <img className="message-img" src={clientImg} alt="User" />
                                                <p className='message-text'>{log.message}</p>
                                        </div>}
                                        {gptSentMessage && <div className="response">
                                                <img className="message-img" src={gptImg} alt="GPT" />
                                                <p className='message-text'>{log.response}</p>
                                        </div>}
                                        <p className="timestamp">{timestamp}</p>
                                </div>
                        </div>
                </li>
        );
}
