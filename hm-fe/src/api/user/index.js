import axios from "axios";
const headers = {
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
    "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
};

const apiUrl = process.env.VUE_APP_API_URL;

export const userService = {
    getAllPatients,
    getAllPatientsWithPagination,
    importPeople,
    getUserProfile,
    updateUserPassword,
    updateUserProfile
};

function getAllPatients() {
    return axios.get(`${apiUrl}/users/patients`, { headers });
}

function getAllPatientsWithPagination(filters, pagination) {
    let pagFilters = "";

    if (pagination) {
        pagFilters = `?page=${pagination.page - 1}&size=${pagination.size}&pagination=true`
    }
    let params = {
        userId: filters?.userId
    };
    return axios.get(`${apiUrl}/users/patients/${pagFilters}`, { params: params }, { headers });
}

function importPeople(csvFile) {
    let formData = new FormData();

    if (isNaN(csvFile) && !(typeof csvFile === 'string')) formData.append("doc", csvFile);

    return axios.post(`${apiUrl}/users/import`, formData);
}

function getUserProfile(userId) {
    return axios.get(`${apiUrl}/users/profile/${userId}`, { headers });
}

function updateUserPassword(userId, body) {
    return axios.put(`${apiUrl}/auth/change-password/${userId}`, body, { headers });
}

function updateUserProfile(userId, body) {
    return axios.put(`${apiUrl}/users/profile/${userId}`, body, { headers });
}

