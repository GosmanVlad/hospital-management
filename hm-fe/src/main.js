import { createApp } from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import router from './router';
import store from './store';
import { loadFonts } from './plugins/webfontloader'
import axios from 'axios';

import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css'
import VueGoogleCharts from "vue3-googl-chart";

loadFonts()

axios.defaults.withCredentials = false;
axios.defaults.baseURL = process.env.VUE_APP_API_URL;
const token = store.getters.StateToken;
if (token) {
  axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
}

createApp(App)
  .use(vuetify)
  .use(router)
  .use(store)
  .use(VueGoogleCharts)
  .component('Datepicker', VueDatePicker)
  .mount('#app')
