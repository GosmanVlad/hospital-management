<template>
    <h3 class="page-title">Grafic facturi</h3>
    <v-row>
        <v-col md='6'>
            <div style="height: 600px; width: 700px">
                <GChart type="ColumnChart" :data="chartData" :options="chartOptions" />
            </div>
        </v-col>
        <v-col md='6'>
            <div style="height: 600px; width: 700px">
                <GChart type="PieChart" :data="chartData" :options="chartOptions" />
            </div>
        </v-col>
    </v-row>
</template>
<script>
import { raportService } from "@/api";
import moment from "moment";

export default {
    name: "DashboardComponent",
    data() {
        return {
            filters: {
                patientId: undefined,
                doctorId: undefined
            },
            chartOptions: {
                chart: {
                    title: "Company Performance",
                    subtitle: "Sales, Expenses, and Profit: 2014-2017",
                },
            },

            chartData: [
                ["Luna", "Platite", "Neplatite", "Anulat"],
            ]
        };
    },
    mounted() {
        this.loadInvoiceRaport();
    },
    methods: {
        loadInvoiceRaport() {
            if (this.$store.getters.StateEmployeeId) {
                this.filters = {
                    doctorId: this.$store.getters.StateEmployeeId
                }
            }
            raportService.getInvoicesRaport(this.filters).then((res) => {
                res.data.result.map((item) => {
                    const obj = [moment(item.date).format("MMM"), item.paidInvoices, item.unpaidInvoices, item.pendingInvoices];
                    this.chartData.push(obj)
                })
            })
        }
    }
}
</script>
<style scoped>
.test {
    margin-bottom: -300px;
}
</style>