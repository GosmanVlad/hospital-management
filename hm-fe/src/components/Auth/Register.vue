<template>
    <div class="d-flex align-center justify-center" style="height: 80vh">
        <div class="login-form margin-top-form">
            <h1 class="d-flex align-center justify-center auth-title">Inregistrare in platforma</h1> <br />
            <v-divider /><br />
            <v-sheet width="400" class="mx-auto">
                <v-text-field variant="underlined" v-model="form.firstName" label="Nume"></v-text-field>
                <v-text-field variant="underlined" v-model="form.lastName" label="Prenume"></v-text-field>
                <v-text-field variant="underlined" v-model="form.email" label="Email"></v-text-field>
                <v-text-field variant="underlined" v-model="form.personalNumber"
                    label="Cod Numeric Personal"></v-text-field>
                <v-text-field variant="underlined" v-model="form.phone" label="Numar de telefon"></v-text-field>
                <v-text-field variant="underlined" v-model="form.country" label="Tara"></v-text-field>
                <v-text-field variant="underlined" v-model="form.city" label="Oras"></v-text-field>
                <v-text-field variant="underlined" v-model="form.homeAddress" label="Adresa"></v-text-field>
                <label>Data de nastere</label>
                <Datepicker required autoApply valueType="format" enableTimePicker="false" id="birthdate"
                    v-model="form.birtDate" format="dd.MM.yyyy" modelType="yyyy-MM-dd">
                </Datepicker><br />
                <v-select label="Select" v-model="form.role" :items="['NURSE', 'DOCTOR', 'PATIENT']"></v-select>
                <div class="auth-footer-section">
                    <v-btn type="submit" variant="outlined" color="error" block class="mt-2"
                        @click.prevent="register">Inregistrare</v-btn>
                </div>
            </v-sheet>
        </div>
    </div>
</template>
<script>
import { mapActions } from "vuex";
export default {
    name: 'register-page',
    data: () => ({
        valid: false,
        form: {
            firstName: "",
            lastName: "",
            email: "",
            role: "NURSE",
            password: "gsmvlad",
            personalNumber: "",
            phone: "",
            homeAddress: "",
            city: "",
            county: "",
            birtDate: undefined
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
<style scoped>
.auth-footer-section {
    padding-top: 30px;
    padding-bottom: 30px;
}

.margin-top-form {
    margin-top: 350px !important;
}
</style>