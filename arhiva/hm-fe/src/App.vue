<template>
  <v-layout ref="app">
    <Navbar />
    <Sidebar />

    <v-main class="content">
      <div class="page-content">
        <router-view />
      </div>

      <v-snackbar v-model="snackbar.show" :color="snackbar.color">
        <v-icon v-if="snackbar.status === 'warning'" style="margin-right:10px">mdi-alert</v-icon>
        <v-icon v-if="snackbar.status === 'error'" style="margin-right:10px">mdi-alert-circle-outline</v-icon>
        <template v-slot:actions>

          <v-btn color="black" variant="text" @click="snackbar.show = false">
            <v-icon>mdi-close-thick</v-icon>
          </v-btn>
        </template>
        {{ snackbar.message }}
      </v-snackbar>
    </v-main>
  </v-layout>
</template>

<style scoped>
@import './assets/css/main.css';
</style>

<script>
import Navbar from "./components/Navbar.vue";
import Sidebar from "./components/Sidebar.vue";
import { snackbarColors } from "@/consts/colors";
import axios from "axios";

export default {
  name: 'App',
  components: { Navbar, Sidebar },
  data: () => ({
    layout: null,
    snackbar: {
      status: "",
      message: "",
      show: false,
      color: snackbarColors.error
    },
  }),
  mounted() {
    axios.interceptors.response.use(
      (response) => {
        return response;
      },
      (error) => {
        console.log(error);
        if (error.response?.status === 401) {
          this.logout();
          this.snackbar = {
            show: true,
            status: "error",
            color: snackbarColors.error,
            message: "Sesiunea a expirat, va rugam sa va autentificati din nou",
          }
        }
        return Promise.reject(error);
      }
    );
  },
  methods: {
    async logout() {
      await this.$store.dispatch("LogOut");
      this.$router.push("/login");
    },
    print(key) {
      alert(JSON.stringify(this.$refs.app.getLayoutItem(key), null, 2))
    },
  },
}
</script>
