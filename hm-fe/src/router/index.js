import { createRouter, createWebHistory } from "vue-router";
import Homepage from "../pages/Homepage.vue";
import Register from "../pages/Register.vue";
import Activation from "../pages/Activation.vue";
import Appointments from "../pages/Appointments";
import Hospitalization from "../pages/Hospitalization";
import Departments from "../pages/Departments";
import Invoices from "../pages/Invoices";
import Login from "../pages/Login.vue";
import People from "../pages/People.vue";
import { appName } from "../consts"
import store from "../store";

const router = createRouter({
    mode: 'history',
    history: createWebHistory(),
    routes: [
        {
            path: "/",
            name: "home",
            component: Homepage,
            meta: {
                requiresAuth: true,
            },
        },
        {
            path: "/register",
            name: "register",
            component: Register,
        },
        {
            path: "/activation",
            name: "activation",
            component: Activation,
        },
        {
            path: "/login",
            name: "login",
            component: Login,
        },
        {
            path: "/appointments",
            name: "appointments",
            component: Appointments,
        },
        {
            path: "/hospitalization",
            name: "hospitalization",
            component: Hospitalization,
        },
        {
            path: "/departments",
            name: "departments",
            component: Departments,
        },
        {
            path: "/invoices",
            name: "invoices",
            component: Invoices,
        },
        {
            path: "/people",
            name: "people",
            component: People,
        },
    ],
});

router.beforeEach((to, from, next) => {
    document.title = appName;
    if (to.matched.some((record) => record.meta.requiresAuth)) {
        if (store.getters.isAuthenticated) {
            next();
            return;
        }
        next("/login");
    } else {
        next();
    }
});

router.beforeEach((to, from, next) => {
    if (to.matched.some((record) => record.meta.guest)) {
        if (store.getters.isAuthenticated) {
            next("/");
            return;
        }
        next();
    } else {
        next();
    }
});

export default router;
