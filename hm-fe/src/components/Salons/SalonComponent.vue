<template>
    <v-tabs v-model="tab">
        <v-tab value="one">Lista saloane</v-tab>
        <v-tab value="two">Import csv</v-tab>
    </v-tabs>

    <v-card-text>
        <v-window v-model="tab">
            <v-window-item value="one"> <br />
                <v-row justify="end" class="modal">
                    <v-dialog v-model="dialog" persistent width="1024" height="500">
                        <template v-slot:activator="{ props }">
                            <v-btn color="error" v-bind="props" style="margin-right:15px" density="compact">
                                Adauga un salon
                            </v-btn>
                        </template>
                        <v-card>
                            <v-card-title>
                                <span class="text-h5">Adauga un salon</span>
                            </v-card-title>
                            <v-card-text>
                                <v-container>
                                    <v-row>
                                        <v-col cols="6">
                                            <v-text-field label="Nume salon" v-model="this.data.salonName"></v-text-field>
                                        </v-col>
                                        <v-col cols="6">
                                            <v-text-field label="Numar locuri" v-model="this.data.seats"></v-text-field>
                                        </v-col>
                                    </v-row>
                                    <v-row>
                                        <v-col cols="12">
                                            <v-select :items="this.departments" item-title="departmentName"
                                                item-value="departmentId" variant="underlined"
                                                v-model="this.data.departmentId" ref="department" placeholder="department"
                                                label="Selecteaza departament">
                                            </v-select>
                                        </v-col>
                                    </v-row>
                                </v-container>
                            </v-card-text>
                            <v-card-actions>
                                <v-spacer></v-spacer>
                                <v-btn color="blue-darken-1" variant="text" @click="dialog = false">
                                    Close
                                </v-btn>
                                <v-btn color="blue-darken-1" variant="text" @click="saveSalon()">
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
                                #
                            </th>
                            <th class="text-left">
                                Nume salon
                            </th>
                            <th class="text-left">
                                Departament
                            </th>
                            <th class="text-left">
                                Numar locuri
                            </th>
                            <th class="text-left">
                                Actiuni
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="item in salons.content" :key="item.salonId" class="table-rows">
                            <td>{{ item.salonId }}</td>
                            <td>{{ item.salonName }}</td>
                            <td>{{ item.department?.departmentName }}</td>
                            <td>{{ item.seats }}</td>
                            <td>
                                <div class="one-line">
                                    <span class="group pa-2 cursor" @click="removeSalon(item.salonId)">
                                        <v-tooltip activator="parent" location="top">Sterge</v-tooltip>
                                        <v-icon>mdi-cancel</v-icon>
                                    </span>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </v-table>
                <div class="text-center">
                    <v-pagination v-model="this.pagination.page" :length="this.pagination.totalPages"
                        @update:modelValue="getSalons()" total-visible="10"></v-pagination>
                </div>
            </v-window-item>

            <v-window-item value="two">
                <v-form ref="csvForm">
                    <v-file-input label="Importa saloanele din csv" @change="selectCSV"></v-file-input>
                    <v-btn color="green" variant="text" @click="downloadFile(templatePath)">
                        Descarca template
                    </v-btn>
                    <v-btn color="blue-darken-1" variant="text" @click="submitBtn()">
                        Importa
                    </v-btn>
                </v-form>
            </v-window-item>

        </v-window>
    </v-card-text>

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
import { departmentService, salonService } from '@/api';
import { fileService } from '@/api';
import { snackbarColors } from "@/consts/colors";

export default {
    name: 'SalonComponent',
    data: () => ({
        tab: null,
        snackbar: {
            status: "",
            message: "",
            show: false,
            color: snackbarColors.error
        },
        loading: false,
        salons: [],
        dialog: false,
        data: {
            salonName: "",
            seats: undefined,
            departmentId: undefined
        },
        pagination: {
            page: 1,
            totalPages: 1,
            size: 10,
            totalElements: 10,
        },
        csvFile: "",
        departments: [],
        templatePath: "E:/hm/documents/salons/model/import-model.csv"
    }),
    mounted() {
        this.getSalons();
        this.getDepartments();
    },
    methods: {
        selectCSV(event) {
            this.csvFile = event.target.files[0];
        },
        resetFileInput() {
            this.$refs.csvForm.reset();
            this.csvFile = "";
        },
        submitBtn() {
            this.loading = true;

            salonService.importSalonsFromCsv(this.csvFile).then(() => {
                this.loading = false;

                this.resetFileInput();
                this.getSalons();

                this.snackbar = {
                    show: true,
                    status: "success",
                    color: snackbarColors.success,
                    message: "Fisierul csv a fost importat cu succes!"
                }
            }).catch((error) => {
                this.loading = false;
                this.snackbar = {
                    show: true,
                    status: "error",
                    color: snackbarColors.error,
                    message: error
                }
            });
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
        getSalons() {
            this.loading = true;
            salonService.getAllSalonsWithPagination(this.pagination).then((res) => {
                this.salons = res.data.result;
                this.pagination.totalPages = res.data.result.totalPages;
                this.pagination.totalElements = res.data.result.totalElements;
                this.loading = false;
            }).catch((err) => {
                this.loading = false;
                this.snackbar = {
                    show: true,
                    status: "error",
                    color: snackbarColors.error,
                    message: err,
                }
            })
        },
        removeSalon(salonId) {
            salonService.deleteSalon(salonId).then(() => {
                this.getSalons();
                this.snackbar = {
                    show: true,
                    status: "success",
                    color: snackbarColors.success,
                    message: "Salon sters",
                }
            }).catch(() => {
                this.snackbar = {
                    show: true,
                    status: "error",
                    color: snackbarColors.error,
                    message: "Nu se poate sterge un salon care este folosit!",
                }
            })
        },
        saveSalon() {
            salonService.saveSalon(this.data).then(() => {
                this.dialog = false;
                this.getSalons();
                this.snackbar = {
                    show: true,
                    status: "success",
                    color: snackbarColors.success,
                    message: "Salon adaugat",
                }
            }).catch((err) => {
                this.snackbar = {
                    show: true,
                    status: "error",
                    color: snackbarColors.error,
                    message: err,
                }
            })
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