<template>
    <h3 class="page-title">Facturi</h3>
    <StaffComponent v-if="role === 'DOCTOR' || role === 'NURSE'" />
    <PatientComponent v-if="role === 'PATIENT'" />
</template>

<script>
import StaffComponent from "@/components/Invoices/StaffComponent.vue";
import PatientComponent from "@/components/Invoices/PatientComponent.vue";

export default {
    name: "InvoicesPage",
    components: { StaffComponent, PatientComponent },
    computed: {
        role: function () {
            return this.$store.getters.StateRole;
        },
    },
}
</script>
<style scoped></style>
