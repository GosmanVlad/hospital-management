<template>
    <v-app-bar color="grey-lighten-2" name="app-bar" class="justify-center">
        <span class="d-flex align-center mt-5">
            <p class="header-title ml-4">Hospital Manager</p>
        </span>
        <v-spacer></v-spacer>
        <div id="containerTransition" class="text-left profileContainer">
            <router-link :to="'/login'" class="link-style" v-if="!isLoggedIn">
                <v-btn flat color="info" @click.prevent="logout">
                    Autentificare
                </v-btn>
            </router-link>
            <router-link :to="'/login'" class="link-style" v-if="isLoggedIn" style="padding-top:4px">
                <v-btn flat color="info" @click.prevent="logout">
                    Log Out
                </v-btn>
            </router-link>
        </div>
    </v-app-bar>
</template>

<script>
export default {
    name: 'NavBar',
    methods: {
        async logout() {
            await this.$store.dispatch("LogOut");
            this.$router.push("/login");
        },
    },
    computed: {
        isLoggedIn: function () {
            return this.$store.getters.isAuthenticated;
        },
        role: function () {
            return this.$store.getters.StateRole;
        },
    },
}
</script>

<style scoped>
.mt-5 {
    margin-top: 0px !important;
}

.profileContainer {
    width: 140px;
    max-height: 48px;
    overflow: hidden;
    text-align: center;
    font-size: 17px;
    transition: 0ms;
    color: white;
}

.profileContainer.active {
    max-height: 80px;
    overflow: auto;
}
</style>