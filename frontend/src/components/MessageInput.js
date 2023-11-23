import React from 'react';
import { RiSendPlane2Line } from 'react-icons/ri';

const MessageInput = ({ value, onChange, onKeyPress, onClick }) => {
        return (
                <div className="message-input-container">
                        <input
                                type="text"
                                placeholder="Type your message..."
                                value={value}
                                onChange={onChange}
                                onKeyPress={onKeyPress}
                        />
                        <RiSendPlane2Line className="send-icon" onClick={onClick} />
                </div>
        );
};

export default MessageInput;