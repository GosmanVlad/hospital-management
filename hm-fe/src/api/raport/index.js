import axios from "axios";

const headers = {
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
    "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
};

const apiUrl = process.env.VUE_APP_API_URL;

export const raportService = {
    getInvoicesRaport,
    getCardsRaport,
    getPieRaport
};

function getInvoicesRaport(filters) {

    let params = {
        patientId: filters?.patientId || undefined,
        doctorId: filters?.doctorId || undefined,
    };

    return axios.get(`${apiUrl}/raports/invoices`, { params: params }, { headers });
}

function getCardsRaport(filters) {

    let params = {
        patientId: filters?.patientId || undefined,
        doctorId: filters?.doctorId || undefined,
    };

    return axios.get(`${apiUrl}/raports/cards`, { params: params }, { headers });
}

function getPieRaport(filters) {

    let params = {
        patientId: filters?.patientId || undefined,
        doctorId: filters?.doctorId || undefined,
    };

    return axios.get(`${apiUrl}/raports/pie-raport`, { params: params }, { headers });
}