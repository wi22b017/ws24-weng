import {defineStore} from 'pinia'
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
          try {
            const response = await apiClient.post('auth/token', {
                  usernameOrEmail: usernameOrEmail,
                  password: password
                }
            );

            if (response.data && response.data.token) {
                localStorage.setItem('access_token', response.data.token);
                const userId = this.getUserIdFromToken(response.data.token);
                await this.fetchUserData(userId);
                //await updateNavbar(); // Ensure navbar updates correctly after fetching user.role
            }

              return {
                success: true,
                message: 'Login Successful'
            };
          } catch (error) {
              return {
                  success: false,
                  message: error.response.data.message
              };
          }
        },
        async fetchUserData(userId) {
            try {
                const response = await apiClient.get(`/users/${userId}`);
                this.id = response.data.id;
                this.gender = response.data.gender;
                this.firstName = response.data.firstName;
                this.lastName = response.data.lastName;
                this.username = response.data.username;
                this.email= response.data.email;
                this.role= response.data.role;
                this.status= response.data.status;
                this.street= response.data.address.street;
                this.number= response.data.address.number;
                this.zip= response.data.address.zip;
                this.city= response.data.address.city;
                this.paymentMethodName= response.data.paymentMethod.name;
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