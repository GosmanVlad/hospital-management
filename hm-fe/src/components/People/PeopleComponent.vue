<template>
    <v-btn color="error" v-bind="props" style="margin-right:15px; margin-bottom:10px" density="compact"
        @click="goToRegisterPage()">
        Inregistreaza o persoana
    </v-btn>

    <v-tabs v-model="tab">
        <v-tab value="one" v-if="storeRole === 'DOCTOR'">Angajati</v-tab>
        <v-tab value="two">Pacienti</v-tab>
        <v-tab value="three">Importa persoane</v-tab>
    </v-tabs>

    <v-card-text>
        <v-window v-model="tab">
            <v-window-item value="one"> <br />
                <v-row>
                    <v-col md="3">
                        <v-autocomplete :items="this.doctorsNomenclature"
                            :item-title="(value) => value.user.firstName + ' ' + value.user.lastName"
                            item-value="employeeId" single-line variant="underlined" append-inner-icon="mdi-magnify"
                            v-model="this.filters.employeeId" label="Cauta doctor" placeholder="Doctor"></v-autocomplete>
                    </v-col>
                    <v-col md="3">
                        <v-select :items="this.roles" item-title="value" item-value="key" variant="underlined"
                            v-model="this.filters.role" ref="statusbar" placeholder="status" label="Selecteaza rol"
                            v-on:keyup.enter="loadStaff()">
                        </v-select>
                    </v-col>
                    <v-col style="padding-top:25px; margin-left:15px">
                        <v-btn @click="loadStaff()" rounded="lg">Aplica</v-btn>
                        <v-btn @click="resetFiltersStaff()" rounded="lg" color="error"
                            style="margin-left:10px">Sterge</v-btn>
                    </v-col>
                </v-row>
                <v-progress-linear indeterminate color="red-darken-2" v-if="loadingStaff"></v-progress-linear>
                <v-table density="compact">
                    <thead class="table-header">
                        <tr>
                            <th class="text-left">
                                #
                            </th>
                            <th class="text-left">
                                Nume
                            </th>
                            <th class="text-left">
                                Telefon
                            </th>
                            <th class="text-left">
                                Tara provenienta
                            </th>
                            <th class="text-left">
                                Oras
                            </th>
                            <th class="text-left">
                                Data angajarii
                            </th>
                            <th class="text-left">
                                Rol
                            </th>
                            <th class="text-left">
                                Actiuni
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="item in staff.content" :key="item.employeeId" class="table-rows">
                            <td>{{ item.employeeId }}</td>
                            <td>{{ item.user.firstName + " " + item.user.lastName }}</td>
                            <td>{{ item.user.phone }}</td>
                            <td>{{ item.user.county }}</td>
                            <td>{{ item.user.city }}</td>
                            <td>{{ formatDateWithoutHour(item.user.createdDate) }}</td>
                            <td>{{ item.user.role }}</td>
                            <td>
                                <div class="one-line">
                                    <span class="group pa-2 cursor">
                                        <router-link :to="'/profile/' + item.user.userId" style="color:black"><v-tooltip
                                                activator="parent" location="top">Vezi mai
                                                multe detalii</v-tooltip>
                                            <v-icon>mdi-account</v-icon></router-link>
                                    </span>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </v-table>
                <div class="text-center">
                    <v-pagination v-model="this.paginationStaff.page" :length="this.paginationStaff.totalPages"
                        @update:modelValue="loadStaff()" total-visible="10"></v-pagination>
                </div>
            </v-window-item>

            <v-window-item value="two">
                <v-row>
                    <v-col md="3">
                        <v-autocomplete :items="this.patientsNomenclature"
                            :item-title="(value) => value.firstName + ' ' + value.lastName" item-value="userId" single-line
                            variant="underlined" append-inner-icon="mdi-magnify" v-model="this.filtersPatients.userId"
                            label="Selecteaza pacient" placeholder="Selecteaza pacient"></v-autocomplete>
                    </v-col>
                    <v-col style="padding-top:25px; margin-left:15px">
                        <v-btn @click="loadAllPatients()" rounded="lg">Aplica</v-btn>
                        <v-btn @click="resetFiltersPatients()" rounded="lg" color="error"
                            style="margin-left:10px">Sterge</v-btn>
                    </v-col>
                </v-row>
                <v-progress-linear indeterminate color="red-darken-2" v-if="loadingPatients"></v-progress-linear>
                <v-table density="compact">
                    <thead class="table-header">
                        <tr>
                            <th class="text-left">
                                #
                            </th>
                            <th class="text-left">
                                Nume pacient
                            </th>
                            <th class="text-left">
                                Telefon
                            </th>
                            <th class="text-left">
                                Cod numeric personal
                            </th>
                            <th class="text-left">
                                Tara provenienta
                            </th>
                            <th class="text-left">
                                Oras
                            </th>
                            <th class="text-left">
                                Data crearii
                            </th>
                            <th class="text-left">
                                Actiuni
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="item in patients.content" :key="item.userId" class="table-rows">
                            <td>{{ item.userId }}</td>
                            <td>{{ item.firstName + " " + item.lastName }}</td>
                            <td>{{ item.phone }}</td>
                            <td>{{ item.personalNumber }}</td>
                            <td>{{ item.county }}</td>
                            <td>{{ item.city }}</td>
                            <td>{{ formatDateWithoutHour(item.createdDate) }}</td>
                            <td>
                                <div class="one-line">
                                    <span class="group pa-2 cursor">
                                        <router-link :to="'/profile/' + item.userId" style="color:black"><v-tooltip
                                                activator="parent" location="top">Vezi mai
                                                multe detalii</v-tooltip>
                                            <v-icon>mdi-account</v-icon></router-link>
                                    </span>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </v-table>
                <div class="text-center">
                    <v-pagination v-model="this.pagination.page" :length="this.pagination.totalPages"
                        @update:modelValue="loadAllPatients()" total-visible="10"></v-pagination>
                </div>
            </v-window-item>

            <v-window-item value="three">
                <v-progress-linear indeterminate color="red-darken-2" v-if="loading"></v-progress-linear>
                <v-form ref="csvForm">
                    <v-file-input label="Importa departamente din csv" @change="selectCSV"></v-file-input>
                    <v-btn color="green" variant="text" @click="downloadFile(templatePath)">
                        Descarca template
                    </v-btn>
                    <v-btn color="blue-darken-1" variant="text" @click="submitBtn()">
                        Importa
                    </v-btn>
                </v-form>
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
import { userService } from '@/api';
import { fileService } from '@/api';
import { employeeService } from '@/api';
import { roles } from "@/consts/roles";
import { snackbarColors } from "@/consts/colors";
import moment from "moment";

export default {
    name: 'PeopleComponent',
    data: () => ({
        tab: null,
        loadingPatients: false,
        loadingStaff: false,
        templatePath: "E:/hm/documents/people/model/import-model.csv",
        patients: [],
        pagination: {
            page: 1,
            totalPages: 1,
            size: 10,
            totalElements: 10,
        },
        paginationStaff: {
            page: 1,
            totalPages: 1,
            size: 10,
            totalElements: 10,
        },
        staff: [],
        filters: {
            employeeId: undefined,
            role: ""
        },
        filtersPatients: {
            userId: undefined,
        },
        doctorsNomenclature: [],
        patientsNomenclature: [],
        roles: roles,
        snackbar: {
            status: "",
            message: "",
            show: false,
            color: snackbarColors.error
        },
        csvFile: ""
    }),
    mounted() {
        this.loadAllPatients();
        this.loadStaff();
        this.loadDoctorsNomenclature();
        this.loadPatientNomenclature();
    },
    computed: {
        storeRole: function () {
            return this.$store.getters.StateRole;
        },
    },
    methods: {
        selectCSV(event) {
            this.csvFile = event.target.files[0];
        },
        goToRegisterPage() {
            this.$router.push("/register")
        },
        loadAllPatients() {
            this.loadingPatients = true;
            userService.getAllPatientsWithPagination(this.filtersPatients, this.pagination).then((res) => {
                this.patients = res.data.result;
                this.pagination.totalPages = res.data.result.totalPages;
                this.pagination.totalElements = res.data.result.totalElements;
                this.loadingPatients = false;
            }).catch(() => this.loadingPatients = false)
        },
        loadStaff() {
            this.loadingStaff = true;
            employeeService.getEmployeesWithPagination(this.filters, this.paginationStaff).then((res) => {
                this.staff = res.data.result;
                this.paginationStaff.totalPages = res.data.result.totalPages;
                this.paginationStaff.totalElements = res.data.result.totalElements;
                this.loadingStaff = false;
            }).catch(() => this.loadingStaff = false)

        },
        loadDoctorsNomenclature() {
            employeeService.getDoctors().then((res) => {
                this.doctorsNomenclature = res.data.result;
            })
        },
        loadPatientNomenclature() {
            userService.getAllPatients().then((res) => {
                this.patientsNomenclature = res.data.result;
            })
        },
        formatDateWithoutHour(date) {
            return moment(date).format("DD-MM-YYYY")
        },
        resetFiltersStaff() {
            this.filters = {
                employeeId: undefined,
                role: ""
            };
            this.loadStaff();
        },
        resetFiltersPatients() {
            this.filtersPatients = {
                userId: undefined,
            };
            this.loadAllPatients();
        },
        downloadFile(filePath) {
            fileService.download(filePath);
        },
        resetFileInput() {
            this.$refs.csvForm.reset();
            this.csvFile = "";
        },
        submitBtn() {
            this.loading = true;

            userService.importPeople(this.csvFile).then(() => {
                this.loading = false;

                this.loadAllPatients();
                this.loadStaff();
                this.loadDoctorsNomenclature();
                this.loadPatientNomenclature();

                this.tab = "one";
                this.snackbar = {
                    show: true,
                    status: "success",
                    color: snackbarColors.success,
                    message: "Fisierul csv a fost importat cu succes!"
                }
            }).catch((error) => {
                this.loading = false;
                this.snackbar = {
                    show: true,
                    status: "error",
                    color: snackbarColors.error,
                    message: error
                }
            });
        }
    },
}
</script>
<style scoped>
.modal {
    padding-bottom: 25px;
}

.one-line {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.cursor {
    cursor: pointer;
}
</style>