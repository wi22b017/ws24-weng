import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import { createPinia } from 'pinia';
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate';

// Bootstrap styles and scripts
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import 'bootstrap-icons/font/bootstrap-icons.css';

// Font Awesome setup
import { library } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faPlaneDeparture, faPlaneArrival, faClock, faPlane } from '@fortawesome/free-solid-svg-icons';

// Add Font Awesome icons to the library
library.add(faPlaneDeparture, faPlaneArrival, faClock, faPlane);

const pinia = createPinia();
pinia.use(piniaPluginPersistedstate);

createApp(App)
    .use(pinia)
    .use(router)
    .component('font-awesome-icon', FontAwesomeIcon) // Register the FontAwesomeIcon component globally
    .mount('#app');
