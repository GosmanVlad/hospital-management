<template>
    <v-form v-model="valid">
        <v-container fill-height fluid>
            <v-row justify="center">
                Contul nu este activat. <br />
                Seteaza o parola pentru a-l activa.
                <v-col cols="12" md="4">
                    <v-text-field v-model="form.password" label="Parola" required type="password"></v-text-field>
                </v-col>

                <v-col cols="12" md="4">
                    <v-text-field v-model="form.repeatPassword" label="Repeta parola" required
                        type="password"></v-text-field>
                </v-col>
            </v-row>

            <v-card-actions class="justify-center">
                <v-btn>
                    <v-btn @click.prevent="activateAccount" rounded="lg" color="primary">Activeaza cont</v-btn>
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
