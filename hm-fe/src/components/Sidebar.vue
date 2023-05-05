<template>
    <v-navigation-drawer color="grey-darken-2" permanent name="drawer">
        <v-list class="main-nav-container">
            <div>
                <div>
                    <v-list-item v-bind="props"><router-link :to="'/'" class="router-link-path">
                            Menu 1
                        </router-link></v-list-item>
                    <v-list-item v-bind="props"><router-link :to="'/'" class="router-link-path">
                            Menu 2
                        </router-link></v-list-item>
                    <v-list-item v-bind="props"><router-link :to="'/'" class="router-link-path">
                            Menu 3
                        </router-link></v-list-item>
                </div>
            </div>
        </v-list>
    </v-navigation-drawer>
</template>
<script>
export default {
    name: 'SideBar',
    date() {
        return {
            props: "",
        }
    }
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