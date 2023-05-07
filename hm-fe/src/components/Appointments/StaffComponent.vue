<template>
    <v-table density="compact">
        <thead class="table-header">
            <tr>
                <th class="text-left">
                    Numar programare
                </th>
                <th class="text-left">
                    Nume aplicant
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
import { appointmentService } from '@/api';
import { snackbarColors } from "@/consts/colors";
import { appointmentStatus } from "@/consts/statuses";
import moment from "moment";

export default {
    name: "StaffComponent",
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
    methods: {
        loadAppointments() {
            this.filters.employeeId = this.$store.getters.StateEmployeeId;
            appointmentService.getAppointments(this.filters, this.pagination).then((res) => {
                this.tableData = res.data.result;
                this.pagination.totalPages = res.data.result.totalPages;
                this.pagination.totalElements = res.data.result.totalElements;
            })
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
        }
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
