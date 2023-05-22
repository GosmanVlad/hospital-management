<template>
    <h3 class="page-title">Informatii generale</h3>
    <v-row>
        <v-col md="3" v-if="role !== 'PATIENT'">
            <v-card class="mx-auto card-purple" max-width="344" variant="outlined">
                <v-card-item>
                    <div>
                        <div class="text-overline mb-1">
                            Pacienti
                        </div>
                        <div class="text-h6 mb-1">
                            {{ this.cards.patients }}
                        </div>
                        <div class="text-caption">Toti pacientii acestui spital</div>
                    </div>
                </v-card-item>
            </v-card>
        </v-col>
        <v-col md="3">
            <v-card class="mx-auto card-blue" max-width="344" variant="outlined">
                <v-card-item>
                    <div>
                        <div class="text-overline mb-1">
                            Programari
                        </div>
                        <div class="text-h6 mb-1">
                            {{ this.cards.appointments }}
                        </div>
                        <div class="text-caption">Total programari efectuate pe acest cont</div>
                    </div>
                </v-card-item>
            </v-card>
        </v-col>
        <v-col md="3">
            <v-card class="mx-auto card-red" max-width="344" variant="outlined">
                <v-card-item>
                    <div>
                        <div class="text-overline mb-1">
                            Spitalizari
                        </div>
                        <div class="text-h6 mb-1">
                            {{ this.cards.hospitalizations }}
                        </div>
                        <div class="text-caption">Total internari efectuate pe acest cont</div>
                    </div>
                </v-card-item>
            </v-card>
        </v-col>
        <v-col md="3">
            <v-card class="mx-auto card-green" max-width="344" variant="outlined" v-if="role !== 'PATIENT'">
                <v-card-item>
                    <div>
                        <div class="text-overline mb-1">
                            Facturi
                        </div>
                        <div class="text-h6 mb-1">
                            {{ this.cards.invoices }}
                        </div>
                        <div class="text-caption">Total facturi inregistrate</div>
                    </div>
                </v-card-item>
            </v-card>

            <v-card class="mx-auto card-green" max-width="344" variant="outlined" v-if="role === 'PATIENT'">
                <v-card-item>
                    <div>
                        <div class="text-overline mb-1">
                            Facturi neplatite
                        </div>
                        <div class="text-h6 mb-1">
                            <span style="color: red; font-weight: bold;" v-if="this.cards.invoices > 0">{{
                                this.cards.invoices }}</span>
                            <span v-if="this.cards.invoices === 0">{{ this.cards.invoices }}</span>
                        </div>
                        <div class="text-caption">Total facturi inregistrate</div>
                    </div>
                </v-card-item>
            </v-card>
        </v-col>
    </v-row>
    <v-divider class="divider" />
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
            cards: [],
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
        this.loadCardsRaport();
    },
    methods: {
        loadInvoiceRaport() {
            if (this.$store.getters.StateEmployeeId) {
                this.filters = {
                    doctorId: this.$store.getters.StateEmployeeId
                }
            } else {
                this.filters = {
                    patientId: this.$store.getters.StateUserId
                }
            }
            raportService.getInvoicesRaport(this.filters).then((res) => {
                res.data.result.map((item) => {
                    const obj = [moment(item.date).format("MMM"), item.paidInvoices, item.unpaidInvoices, item.pendingInvoices];
                    this.chartData.push(obj)
                })
            })
        },

        loadCardsRaport() {
            if (this.$store.getters.StateEmployeeId) {
                this.filters = {
                    doctorId: this.$store.getters.StateEmployeeId
                }
            } else {
                this.filters = {
                    patientId: this.$store.getters.StateUserId
                }
            }
            raportService.getCardsRaport(this.filters).then((res) => {
                this.cards = res.data.result;
            })
        }
    },

    computed: {
        role: function () {
            return this.$store.getters.StateRole;
        },
    },
}
</script>
<style scoped>
.card-red {
    border-color: rgb(226, 25, 25) !important;
}

.card-green {
    border-color: rgb(52, 226, 25) !important;
}

.card-blue {
    border-color: rgb(52, 25, 226) !important;
}

.card-purple {
    border-color: rgb(179, 25, 226) !important;
}

.divider {
    margin-top: 25px;
    margin-bottom: 25px;
}
</style>