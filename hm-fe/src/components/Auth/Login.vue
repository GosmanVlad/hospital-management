<template>
    <v-form v-model="valid">
        <v-container fill-height fluid>
            <v-row justify="center">
                <v-col cols="12" md="4">
                    <v-text-field v-model="form.email" label="Email" required></v-text-field>
                </v-col>

                <v-col cols="12" md="4">
                    <v-text-field v-model="form.password" label="password" required type="password"></v-text-field>
                </v-col>
            </v-row>

            <v-card-actions class="justify-center">
                <v-btn>
                    <v-btn @click.prevent="login" rounded="lg" color="primary">Login</v-btn>
                </v-btn>
                <v-btn>
                    <v-btn @click="join" rounded="lg" color="primary" hidden>Join</v-btn>
                </v-btn>
            </v-card-actions>
        </v-container>
    </v-form>
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
                    this.$router.push("/");
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
