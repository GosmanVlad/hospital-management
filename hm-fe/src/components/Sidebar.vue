<template>
    <v-navigation-drawer class="navigation-drawer-main mt-5" color="#f5f5f5" permanent width="280" border="none"
        elevation="5" max-height="max-content" :rail="rail" @click="rail = false">
        <v-list-item class="align-left" v-if="isLoggedIn">
            <v-icon v-if="rail">mdi-menu-right</v-icon>
            <template v-slot:append>
                <v-btn v-if="!rail" variant="text" icon="mdi-menu-left" @click.stop="switchRail()"></v-btn>
            </template>
        </v-list-item>
        <v-divider v-if="!rail"></v-divider>
        <v-list v-if="!rail && isLoggedIn" class="main-nav-container">
            <div v-if="!rail">
                <v-list-item><router-link :to="'/'" class="router-link-path">
                        Dashboard
                    </router-link></v-list-item>
                <v-list-item><router-link :to="'/appointments'" class="router-link-path">
                        Programari
                    </router-link></v-list-item>
                <v-list-item><router-link :to="'/'" class="router-link-path">
                        Menu 3
                    </router-link></v-list-item>
            </div>
        </v-list>
    </v-navigation-drawer>
</template>
<script>
export default {
    name: 'SideBar',
    data() {
        return {
            props: "",
            rail: true,
        }
    },
    methods: {
        switchRail() {
            this.rail = !this.rail;
        }
    },
    computed: {
        isLoggedIn: function () {
            return this.$store.getters.isAuthenticated;
        },
    },
}
</script>
<style scoped>
.v-list-item {
    height: 60px;
}

.list-item {
    font-size: 12px;
    color: #000000;
    line-height: 22px;
    align-items: center;
    white-space: pre;
}

.router-link-path {
    text-decoration: none;
    color: #000000;
}

.main-nav-container {
    overflow-x: hidden;
    background-color: #f5f5f5;
    padding: 15px !important;
}
</style>