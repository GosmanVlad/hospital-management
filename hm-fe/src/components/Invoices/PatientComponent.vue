<template>
    <v-form ref="form">
        <v-row>
            <v-col md="2">
                <v-select :items="this.statuses" item-title="value" item-value="key" variant="underlined"
                    v-model="this.filters.status" ref="statusbar" placeholder="status" label="Selecteaza status"
                    v-on:keyup.enter="getInvoices()" style="margin-top:13px">
                </v-select>
            </v-col>
            <v-col md="3">
                <label>Data start</label>
                <Datepicker required autoApply valueType="format" enableTimePicker="false" id="startDate"
                    v-model="this.filters.startDate" format="dd.MM.yyyy" modelType="yyyy-MM-dd">
                </Datepicker>
            </v-col>
            <v-col md="3">
                <label>Data final</label>
                <Datepicker required autoApply valueType="format" enableTimePicker="false" id="endDate"
                    v-model="this.filters.endDate" format="dd.MM.yyyy" modelType="yyyy-MM-dd">
                </Datepicker>
            </v-col>
            <v-col style="padding-top:25px; margin-left:15px">
                <v-btn @click="getInvoices()" rounded="lg">Aplica</v-btn>
                <v-btn @click="resetFilters()" rounded="lg" color="error" style="margin-left:10px">Sterge</v-btn>
            </v-col>
        </v-row>
    </v-form>
    <v-btn color="green" @click="exportExcel" style="margin-right: 10px; margin-top: 10px; margin-bottom: 10px"
        density="compact">
        Exporta excel
    </v-btn>
    <v-progress-linear indeterminate color="red-darken-2" v-if="loading"></v-progress-linear>
    <v-table density="compact">
        <thead class="table-header">
            <tr>
                <th class="text-left">
                    Numar factura
                </th>
                <th class="text-left">
                    Data emitere
                </th>
                <th class="text-left">
                    Doctor
                </th>
                <th class="text-left">
                    Total factura
                </th>
                <th class="text-left">
                    TVA
                </th>
                <th class="text-left">
                    Incasat
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
            <tr v-for="item in tableData" :key="item.details.invoiceId" class="table-rows">
                <td>{{ item.details.invoiceId }}</td>
                <td>{{ formatDateWithoutHour(item.details.date) }}</td>
                <td>{{ item.details.doctorName }}</td>
                <td>{{ item.totalInvoice }} RON</td>
                <td>{{ item.vatPercentage }}% ({{ item.vatTax }} RON)</td>
                <td style="color: green; font-weight: bold">{{ item.totalInvoice - item.vatTax }} RON</td>
                <td><v-chip class="ma-2" :color="getStatusColour(item.status)" text-color="white">
                        {{ getStatusLabel(item.status) }}
                    </v-chip></td>
                <td>
                    <div class="one-line">
                        <span class="group pa-2 cursor" @click="generatePdf(item.invoiceId)">
                            <v-tooltip activator="parent" location="top">Descarca factura</v-tooltip>
                            <v-icon>mdi-file-cabinet</v-icon>
                        </span>
                    </div>
                </td>
            </tr>
        </tbody>
    </v-table>

    <div class="text-center">
        <v-pagination v-model="this.pagination.page" :length="this.pagination.totalPages" @update:modelValue="getInvoices()"
            total-visible="10"></v-pagination>
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
import { invoiceService } from '@/api';
import { snackbarColors } from "@/consts/colors";
import { fileService } from '@/api';
import { invoiceStatus } from "@/consts/statuses";
import moment from "moment";

export default {
    name: 'PatientComponent',
    data: () => ({
        loading: false,
        pagination: {
            page: 1,
            totalPages: 1,
            size: 10,
            totalElements: 10,
        },
        filters: {
            startDate: undefined,
            endDate: undefined,
            patientId: undefined,
            status: ""
        },
        snackbar: {
            status: "",
            message: "",
            show: false,
            color: snackbarColors.error
        },
        tableData: [],
        totalSumOfInvoice: 0,
        statuses: invoiceStatus,
    }),
    mounted() {
        this.getInvoices();
    },
    methods: {
        generatePdf(invoiceId) {
            this.loading = true;
            invoiceService.generatePdf(invoiceId).then((res) => {
                this.downloadFile(res.data.result);
                this.loading = false;
            }).catch(() => this.loading = false);
        },
        downloadFile(filePath) {
            fileService.download(filePath);
        },
        getInvoices() {
            this.loading = true;
            this.tableData = [];
            this.totalSumOfInvoice = 0;
            if (this.filters?.startDate)
                this.filters.startDate = moment(this.filters.startDate).format("yyyy-MM-DD");
            if (this.filters?.endDate)
                this.filters.endDate = moment(this.filters.endDate).format("yyyy-MM-DD");
            this.filters.patientId = this.$store.getters.StateUserId;

            invoiceService.getInvoices(this.filters, this.pagination).then((res) => {
                this.pagination.totalPages = res.data.result.totalPages;
                this.pagination.totalElements = res.data.result.totalElements;
                this.loading = false;

                res.data.result.content.map((invoice) => {
                    let totalInvoice = 0;

                    invoice.invoiceItems.map((item) => {
                        totalInvoice += item.brutCost;
                        this.totalSumOfInvoice += item.brutCost;
                    })
                    this.tableData.push({
                        status: invoice.status,
                        invoiceId: invoice.invoiceId,
                        details: invoice,
                        totalInvoice: totalInvoice,
                        vatPercentage: invoice.vatPercentage,
                        vatTax: Math.round(invoice.vatPercentage / 100 * totalInvoice * 100) / 100,
                    })
                })
            }).catch(() => {
                this.loading = false;
                this.tableData = []
            })
        },

        exportExcel() {
            this.loading = true;
            invoiceService.exportExcel(this.filters).then(() => {
                this.loading = false;
            }).catch(() => this.loading = false);
        },

        formatDateWithoutHour(date) {
            return moment(date).format("DD-MM-YYYY")
        },

        resetFilters() {
            this.filters = {
                startDate: undefined,
                endDate: undefined,
                patientId: this.$store.getters.StateUserId,
                status: ""
            }
            this.getInvoices();
        },

        getStatusColour(status) {
            if (status === 'unpaid')
                return 'error';
            if (status === 'paid')
                return 'green'
            if (status === 'canceled')
                return 'gray'
            return '';
        },
        getStatusLabel(status) {
            const stats = this.statuses.find((x) => x.key === status);
            return stats?.value || "Unknown";
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