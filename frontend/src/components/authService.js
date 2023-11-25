// authService.js
import axios from 'axios';

const LOCAL_URL = () => {
        return 'http://localhost:8080/api';
};
const AZURE_URL = () => {
        return 'https://telegram-gpt-integration-by-d3n41kk.azurewebsites.net/api';
};

const refreshToken = async () => {
        try {
                const refreshToken = localStorage.getItem('refreshToken');
                const response = await axios.post(`${AZURE_URL}/refresh`, {
                        refreshToken: refreshToken,
                });

                const newAccessToken = response.data.accessToken;

                localStorage.setItem('token', newAccessToken);

                return newAccessToken;
        } catch (error) {
                console.error('Error refreshing token:', error);
        }
};

const saveTokens = (response) => {

        const accessToken = response.data.accessToken;
        const refreshToken = response.data.refreshToken;

        console.log('accessToken:', accessToken);
        console.log('refreshToken:', refreshToken);

        localStorage.setItem('token', accessToken);
        localStorage.setItem('refreshToken', refreshToken);
}

export { refreshToken, saveTokens, LOCAL_URL, AZURE_URL };
