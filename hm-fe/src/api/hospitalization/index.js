import axios from "axios";
import moment from "moment";

const headers = {
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
    "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
};

const apiUrl = process.env.VUE_APP_API_URL;

export const hospitalizationService = {
    addHospitalization,
    getHospitalization,
    exportExcel,
    generatePdf
};

function addHospitalization(body) {
    return axios.post(`${apiUrl}/hospitalizations/`, body, { headers });
}

function getHospitalization(filters, pagination) {
    let pagFilters = "";

    if (pagination) {
        pagFilters = `?page=${pagination.page - 1}&size=${pagination.size}`
    }

    let params = {
        startDate: filters?.startDate,
        endDate: filters?.endDate,
        search: filters?.search
    };
    return axios.get(`${apiUrl}/hospitalizations/${pagFilters}`, { params: params }, { headers });
}

function exportExcel(filters) {
    let params = {
        startDate: filters?.startDate,
        endDate: filters?.endDate,
        search: filters?.search
    };
    return axios.get(`${apiUrl}/hospitalizations/export-excel`, { params: params, responseType: 'arraybuffer', headers: headers }).then((res) => {
        const fileURL = window.URL.createObjectURL(new Blob([res.data]));
        const fileLink = document.createElement("a");
        const contentDisposition = res.headers["content-disposition"];
        let fileName = "Hospitalizations" + "_" + moment(filters?.startDate).format("YYYY-MM-DD") + "_" + moment(filters?.endDate).format("YYYY-MM-DD");

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

function generatePdf(hospitalizationId) {
    return axios.get(`${apiUrl}/hospitalizations/generate-hospitalization/${hospitalizationId}`, { headers })
}

