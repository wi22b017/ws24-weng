import {defineStore} from 'pinia'
import apiClient from '../utils/axiosClient';
import router from '@/router';

export const useUserStore = defineStore('user', {
    state: () => ({
        id: 1,
        gender: '',
        firstName: '',
        lastName: '',
        username: '',
        email: '',
        dateOfBirth: '',
        role: '',
        status: '',
        street: '',
        number: 1,
        zip: 1,
        city: '',
        country:'',
        paymentMethodName: '',
        paymentMethodId: '',
        profilePictureUrl: null,
        isLoggedIn: false,
        bookings: []
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
                await this.fetchUserBookings(userId);
            }

              return {
                success: true,
                message: 'Login Successful'
            };
          } catch (error) {
              return {
                  success: false,
                  message: error.response.data.error
              };
          }
        },
        async logout(){
            try {
                // Clear the access token from localStorage
                localStorage.removeItem('access_token');
                localStorage.removeItem('user'); // delete stored userObject from pinia
                localStorage.removeItem('adminUserStore'); // delete stored userObject from pinia
                localStorage.removeItem('flight'); // delete stored flights from pinia

                // Reset all state fields to their default values
                this.$reset();
                await router.push({name: 'home'}); // Redirect to homepage
            } catch (error) {
                console.error('Error during logout:', error);
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
                this.email = response.data.email;
                this.dateOfBirth = response.data.dateOfBirth;
                this.role = response.data.role;
                this.status = response.data.status;
                this.street = response.data.address.street;
                this.number = response.data.address.number;
                this.zip = response.data.address.zip;
                this.city = response.data.address.city;
                this.country = response.data.address.country;
                this.paymentMethodName = response.data.paymentMethod.name;
                this.paymentMethodId = response.data.paymentMethod.id;
                this.profilePictureUrl = response.data.profilePictureUrl;
                this.isLoggedIn = true;
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
        },
        async createBooking(bookingData) {
            try {
                const response = await apiClient.post('/bookings', bookingData);
                const token = localStorage.getItem('access_token');
                const userId = this.getUserIdFromToken(token);
                if (userId) {
                    await this.fetchUserBookings(userId); // Ensure this is called only if userId is valid
                }


                return {
                    success: true,
                    message: 'Booking created successfully',
                    data: response.data,
                };
            } catch (error) {
                console.error('Error creating booking:', error);

                return {
                    success: false,
                    message: error.response?.data?.error || 'An error occurred while creating the booking',
                };
            }
        },
        async fetchUserBookings(userId) {
            try {
                const response = await apiClient.get(`/bookings/user/${userId}`);
                this.bookings = response.data;
                console.log("bookings", response.data)
            } catch (error) {
                return error.response.data.message;
            }
        },
        async cancelBooking(bookingId) {
            try {
                const response = await apiClient.patch(`/bookings/${bookingId}/status`, {status: "Cancelled"});
                const booking = this.bookings.find((b) => b.id === bookingId);
                if(booking) {
                    booking.status = "Cancelled";
                }
                return {
                    success: true,
                    message: 'Booking cancelled successfully',
                    data: response.data,
                };

            } catch (error) {
                console.error('Error cancelling booking:', error);

                return {
                    success: false,
                    message: error.response?.data?.error || 'An error occurred while cancelling the booking',
                };
            }

        }
    },
    persist: {
        enabled: true,
        strategies: [
            {
                key: 'user',
                storage: localStorage,
            },
        ],
    },
})