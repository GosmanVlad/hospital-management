<template>
    <v-app-bar color="grey-lighten-2" name="app-bar" class="justify-center">
        <span class="d-flex align-center mt-5">
            <p class="header-title ml-4">Hospital Manager</p>
        </span>
        <v-spacer></v-spacer>
        <div id="containerTransition" class="text-left profileContainer" :class="{ active: isActive }">
            Autentificare
        </div>
    </v-app-bar>
</template>

<script>
export default {
    name: 'NavBar',
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