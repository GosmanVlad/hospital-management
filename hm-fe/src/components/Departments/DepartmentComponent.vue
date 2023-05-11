<template>
    <v-row justify="end" class="modal">
        <v-dialog v-model="dialog" persistent width="1024" height="500">
            <template v-slot:activator="{ props }">
                <v-btn color="error" v-bind="props">
                    Adauga un departament
                </v-btn>
            </template>
            <v-card>
                <v-card-title>
                    <span class="text-h5">Adauga un departament</span>
                </v-card-title>
                <v-card-text>
                    <v-container>
                        <v-row>
                            <v-col cols="12">
                                <v-text-field label="Nume departament" v-model="this.data.departmentName"></v-text-field>
                            </v-col>
                        </v-row>
                    </v-container>
                </v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="blue-darken-1" variant="text" @click="dialog = false">
                        Close
                    </v-btn>
                    <v-btn color="blue-darken-1" variant="text" @click="saveDepartment()">
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
                    Nume departament
                </th>
                <th class="text-left">
                    Actiuni
                </th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="item in departments" :key="item.departmentId" class="table-rows">
                <td>{{ item.departmentId }}</td>
                <td>{{ item.departmentName }}</td>
                <td>
                    <div class="one-line">
                        <span class="group pa-2 cursor" @click="removeDepartments(item.departmentId)">
                            <v-tooltip activator="parent" location="top">Sterge</v-tooltip>
                            <v-icon>mdi-cancel</v-icon>
                        </span>
                    </div>
                </td>
            </tr>
        </tbody>
    </v-table>

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
import { departmentService } from '@/api';
import { snackbarColors } from "@/consts/colors";

export default {
    name: 'DepartmentComponent',
    data: () => ({
        snackbar: {
            status: "",
            message: "",
            show: false,
            color: snackbarColors.error
        },
        loading: false,
        departments: [],
        dialog: false,
        data: {
            departmentName: ""
        }
    }),
    mounted() {
        this.getDepartments();
    },
    methods: {
        getDepartments() {
            this.loading = true;
            departmentService.getDepartments().then((res) => {
                this.departments = res.data.result;
                console.log(this.departments);
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
        removeDepartments(departmentId) {
            console.log(departmentId);
            departmentService.removeDepartments(departmentId).then(() => {
                this.getDepartments();
                this.snackbar = {
                    show: true,
                    status: "success",
                    color: snackbarColors.success,
                    message: "Departament sters",
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
        saveDepartment() {
            departmentService.saveDepartment(this.data).then(() => {
                this.dialog = false;
                this.getDepartments();
                this.snackbar = {
                    show: true,
                    status: "success",
                    color: snackbarColors.success,
                    message: "Departament adaugat",
                }
            }).catch((err) => {
                this.snackbar = {
                    show: true,
                    status: "error",
                    color: snackbarColors.error,
                    message: err,
                }
            })
        }
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