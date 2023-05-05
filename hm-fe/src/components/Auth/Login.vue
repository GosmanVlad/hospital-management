<template>
    <div class="d-flex align-center justify-center" style="height: 80vh">
        <div class="login-form">
            <h1 class="d-flex align-center justify-center auth-title">Autentificare platforma</h1> <br />
            <v-divider /><br />
            <v-sheet width="400" class="mx-auto">
                <v-text-field variant="underlined" v-model="form.email" label="Email"></v-text-field>
                <v-text-field variant="underlined" v-model="form.password" label="Parola"></v-text-field>
                <div class="auth-footer-section">
                    <a href="#" class="text-body-2 font-weight-regular" style="color: #9d152d">Forgot Password?</a>

                    <v-btn type="submit" variant="outlined" color="error" block class="mt-2"
                        @click.prevent="login">Autentificare</v-btn>
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
import { mapActions } from "vuex";
import { snackbarColors } from "@/consts/colors";
export default {
    name: 'login-page',
    data: () => ({
        valid: false,
        form: {
            email: "",
            password: "",
        },
        snackbar: {
            status: "",
            message: "",
            show: false,
            color: snackbarColors.error
        },
        response: undefined
    }),
    methods: {
        ...mapActions(["LogIn"]),
        async login() {
            try {
                await this.LogIn(this.form).then((res) => {
                    this.snackbar = {
                        show: true,
                        status: "success",
                        color: snackbarColors.success,
                        message: "Te-ai autentificat cu succes!",
                    }
                    this.response = res;
                    localStorage.setItem("role", res.data.role);

                    if (!res.data.activated) {
                        this.$router.push("/activation");
                    } else {
                        this.$router.push("/");
                    }
                }).catch(() => {
                    this.snackbar = {
                        show: true,
                        status: "error",
                        color: snackbarColors.error,
                        message: "Username-ul sau parola introdusa nu se gasesc in baza de date!",
                    }
                });

            } catch (error) {
                console.log(error);
            }
        },
        join() {
            this.$router.push("/join");
        }
    }
}
</script>
<style scoped>
.auth-footer-section {
    padding-top: 50px;
    padding-bottom: 30px;
}
</style>