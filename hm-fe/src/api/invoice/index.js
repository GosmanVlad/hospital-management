import axios from "axios";
const headers = {
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
    "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
};

const apiUrl = process.env.VUE_APP_API_URL;

export const invoiceService = {
    getInvoices,
    addInvoice
};

function getInvoices(filters, pagination) {
    let pagFilters = "";

    if (pagination) {
        pagFilters = `?page=${pagination.page - 1}&size=${pagination.size}`
    }

    let params = {
        startDate: filters?.startDate,
        endDate: filters?.endDate,
        search: filters?.search
    };
    return axios.get(`${apiUrl}/invoices/${pagFilters}`, { params: params }, { headers });
}

function addInvoice(body) {
    return axios.post(`${apiUrl}/invoices/`, body, { headers });
}