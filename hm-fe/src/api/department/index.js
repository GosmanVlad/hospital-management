import axios from "axios";
const headers = {
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
    "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
};

const apiUrl = process.env.VUE_APP_API_URL;

export const departmentService = {
    getDepartments,
    removeDepartments,
    saveDepartment,
    importDepartmentsFromCsv
};

function getDepartments() {
    return axios.get(`${apiUrl}/departments`, { headers });
}

function removeDepartments(departmentId) {
    return axios.delete(`${apiUrl}/departments/${departmentId}`, { headers });
}

function saveDepartment(data) {
    return axios.post(`${apiUrl}/departments`, data, { headers });
}

function importDepartmentsFromCsv(csvFile) {
    let formData = new FormData();

    if (isNaN(csvFile) && !(typeof csvFile === 'string')) formData.append("doc", csvFile);

    return axios.post(`${apiUrl}/departments/import`, formData);
}