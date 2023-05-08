<template>
    <h3 class="page-title">Internari</h3>
    <HospitalizationComponent />
</template>

<script>
import HospitalizationComponent from "@/components/Hospitalization/HospitalizationComponent.vue";

export default {
    name: "HospitalizationPage",
    components: { HospitalizationComponent },
}
</script>
<style scoped></style>
