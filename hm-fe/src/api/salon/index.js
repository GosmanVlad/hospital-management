import axios from "axios";
const headers = {
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
    "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
};

const apiUrl = process.env.VUE_APP_API_URL;

export const salonService = {
    getAllSalons,
    getAllSalonsWithPagination,
    deleteSalon,
    saveSalon,
    importSalonsFromCsv
};

function getAllSalons() {
    return axios.get(`${apiUrl}/salons/`, { headers });
}

function getAllSalonsWithPagination(pagination) {
    let pagFilters = "";
    if (pagination) {
        pagFilters = `?page=${pagination.page - 1}&size=${pagination.size}&pagination=true`
    }
    return axios.get(`${apiUrl}/salons/${pagFilters}`, { headers });
}

function deleteSalon(salonId) {
    return axios.delete(`${apiUrl}/salons/${salonId}`, { headers });
}

function saveSalon(data) {
    return axios.post(`${apiUrl}/salons`, data, { headers });
}

function importSalonsFromCsv(csvFile) {
    let formData = new FormData();

    if (isNaN(csvFile) && !(typeof csvFile === 'string')) formData.append("doc", csvFile);

    return axios.post(`${apiUrl}/salons/import`, formData);
}
