<template>
    <v-row>
        <v-col cols="5">
            <span style="font-weight: bold">Adauga procentul de TVA aplicat pentru serviciile
                prestate</span>
            <v-text-field label="Procent TVA" v-model="vatPercentage"></v-text-field>
        </v-col>
        <v-col cols="5">
            <v-autocomplete :items="this.patients" :item-title="(value) => value.firstName + ' ' + value.lastName"
                item-value="userId" single-line variant="underlined" append-inner-icon="mdi-magnify"
                v-model="this.selectedPatient" label="Selecteaza pacient" placeholder="Selecteaza pacient"
                style="margin-top:29px"></v-autocomplete>
        </v-col>
    </v-row>
    <v-divider /> <br />
    <h2>Servicii prestate</h2> <br />
    <v-row class="ml-4 mr-4" v-for="(item, index) in invoiceItems" :key="index">
        <v-col cols="6">
            <v-text-field label="Serviciul prestat" v-model="item.service"></v-text-field>
        </v-col>
        <v-col cols="1">
            <v-text-field label="Cost" v-model="item.brutCost"></v-text-field>
        </v-col>
        <v-col cols="3">
            <div style="padding-top:15px; font-size: 16px;font-weight: bold"><span>Din care TVA: </span>
                <span style="color:green">{{ Math.round(vatPercentage / 100 * item.brutCost * 100) / 100 }} RON</span>
            </div>
        </v-col>
        <v-col class="align d-flex">
            <div class="one-line">
                <span class="group pa-2 cursor" v-if="index + 1 === invoiceItems.length" @click="addNewRowOfInvoiceItem">
                    <v-tooltip activator="parent" location="top">Adauga item</v-tooltip>
                    <v-icon style="font-size: 30px; color:green">mdi-plus</v-icon>
                </span>
                <span class="group pa-2 cursor" v-if="index + 1 === invoiceItems.length && index !== 0"
                    @click="invoiceItems.splice(index, 1)">
                    <v-tooltip activator="parent" location="top">Sterge item</v-tooltip>
                    <v-icon style="font-size: 30px; color:#9d152d">mdi-trash-can</v-icon>
                </span>
            </div>
        </v-col>
    </v-row>
    <v-divider style="margin-top:30px;" /> <br />
    <span style="font-weight: bold">Selecteaza data la care se va factura</span><br />
    <Datepicker required autoApply valueType="format" enableTimePicker="false" id="startDate" v-model="this.invoiceDate"
        format="dd.MM.yyyy" modelType="yyyy-MM-dd">
    </Datepicker>
    <br />
    <v-btn @click="saveInvoice()" rounded="lg" style="margin-top: 15px; margin-bottom:20px" color="green">Salveaza
        factura</v-btn>
    <br />
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
import { userService } from '@/api';
import { snackbarColors } from "@/consts/colors";

export default {
    name: 'AddInvoiceComponent',
    data: () => ({
        invoiceDate: undefined,
        invoiceItems: [
            {
                service: "",
                brutCost: 0,
            }
        ],
        snackbar: {
            status: "",
            message: "",
            show: false,
            color: snackbarColors.error
        },
        vatPercentage: 0,
        patients: [],
        selectedPatient: undefined
    }),
    mounted() {
        this.loadAllPatients();
    },
    methods: {
        loadAllPatients() {
            userService.getAllPatients().then((res) => {
                this.patients = res.data.result;
            })
        },
        addNewRowOfInvoiceItem() {
            this.invoiceItems.push({
                service: "",
                brutCost: 0
            })
        },
        saveInvoice() {
            const body = {
                doctorId: this.$store.getters.StateEmployeeId,
                patientId: this.selectedPatient,
                date: this.invoiceDate,
                vatPercentage: this.vatPercentage,
                invoiceItems: this.invoiceItems
            }
            invoiceService.addInvoice(body).then(() => {
                this.snackbar = {
                    show: true,
                    status: "success",
                    color: snackbarColors.success,
                    message: "Factura a fost creata!",
                }
                this.$emit('invoice-added', 'true');
            }).catch((err) => {
                this.snackbar = {
                    show: true,
                    status: "error",
                    color: snackbarColors.error,
                    message: err,
                }
            })
        }
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