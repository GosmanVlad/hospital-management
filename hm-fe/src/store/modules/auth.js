import axios from "axios";

const apiUrl = process.env.VUE_APP_API_URL;
const headers = {
    "Content-Type": "application/xml",
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
    "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
};
const state = {
    user: null,
    token: null,
    role: null,
};
const getters = {
    StateRole: (state) => state.role,
    isAuthenticated: (state) => !!state.user,
    StateUser: (state) => state.user,
    StateToken: (state) => state.token
};
const actions = {
    async Register({ dispatch }, form) {
        const response = await axios.post(`${apiUrl}/auth/register`, form, { headers });
        if (response.data.status === "success") {
            let UserForm = new FormData();
            UserForm.append("username", form.username);
            UserForm.append("password", form.password);
            UserForm.append("role", form.role);
            await dispatch("LogIn", UserForm);
        }
        return response.data;
    },

    async LogIn({ commit }, data) {
        const response = await axios.post(`${apiUrl}/auth/login`, data);
        if (!response.error) {
            await commit("setUser", response.data?.username || {});
            await commit("setRole", response.data?.role || "");
            await commit("setToken", response.data.token);
        }
        return response;
    },

    async LogOut({ commit }) {
        let username = null;
        localStorage.clear();
        commit("logout", username);
    },
};
const mutations = {
    setUser(state, user) {
        state.user = user;
    },
    setRole(state, role) {
        state.role = role;
    },
    setToken(state, token) {
        state.token = token;
        axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
    },
    logout(state, user) {
        state.user = user;
        state.token = null;
        axios.defaults.headers.common["Authorization"] = undefined;
    },
};
export default {
    state,
    getters,
    actions,
    mutations,
};
