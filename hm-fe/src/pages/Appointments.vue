<template>
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
