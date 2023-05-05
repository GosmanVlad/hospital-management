<template>
    <v-form v-model="valid">
        <v-container>
            <v-row>
                <v-col cols="12" md="4">
                    <v-text-field v-model="form.username" label="Username" required></v-text-field>
                </v-col>

                <v-col cols="12" md="4">
                    <v-text-field v-model="form.password" label="password" required type="password"></v-text-field>
                </v-col>

                <v-col cols="12" md="4">
                    <v-select label="Select" v-model="form.role" :items="['CEZV-EE', 'CEZV-GN', 'DEO', 'ADMIN']"></v-select>
                </v-col>
            </v-row>

            <v-card-actions class="justify-center">
                <v-btn>
                    <v-btn @click.prevent="register" rounded="lg" color="primary">Register</v-btn>
                </v-btn>
            </v-card-actions>
        </v-container>
    </v-form>
</template>
<script>
import { mapActions } from "vuex";
export default {
    name: 'register-page',
    data: () => ({
        valid: false,
        form: {
            username: "",
            password: "",
            role: "CEZV-EE"
        },
    }),
    methods: {
        ...mapActions(["Register"]),
        async register() {
            try {
                await this.Register(this.form);
                this.$router.push("/");
            } catch (error) {
                console.log(error);
            }
        },
    }
}
</script>
