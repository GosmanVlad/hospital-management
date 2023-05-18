import axios from "axios";
import moment from "moment";

const headers = {
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
    "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
};

const apiUrl = process.env.VUE_APP_API_URL;

export const invoiceService = {
    getInvoices,
    addInvoice,
    exportExcel,
    generatePdf
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

function exportExcel(filters) {
    let params = {
        startDate: filters?.startDate,
        endDate: filters?.endDate,
        search: filters?.search
    };
    return axios.get(`${apiUrl}/invoices/export-excel`, { params: params, responseType: 'arraybuffer', headers: headers }).then((res) => {
        const fileURL = window.URL.createObjectURL(new Blob([res.data]));
        const fileLink = document.createElement("a");
        const contentDisposition = res.headers["content-disposition"];
        let fileName = "Invoices" + "_" + moment(filters?.startDate).format("YYYY-MM-DD") + "_" + moment(filters?.endDate).format("YYYY-MM-DD");

        if (contentDisposition) {
            const fileNameMatch = contentDisposition.match(/filename="(.+)"/);
            if (fileNameMatch?.length === 2) fileName = fileNameMatch[1];
        }

        fileLink.href = fileURL;

        fileLink.setAttribute("download", fileName + ".xlsx");
        document.body.appendChild(fileLink);

        fileLink.click();
    })
}

function generatePdf(invoiceId) {
    return axios.get(`${apiUrl}/invoices/generate-invoice/${invoiceId}`, { headers })
}