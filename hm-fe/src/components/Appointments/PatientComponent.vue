<template>
    <h3 class="page-title">Istoric programari</h3>
    <v-row justify="end" class="modal">
        <v-dialog v-model="dialog" persistent width="1024">
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
                            <v-col cols="12" sm="6">
                                <v-select :items="['0-17', '18-29', '30-54', '54+']" label="Age*" required></v-select>
                            </v-col>
                            <v-col cols="12" sm="6">
                                <v-autocomplete
                                    :items="['Skiing', 'Ice hockey', 'Soccer', 'Basketball', 'Hockey', 'Reading', 'Writing', 'Coding', 'Basejump']"
                                    label="Interests" multiple></v-autocomplete>
                            </v-col>
                            <v-col cols="12">
                                <v-text-field label="Detalii"></v-text-field>
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
                    <v-btn color="blue-darken-1" variant="text" @click="dialog = false">
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
                    Status
                </th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="item in tableData?.content" :key="item.appointmentId" class="table-rows">
                <td>{{ item.appointmentId }}</td>
                <td>{{ item.status }}</td>
            </tr>
        </tbody>
    </v-table>
    <div class="text-center">
        <v-pagination v-model="this.pagination.page" :length="this.pagination.totalPages"
            @update:modelValue="loadMessages(false)" total-visible="10"></v-pagination>
    </div>
</template>
<script>
import { appointmentService } from '@/api';

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
        dialog: false,
    }),
    mounted() {
        this.filters.userId = this.$store.getters.StateUserId;
        appointmentService.getAppointments(this.filters, this.pagination).then((res) => {
            this.tableData = res.data.result;
            this.pagination.totalPages = res.data.totalPages;
            this.pagination.totalElements = res.data.totalElements;
        })
    }
}
</script>
<style scoped>
.modal {
    padding-bottom: 25px;
}
</style>