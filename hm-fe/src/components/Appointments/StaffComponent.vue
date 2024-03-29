<template>
    <v-form ref="form">
        <v-row>
            <v-col md="3">
                <v-select :items="this.statuses" item-title="value" item-value="key" variant="underlined"
                    v-model="this.filters.status" ref="statusbar" placeholder="status" label="Selecteaza status"
                    v-on:keyup.enter="loadAppointments()">
                </v-select>
            </v-col>
            <v-col style="padding-top:25px; margin-left:15px">
                <v-btn @click="loadAppointments()" rounded="lg">Aplica</v-btn>
                <v-btn @click="resetFilters()" rounded="lg" color="error" style="margin-left:10px">Sterge</v-btn>
            </v-col>
        </v-row>
    </v-form>
    <v-btn color="green" @click="exportExcel" style="margin-right: 10px" density="compact">
        Exporta excel
    </v-btn>
    <v-progress-linear indeterminate color="red-darken-2" v-if="loading"></v-progress-linear>
    <v-table density="compact">
        <thead class="table-header">
            <tr>
                <th class="text-left">
                    Numar programare
                </th>
                <th class="text-left">
                    Nume aplicant
                </th>
                <th class="text-left" v-if="role === 'NURSE'">
                    Doctor
                </th>
                <th class="text-left">
                    Data
                </th>
                <th class="text-left">
                    Detalii
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
                <td>{{ item.firstNameApplicant + ' ' + item.lastNameApplicant }}</td>
                <td v-if="role === 'NURSE'">{{ item.employeeName }}</td>
                <td>{{ formatDateWithHour(item.date) }}</td>
                <td>{{ item.details }}</td>
                <td><v-chip class="ma-2" :color="getStatusColour(item.status)" text-color="white">
                        {{ getStatusLabel(item.status) }}
                    </v-chip></td>
                <td>
                    <div class="one-line">
                        <span class="group pa-2 cursor" @click="updateStatus(item.appointmentId, 'accepted')"
                            v-if="item.status === 'pending'">
                            <v-tooltip activator="parent" location="top">Aproba programarea</v-tooltip>
                            <v-icon>mdi-check</v-icon>
                        </span>
                        <span class="group pa-2 cursor" @click="updateStatus(item.appointmentId, 'declined')"
                            v-if="item.status === 'pending'">
                            <v-tooltip activator="parent" location="top">Anuleaza programarea</v-tooltip>
                            <v-icon>mdi-cancel</v-icon>
                        </span>
                        <span class="group pa-2 cursor" @click="generatePdf(item.appointmentId)"
                            v-if="item.status === 'accepted'">
                            <v-tooltip activator="parent" location="top">Descarca programarea</v-tooltip>
                            <v-icon>mdi-file-cabinet</v-icon>
                        </span>
                    </div>
                </td>
            </tr>
        </tbody>
    </v-table>
    <div class="text-center">
        <v-pagination v-model="this.pagination.page" :length="this.pagination.totalPages"
            @update:modelValue="loadAppointments()" total-visible="10"></v-pagination>
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
import { appointmentService, fileService } from '@/api';
import { snackbarColors } from "@/consts/colors";
import { appointmentStatus } from "@/consts/statuses";
import moment from "moment";

export default {
    name: "StaffComponent",
    data: () => ({
        tableData: undefined,
        loading: false,
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
        snackbar: {
            status: "",
            message: "",
            show: false,
            color: snackbarColors.error
        },
        statuses: appointmentStatus
    }),
    mounted() {
        this.loadAppointments();
    },
    computed: {
        role: function () {
            return this.$store.getters.StateRole;
        },
    },
    methods: {
        loadAppointments() {
            if (this.$store.getters.StateRole === 'NURSE') {
                this.filters.departmentId = this.$store.getters.StateDepartmentId;
            } else {
                this.filters.employeeId = this.$store.getters.StateEmployeeId;
            }
            this.loading = true;
            appointmentService.getAppointments(this.filters, this.pagination).then((res) => {
                this.tableData = res.data.result;
                this.pagination.totalPages = res.data.result.totalPages;
                this.pagination.totalElements = res.data.result.totalElements;
                this.loading = false;
            }).catch(() => this.loading = false)
        },
        formatDateWithHour(date) {
            return moment(date).format("DD-MM-YYYY HH:mm")
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
        updateStatus(appointmentId, status) {
            appointmentService.changeAppointmentStatus(appointmentId, status).then(() => {
                this.loadAppointments();

                if (status === 'accepted') {
                    this.snackbar = {
                        show: true,
                        status: "success",
                        color: snackbarColors.success,
                        message: "Programare acceptata cu succes!",
                    }
                }
                if (status === 'declined') {
                    this.snackbar = {
                        show: true,
                        status: "success",
                        color: snackbarColors.success,
                        message: "Programare respinsa cu succes!",
                    }
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
        resetFilters() {
            this.filters = {
                userId: undefined,
                employeeId: null,
                departmentId: null,
                status: ""
            }
            this.loadAppointments();
        },
        downloadFile(filePath) {
            fileService.download(filePath);
        },
        generatePdf(appointmentId) {
            appointmentService.generatePdf(appointmentId).then((res) => this.downloadFile(res.data.result));
        },
        exportExcel() {
            appointmentService.exportExcel(this.filters);
        },
    }
}
</script>
<style scoped>
.one-line {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.cursor {
    cursor: pointer;
}
</style>
