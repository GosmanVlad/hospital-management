<template>
    <v-form ref="form">
        <v-row>
            <v-col md="3" style="padding-top:16px;">
                <v-text-field label="Cauta numele pacientului" v-model="this.filters.search"></v-text-field>
            </v-col>
            <v-col md="3">
                <label>Data internarii</label>
                <Datepicker required autoApply valueType="format" enableTimePicker="false" id="startDate"
                    v-model="this.filters.startDate" format="dd.MM.yyyy" modelType="yyyy-MM-dd">
                </Datepicker>
            </v-col>
            <v-col md="3">
                <label>Data externarii</label>
                <Datepicker required autoApply valueType="format" enableTimePicker="false" id="endDate"
                    v-model="this.filters.endDate" format="dd.MM.yyyy" modelType="yyyy-MM-dd">
                </Datepicker>
            </v-col>
            <v-col style="padding-top:25px; margin-left:15px">
                <v-btn @click="loadAllHospitalizations()" rounded="lg">Aplica</v-btn>
                <v-btn @click="resetFilters()" rounded="lg" color="error" style="margin-left:10px">Sterge</v-btn>
            </v-col>
        </v-row>
    </v-form>
    <v-row justify="end" class="modal">
        <v-dialog v-model="dialog" persistent width="1024" height="500">
            <template v-slot:activator="{ props }">
                <v-btn color="green" @click="exportExcel" style="margin-right: 10px" density="compact">
                    Exporta excel
                </v-btn>
                <v-btn color="error" v-bind="props" v-if="role === 'DOCTOR' || role === 'NURSE'" density="compact">
                    Adauga o internare
                </v-btn>

            </template>
            <v-card>
                <v-card-title>
                    <span class="text-h5">Adauga o internare</span>
                </v-card-title>
                <v-card-text>
                    <v-container>
                        <v-row>
                            <v-col cols="6">
                                <label>Data internarii</label>
                                <Datepicker required autoApply valueType="format" enableTimePicker="false" id="startDate"
                                    v-model="this.data.startDate" format="dd.MM.yyyy" modelType="yyyy-MM-dd">
                                </Datepicker>
                            </v-col>
                            <v-col cols="6">
                                <label>Data externarii</label>
                                <Datepicker required autoApply valueType="format" enableTimePicker="false" id="endDate"
                                    v-model="this.data.endDate" format="dd.MM.yyyy" modelType="yyyy-MM-dd">
                                </Datepicker>
                            </v-col>
                            <v-col md="6">
                                <v-autocomplete :items="this.patients"
                                    :item-title="(value) => value.firstName + ' ' + value.lastName" item-value="userId"
                                    single-line variant="underlined" append-inner-icon="mdi-magnify"
                                    v-model="this.data.patientId" label="Selecteaza pacient"
                                    placeholder="Selecteaza pacient"></v-autocomplete>
                            </v-col>
                            <v-col md="6">
                                <v-select :items="this.salons" item-title="salonName" item-value="salonId"
                                    variant="underlined" v-model="this.data.salonId" ref="salon"
                                    placeholder="Selecteaza salon" label="Selecteaza salon">
                                </v-select>
                            </v-col>

                            <v-col cols="12">
                                <v-text-field label="Diagnostic" v-model="this.data.diagnosis"></v-text-field>
                            </v-col>
                        </v-row>
                    </v-container>
                </v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="blue-darken-1" variant="text" @click="dialog = false">
                        Close
                    </v-btn>
                    <v-btn color="blue-darken-1" variant="text" @click="saveHospitalization()">
                        Save
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </v-row>
    <v-progress-linear indeterminate color="red-darken-2" v-if="loading"></v-progress-linear>
    <v-table density="compact">
        <thead class="table-header">
            <tr>
                <th class="text-left">
                    Numar internare
                </th>
                <th class="text-left">
                    Pacient
                </th>
                <th class="text-left">
                    Salon
                </th>
                <th class="text-left">
                    Data internarii
                </th>
                <th class="text-left">
                    Data externarii
                </th>
                <th class="text-left">
                    Doctor
                </th>
                <th class="text-left">
                    Actiuni
                </th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="item in tableData?.content" :key="item.hospitalizationId" class="table-rows">
                <td>{{ item.hospitalizationId }}</td>
                <td>{{ item.firstNamePatient + ' ' + item.lastNamePatient }}</td>
                <td>{{ item.salonName }}</td>
                <td>{{ formatDateWithoutHour(item.startDate) }}</td>
                <td>{{ formatDateWithoutHour(item.endDate) }}</td>
                <td>{{ item.doctorName }}</td>
                <td>
                    <div class="one-line">
                        <span class="group pa-2 cursor" @click="generatePdf(item.hospitalizationId)">
                            <v-tooltip activator="parent" location="top">Descarca internarea</v-tooltip>
                            <v-icon>mdi-file-cabinet</v-icon>
                        </span>

                    </div>
                </td>
            </tr>
        </tbody>
    </v-table>
    <div class="text-center">
        <v-pagination v-model="this.pagination.page" :length="this.pagination.totalPages"
            @update:modelValue="loadAllHospitalizations()" total-visible="10"></v-pagination>
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
import { snackbarColors } from "@/consts/colors";
import { hospitalizationService } from '@/api';
import { userService } from '@/api';
import { salonService } from '@/api';
import { fileService } from '@/api';
import moment from "moment";

export default {
    name: 'HospitalizationComponent',
    data: () => ({
        tableData: undefined,
        loading: false,
        dialog: false,
        pagination: {
            page: 1,
            totalPages: 1,
            size: 10,
            totalElements: 10,
        },
        data: {
            startDate: undefined,
            endDate: undefined,
            doctorId: undefined,
            patientId: undefined,
            departmentId: undefined,
            diagnosis: "",
            salonId: undefined
        },
        filters: {
            startDate: undefined,
            endDate: undefined,
            search: ""
        },
        snackbar: {
            status: "",
            message: "",
            show: false,
            color: snackbarColors.error
        },
        patients: [],
        salons: []
    }),
    mounted() {
        this.loadAllPatients();
        this.loadAllHospitalizations();
        this.getAllSalons();
    },
    methods: {
        saveHospitalization() {
            this.data.doctorId = this.$store.getters.StateUserId;
            hospitalizationService.addHospitalization(this.data).then(() => {
                this.dialog = false;
                this.loadAllHospitalizations();

                this.snackbar = {
                    show: true,
                    status: "success",
                    color: snackbarColors.success,
                    message: "Internarea efectuata cu succes!",
                }
            })
        },
        loadAllPatients() {
            userService.getAllPatients().then((res) => {
                this.patients = res.data.result;
            })
        },
        loadAllHospitalizations() {
            this.loading = true;

            if (this.filters?.startDate)
                this.filters.startDate = moment(this.filters.startDate).format("yyyy-MM-DD");
            if (this.filters?.endDate)
                this.filters.endDate = moment(this.filters.endDate).format("yyyy-MM-DD");

            hospitalizationService.getHospitalization(this.filters, this.pagination).then((res) => {
                this.tableData = res.data.result;
                this.pagination.totalPages = res.data.result.totalPages;
                this.pagination.totalElements = res.data.result.totalElements;
                this.loading = false;
            }).catch(() => {
                this.loading = false;
            })
        },
        formatDateWithoutHour(date) {
            return moment(date).format("DD-MM-YYYY")
        },
        getAllSalons() {
            salonService.getAllSalons().then((res) => this.salons = res.data.result);
        },
        resetFilters() {
            this.filters = {
                startDate: undefined,
                endDate: undefined,
                search: ""
            }
            this.loadAllHospitalizations();
        },
        exportExcel() {
            hospitalizationService.exportExcel(this.filters);
        },
        generatePdf(hospitalizationId) {
            hospitalizationService.generatePdf(hospitalizationId).then((res) => this.downloadFile(res.data.result));
        },
        downloadFile(filePath) {
            fileService.download(filePath);
        },
    },
    computed: {
        role: function () {
            return this.$store.getters.StateRole;
        },
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