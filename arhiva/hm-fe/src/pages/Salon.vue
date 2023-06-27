<template>
    <h3 class="page-title" v-if="role === 'DOCTOR'">Saloane</h3>
    <SalonComponent />
</template>
<script>
import SalonComponent from '@/components/Salons/SalonComponent';
export default {
    name: "SalonPage",
    components: { SalonComponent },
}
</script>
