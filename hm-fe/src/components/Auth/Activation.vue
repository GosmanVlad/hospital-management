<template>
    <div class="d-flex align-center justify-center" style="height: 80vh">
        <div class="login-form">
            <h1 class="d-flex align-center justify-center auth-title">Activare cont</h1> <br />
            <p class="description">Pentru a activa contul, este necesara setarea unei noi parole</p>
            <v-divider /><br />
            <v-sheet width="400" class="mx-auto">
                <v-text-field variant="underlined" type="password" v-model="form.password"
                    label="Parola noua"></v-text-field>
                <v-text-field variant="underlined" type="password" v-model="form.repeatPassword"
                    label="Repetare parola noua"></v-text-field>
                <div class="auth-footer-section">
                    <v-btn type="submit" variant="outlined" color="error" block class="mt-2"
                        @click.prevent="activateAccount">Activeaza cont</v-btn>
                </div>
            </v-sheet>
        </div>
    </div>
    <v-snackbar v-model="snackbar.show" :color="snackbar.color">
        <v-icon v-if="snackbar.status === 'warning'" style="margin-right:10px">mdi-alert</v-icon>
        <v-icon v-if="snackbar.status === 'error'" style="margin-right:10px">mdi-alert-circle-outline</v-icon>
        <template v-slot:actions>

            <v-btn color="black" variant="text" @click="snackbar.show = false">
                <v-icon>mdi-close-thick</v-icon>
            </v-btn>
        </template>
        {{ snackbar.message }}
    </v-snackbar>
</template>
<script>
import { authService } from "@/api";
import { snackbarColors } from "@/consts/colors";
export default {
    name: 'activation-page',
    data: () => ({
        valid: false,
        form: {
            repeatPassword: "",
            password: "",
        },
        snackbar: {
            status: "",
            message: "",
            show: false,
            color: snackbarColors.error
        },
        response: undefined,
    }),
    methods: {
        async activateAccount() {
            authService.changePassword(this.$store.getters.StateUserId, this.form).then((res) => {
                if (res.data.message === 'reset_password_success') {
                    this.snackbar = {
                        show: true,
                        status: "success",
                        color: snackbarColors.success,
                        message: "Cont activat cu succes!",
                    }

                    this.$router.push("/");
                }
                else if (res.data.message === 'password_does_not_match') {
                    this.snackbar = {
                        show: true,
                        status: "error",
                        color: snackbarColors.error,
                        message: "Parola nu se potriveste",
                    }
                }
            })
        },
    },
}
</script>
<style scoped>
.auth-footer-section {
    padding-top: 50px;
    padding-bottom: 30px;
}

.description {
    padding-right: 5px;
    padding-left: 5px;
}
</style>