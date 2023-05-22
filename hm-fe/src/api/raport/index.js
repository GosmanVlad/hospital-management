import axios from "axios";

const headers = {
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
    "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
};

const apiUrl = process.env.VUE_APP_API_URL;

export const raportService = {
    getInvoicesRaport
};
function getInvoicesRaport(filters) {

    let params = {
        patientId: filters?.patientId || undefined,
        doctorId: filters?.doctorId || undefined,
    };

    return axios.get(`${apiUrl}/raports/invoices`, { params: params }, { headers });
}