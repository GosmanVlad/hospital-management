<template>
  <v-layout ref="app">
    <Navbar />
    <Sidebar />

    <v-main class="content">
      <router-view />
    </v-main>
  </v-layout>
</template>

<style scoped>
@import './assets/css/main.css';
</style>

<script>
import Navbar from "./components/Navbar.vue";
import Sidebar from "./components/Sidebar.vue";

export default {
  name: 'App',
  components: { Navbar, Sidebar },
  data: () => ({
    layout: null,
  }),
  methods: {
    print(key) {
      alert(JSON.stringify(this.$refs.app.getLayoutItem(key), null, 2))
    },
  },
}
</script>
