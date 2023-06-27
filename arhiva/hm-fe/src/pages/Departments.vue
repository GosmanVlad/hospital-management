<template>
    <h3 class="page-title" v-if="role === 'DOCTOR'">Departamente</h3>
    <DepartmentComponent />
</template>

<script>
import DepartmentComponent from '@/components/Departments/DepartmentComponent.vue';

export default {
    name: "AppointmentsPage",
    components: { DepartmentComponent },
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
<style scoped></style>
