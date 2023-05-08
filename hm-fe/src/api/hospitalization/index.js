import axios from "axios";
const headers = {
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
    "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
};

const apiUrl = process.env.VUE_APP_API_URL;

export const hospitalizationService = {
    addHospitalization,
    getHospitalization
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

