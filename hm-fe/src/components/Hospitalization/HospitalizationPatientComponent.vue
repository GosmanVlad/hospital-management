<template>
    <v-progress-linear indeterminate color="red-darken-2" v-if="loading"></v-progress-linear>
    <v-table density="compact">
        <thead class="table-header">
            <tr>
                <th class="text-left">
                    Numar internare
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
</template>

<script>
import { hospitalizationService } from '@/api';
import { fileService } from '@/api';
import moment from "moment";

export default {
    name: 'HospitalizationPatientComponent',
    data: () => ({
        tableData: undefined,
        pagination: {
            page: 1,
            totalPages: 1,
            size: 10,
            totalElements: 10,
        },
        filters: {
            startDate: undefined,
            endDate: undefined,
            search: ""
        },
        loading: false
    }),
    mounted() {
        this.loadAllHospitalizations();
    },
    methods: {
        loadAllHospitalizations() {
            this.loading = true;

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
        generatePdf(hospitalizationId) {
            hospitalizationService.generatePdf(hospitalizationId).then((res) => this.downloadFile(res.data.result));
        },
        downloadFile(filePath) {
            fileService.download(filePath);
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