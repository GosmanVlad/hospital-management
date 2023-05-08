import axios from "axios";

const apiUrl = process.env.VUE_APP_API_URL;

const state = {
    user: null,
    token: null,
    role: null,
    activated: false,
    userId: null,
    employeeId: null
};
const getters = {
    StateRole: (state) => state.role,
    isAuthenticated: (state) => !!state.user,
    StateUser: (state) => state.user,
    StateToken: (state) => state.token,
    StateUserId: (state) => state.userId,
    StateEmployeeId: (state) => state.employeeId,
    StateActivated: (state) => state.activated
};
const actions = {
    async Register({ dispatch }, form) {
        const response = await axios.post(`${apiUrl}/auth/register`, form);
        if (response.data.status === "success") {
            let UserForm = new FormData();
            UserForm.append("email", form.email);
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
            await commit("setActivated", response.data.activated);
            await commit("setUserId", response.data.user_id);
            await commit("setEmployeeId", response.data.employee_id);
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
    setUserId(state, userId) {
        state.userId = userId;
    },
    setEmployeeId(state, employeeId) {
        state.employeeId = employeeId;
    },
    setActivated(state, activated) {
        state.activated = activated;
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
