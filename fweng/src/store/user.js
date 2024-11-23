import {defineStore} from 'pinia'
//import axios from "axios";
import apiClient from '../utils/axiosClient';

export const useUserStore = defineStore('user', {
    state: () => ({
        id: 1,
        gender: 'male',
        firstName: 'John',
        lastName: 'Doe',
        username: 'Johnboy',
        email: 'john.doe@email.com',
        role: 'user',
        status: 'active',
        street: 'Main Street',
        number: 8,
        zip: 1050,
        city: 'Vienna',
        paymentMethodName: 'Credit Card'
    }),
    getters: {
        fullName() {
            return `${this.firstName} ${this.lastName}`
        }
    },
    actions: {
        async login(usernameOrEmail, password) {
            // call an API
            /*const res = await fetch('https://jsonplaceholder.typicode.com/users/1');
            const user = await res.json();
            this.id = user.id;
            */

          try {
            const response = await apiClient.post('auth/token', {
                  usernameOrEmail: usernameOrEmail,
                  password: password
                }
            );

            // Check if the response contains userId and username
            if (response.data && response.data.token) {

                localStorage.setItem('access_token', response.data.token);

                const userId = this.getUserIdFromToken(response.data.token);

                await this.fetchUserData(userId);

              //await checkLoginStatus(); // Ensure navbar updates after login

            }
              return {
                success: true,
                message: 'Login Successful'
            };
          } catch (error) {
              //return error.response.data.message;
              return {
                  success: false,
                  message: 'Login failed'
              };
          }
        },
        async fetchUserData(userId) {
            try {
                const response = await apiClient.get(`/users/${userId}`);
                console.log(response);
                this.id = response.data.id;
                this.firstName = response.data.firstName;
                this.lastName = response.data.lastName;
                this.username = response.data.username;
            } catch (error) {
                return error.response.data.message;
            }
        },
        getUserIdFromToken(token) {
            try {
                // Split the token to get the payload (second part)
                const base64Url = token.split('.')[1]; // JWT structure: header.payload.signature
                const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/'); // Convert Base64URL to Base64
                const payload = JSON.parse(atob(base64)); // Decode and parse the payload as JSON
                return payload.sub; // Extract the `sub` field
            } catch (error) {
                console.error('Invalid token', error);
            }
        }
    },
})