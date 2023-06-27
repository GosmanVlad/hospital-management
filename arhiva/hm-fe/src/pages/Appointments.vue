<template>
    <h3 class="page-title" v-if="role === 'DOCTOR'">Istoric programari</h3>
    <h3 class="page-title" v-if="role === 'PATIENT'">Programari active</h3>

    <PatientComponent v-if="role === 'PATIENT'" />
    <StaffComponent v-if="role === 'DOCTOR' || role == 'NURSE'" />
</template>

<script>
import PatientComponent from '@/components/Appointments/PatientComponent.vue';
import StaffComponent from '@/components/Appointments/StaffComponent.vue';

export default {
    name: "AppointmentsPage",
    components: { PatientComponent, StaffComponent },
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
