<template>
    <h3 class="page-title">Istoric programari</h3>
    <v-row justify="end" class="modal">
        <v-dialog v-model="dialog" persistent width="1024" height="500">
            <template v-slot:activator="{ props }">
                <v-btn color="error" v-bind="props">
                    Efectueaza o programare
                </v-btn>
            </template>
            <v-card>
                <v-card-title>
                    <span class="text-h5">Efectueaza o parogramare</span>
                </v-card-title>
                <v-card-text>
                    <v-container>
                        <v-row>
                            <v-col cols="12">
                                <Datepicker required autoApply valueType="format" enableTimePicker="true" id="startDate"
                                    v-model="this.saveAppointmentBody.selectedDate">
                                </Datepicker>
                            </v-col>
                            <v-col cols="12" sm="6">
                                <v-select :items="this.departments" item-title="departmentName" item-value="departmentId"
                                    variant="underlined" v-model="this.saveAppointmentBody.selectedDepartment"
                                    ref="department" placeholder="Sectie" label="Selecteaza sectie"
                                    @update:modelValue="getEmployeeByDepartmentId()">
                                </v-select>
                            </v-col>
                            <v-col cols="12" sm="6">
                                <v-select :items="this.employees"
                                    :item-title="(value) => value.user.firstName + ' ' + value.user.lastName"
                                    item-value="employeeId" variant="underlined"
                                    v-model="this.saveAppointmentBody.selectedEmployee" ref="doctor" placeholder="Doctor"
                                    label="Selecteaza doctor">
                                </v-select>
                            </v-col>

                            <v-col cols="12">
                                <v-text-field label="Detalii" v-model="this.saveAppointmentBody.details"></v-text-field>
                            </v-col>
                        </v-row>
                    </v-container>
                    <small>Datele personale vor fi extrase din contul de pe care se face programarea</small>
                </v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="blue-darken-1" variant="text" @click="dialog = false">
                        Close
                    </v-btn>
                    <v-btn color="blue-darken-1" variant="text" @click="saveAppointment()">
                        Save
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </v-row>
    <v-table density="compact">
        <thead class="table-header">
            <tr>
                <th class="text-left">
                    Numar programare
                </th>
                <th class="text-left">
                    Sectie
                </th>
                <th class="text-left">
                    Doctor
                </th>
                <th class="text-left">
                    Data
                </th>
                <th class="text-left">
                    Status
                </th>
                <th class="text-left">
                    Actiuni
                </th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="item in tableData?.content" :key="item.appointmentId" class="table-rows">
                <td>{{ item.appointmentId }}</td>
                <td>{{ item.departmentName }}</td>
                <td>{{ item.employeeName }}</td>
                <td>{{ formatDateWithHour(item.date) }}</td>
                <td><v-chip class="ma-2" :color="getStatusColour(item.status)" text-color="white">
                        {{ getStatusLabel(item.status) }}
                    </v-chip></td>
                <td>
                    <div class="one-line">
                        <span class="group pa-2 cursor" @click="cancelAppointment(item.appointmentId)"
                            v-if="item.status === 'pending'">
                            <v-tooltip activator="parent" location="top">Anuleaza programarea</v-tooltip>
                            <v-icon>mdi-cancel</v-icon>
                        </span>
                    </div>
                </td>
            </tr>
        </tbody>
    </v-table>
    <div class="text-center">
        <v-pagination v-model="this.pagination.page" :length="this.pagination.totalPages"
            @update:modelValue="getAppointments()" total-visible="10"></v-pagination>
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
import { appointmentService } from '@/api';
import { departmentService } from '@/api';
import { employeeService } from '@/api';
import { appointmentStatus } from "@/consts/statuses";
import { snackbarColors } from "@/consts/colors";
import moment from "moment";

export default {
    name: "PatientComponent",
    data: () => ({
        tableData: undefined,
        pagination: {
            page: 1,
            totalPages: 1,
            size: 10,
            totalElements: 10,
        },
        filters: {
            userId: undefined,
            employeeId: null,
            departmentId: null,
            status: ""
        },
        saveAppointmentBody: {
            selectedDepartment: undefined,
            selectedEmployee: undefined,
            selectedDate: moment(),
            details: ""
        },
        snackbar: {
            status: "",
            message: "",
            show: false,
            color: snackbarColors.error
        },
        dialog: false,
        departments: [],
        employees: [],
        statuses: appointmentStatus
    }),
    mounted() {
        this.getAppointments();
        this.getDepartments();
    },
    methods: {
        getAppointments() {
            this.filters.userId = this.$store.getters.StateUserId;
            appointmentService.getAppointments(this.filters, this.pagination).then((res) => {
                this.tableData = res.data.result;
                this.pagination.totalPages = res.data.result.totalPages;
                this.pagination.totalElements = res.data.result.totalElements;
            }).catch((err) => {
                this.snackbar = {
                    show: true,
                    status: "error",
                    color: snackbarColors.error,
                    message: err,
                }
            })
        },
        getDepartments() {
            departmentService.getDepartments().then((res) => {
                this.departments = res.data.result;
            }).catch((err) => {
                this.snackbar = {
                    show: true,
                    status: "error",
                    color: snackbarColors.error,
                    message: err,
                }
            })
        },
        getEmployeeByDepartmentId() {
            employeeService.getEmployeeByDepartmentId(this.saveAppointmentBody.selectedDepartment).then((res) => {
                this.employees = res.data.result;
            }).catch((err) => {
                this.snackbar = {
                    show: true,
                    status: "error",
                    color: snackbarColors.error,
                    message: err,
                }
            })
        },
        saveAppointment() {

            const body = {
                details: this.saveAppointmentBody.details,
                date: moment(this.saveAppointmentBody.selectedDate).format("yyyy-MM-DD[T]HH:mm"),
                departmentId: this.saveAppointmentBody.selectedDepartment || null,
                patientId: this.$store.getters.StateUserId,
                employeeId: this.saveAppointmentBody.selectedEmployee || null
            }
            appointmentService.saveAppointment(body).then((res) => {
                this.getAppointments();

                if (res.result === 'overlap_appointment') {
                    this.snackbar = {
                        show: true,
                        status: "error",
                        color: snackbarColors.error,
                        message: "In acest interval sunt deja facute programari!",
                    }
                }
                this.snackbar = {
                    show: true,
                    status: "success",
                    color: snackbarColors.success,
                    message: "Programare efectuata pe data de " + this.formatDateWithHour(this.saveAppointmentBody.selectedDate),
                }

                this.saveAppointmentBody = {
                    selectedDepartment: undefined,
                    selectedEmployee: undefined,
                    selectedDate: moment(),
                    details: ""
                };
                this.dialog = false;


            }).catch((err) => {
                if (err.response.data.result === 'overlap_appointment') {
                    this.snackbar = {
                        show: true,
                        status: "error",
                        color: snackbarColors.error,
                        message: "In acest interval sunt deja facute programari!",
                    }
                } else if (err.response.data.result === 'some_fields_empty') {
                    this.snackbar = {
                        show: true,
                        status: "error",
                        color: snackbarColors.error,
                        message: "Toate campurile trebuie completate!",
                    }
                }
            })
        },
        formatDateWithHour(date) {
            return moment(date).format("DD-MM-YYYY HH:mm")
        },

        cancelAppointment(appointmentId) {
            appointmentService.changeAppointmentStatus(appointmentId, 'canceled').then(() => {
                this.getAppointments();

                this.snackbar = {
                    show: true,
                    status: "success",
                    color: snackbarColors.success,
                    message: "Programare anulata cu succes!",
                }
            }).catch((err) => {
                this.snackbar = {
                    show: true,
                    status: "error",
                    color: snackbarColors.error,
                    message: err,
                }
            });
        },

        getStatusColour(status) {
            if (status === 'pending')
                return 'primary';
            if (status === 'accepted')
                return 'green'
            if (status === 'canceled')
                return 'error'
            if (status === 'declined')
                return 'error'
            return '';
        },
        getStatusLabel(status) {
            const stats = this.statuses.find((x) => x.key === status);
            return stats?.value || "Unknown";
        },
    }
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