<template>
    <v-tabs v-model="tab">
        <v-tab value="one">Detalii</v-tab>
        <v-tab value="two">Schimba parola</v-tab>
    </v-tabs>

    <v-card-text>
        <v-window v-model="tab">
            <v-window-item value="one"> <br />
                <v-row>
                    <v-col md="6"><v-text-field label="Nume familie" v-model="this.data.firstName"></v-text-field></v-col>
                    <v-col md="6"><v-text-field label="Prenume" v-model="this.data.lastName"></v-text-field></v-col>
                    <v-col md="6"><v-text-field label="CNP" v-model="this.data.personalNumber"></v-text-field></v-col>
                    <v-col md="6"><v-text-field label="Tara" v-model="this.data.country"></v-text-field></v-col>
                    <v-col md="6"><v-text-field label="Oras" v-model="this.data.city"></v-text-field></v-col>
                    <v-col md="6"><v-text-field label="Adresa" v-model="this.data.homeAddress"></v-text-field></v-col>
                    <v-col md="6"><v-text-field label="Numar telefon" v-model="this.data.phone"></v-text-field></v-col>
                    <v-col md="6">
                        <span>Data nasterii</span>
                        <Datepicker required autoApply valueType="format" enableTimePicker="false" id="endDate"
                            v-model="this.data.birthDate" format="dd.MM.yyyy" modelType="yyyy-MM-dd" />
                    </v-col>
                </v-row>
                <div class="text-center" style="margin-top:15px"><v-btn color="green" @click="updateUser()">
                        Salveaza user
                    </v-btn></div>
            </v-window-item>

            <v-window-item value="two">
                <v-row>
                    <v-col md="6"><v-text-field label="Parola noua" v-model="this.password.password"
                            type="password"></v-text-field></v-col>
                    <v-col md="6"><v-text-field label="Repetare parola noua" v-model="this.password.repeatPassword"
                            type="password"></v-text-field></v-col>
                </v-row>
                <div class="text-center" style="margin-top:15px"><v-btn color="green" @click="updatePassword()">
                        Salveaza parola
                    </v-btn></div>
            </v-window-item>

        </v-window>
    </v-card-text>
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

import { userService } from "@/api";
import { snackbarColors } from "@/consts/colors";

export default {
    name: 'SalonComponent',
    props: ['id'],
    data: () => ({
        tab: null,
        data: [],
        password: {
            password: "",
            repeatPassword: ""
        },
        snackbar: {
            status: "",
            message: "",
            show: false,
            color: snackbarColors.error
        },
    }),
    mounted() {
        this.getUserProfile();
    },
    methods: {
        getUserProfile() {
            userService.getUserProfile(this.id).then((res) => {
                this.data = res.data.result;
            })
        },
        updateUser() {
            userService.updateUserProfile(this.id, this.data).then(() => {
                this.snackbar = {
                    show: true,
                    status: "success",
                    color: snackbarColors.success,
                    message: "Contului cu id-ul " + this.id + " a fost actualizat!",
                }
            }).catch((err) => {
                this.snackbar = {
                    show: true,
                    status: "error",
                    color: snackbarColors.error,
                    message: err,
                }
            })
        },
        updatePassword() {
            userService.updateUserPassword(this.id, this.password).then(() => {
                this.snackbar = {
                    show: true,
                    status: "success",
                    color: snackbarColors.success,
                    message: "Parola contului cu id-ul " + this.id + " a fost schimbata!",
                }
            }).catch((err) => {
                this.snackbar = {
                    show: true,
                    status: "error",
                    color: snackbarColors.error,
                    message: err,
                }
            })
        }
    }
}
</script>