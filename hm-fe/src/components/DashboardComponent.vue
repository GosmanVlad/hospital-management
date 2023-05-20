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
export default {
    name: "DashboardComponent",
    data() {
        return {
            chartData: [
                ["Month", "Platite", "Neplatite", "In asteptare"],
                ["Ianuarie", 1000, 400, 200],
                ["Martie", 1170, 460, 250],
                ["Aprilie", 660, 1120, 300],
                ["Mai", 1030, 540, 350],
                ["Iunie", 1030, 540, 350],
                ["Iulie", 1030, 540, 350],
                ["August", 1030, 540, 350],
                ["Septembrie", 1030, 540, 350],
                ["Octombrie", 1030, 540, 350],
                ["Noiembrie", 1030, 540, 350],
                ["Decembrie", 1030, 540, 350],
            ],
            chartOptions: {
                chart: {
                    title: "Company Performance",
                    subtitle: "Sales, Expenses, and Profit: 2014-2017",
                },
            },
        };
    },
}
</script>
<style scoped>
.box {}

.test {
    margin-bottom: -300px;
}
</style>