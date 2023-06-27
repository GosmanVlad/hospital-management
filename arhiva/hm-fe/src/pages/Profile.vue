<template>
    <h3 class="page-title" v-if="role === 'DOCTOR'">Saloane</h3>
    <ProfileComponent :id=id />
</template>
<script>
import ProfileComponent from '@/components/Profile/ProfileComponent';
export default {
    name: "ProfilePage",
    components: { ProfileComponent },
    data() {
        return {
            id: this.$route.params.id,
        }
    },
    mounted() {
        if (parseInt(this.$route.params.id) !== parseInt(this.$store.getters.StateUserId) && this.$store.getters.StateRole === 'PATIENT') {
            this.$router.push("/");
        }
    }
}
</script>
