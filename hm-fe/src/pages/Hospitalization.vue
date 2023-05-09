<template>
    <h3 class="page-title">Internari</h3>
    <HospitalizationComponent v-if="role === 'DOCTOR' || role === 'NURSE'" />
    <HospitalizationPatientComponent v-if="role === 'PATIENT'" />
</template>

<script>
import HospitalizationComponent from "@/components/Hospitalization/HospitalizationComponent.vue";
import HospitalizationPatientComponent from "@/components/Hospitalization/HospitalizationPatientComponent.vue";

export default {
    name: "HospitalizationPage",
    components: { HospitalizationComponent, HospitalizationPatientComponent },
    computed: {
        role: function () {
            return this.$store.getters.StateRole;
        },
    },
}
</script>
<style scoped></style>
