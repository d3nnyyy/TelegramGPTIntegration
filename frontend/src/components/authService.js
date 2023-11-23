// authService.js
import axios from 'axios';

const refreshToken = async () => {
        try {
                const refreshToken = localStorage.getItem('refreshToken');
                const response = await axios.post('http://localhost:8080/api/auth/refresh', {
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

export { refreshToken, saveTokens };
