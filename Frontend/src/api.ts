import axios from 'axios';
import {ICategory, IShow} from "@/interfaces";
import {getToken} from "@/utils";

const env = process.env.VUE_APP_ENV;

if (env === 'production') {
    axios.defaults.baseURL = `http://${process.env.VUE_APP_DOMAIN_SHOW_CATALOG_PROD}`;
} else {
    axios.defaults.baseURL = `http://${process.env.VUE_APP_DOMAIN_SHOW_CATALOG_DEV}`;
}

function authHeaders() {
    return {
        headers: {
            Authorization: getToken(),
        },
    };
}

export const api = {
    async getCompanyFromUser(id: number){
        return axios.get(`/users/${id}/companies`, authHeaders());
    },
    async getUserMe(){
    return axios.get("/users/me", authHeaders());
    },
    async getUser(email: string){
        return axios.get("/username/"+email);
    },
    async getUsers(){
        return axios.get("/users", authHeaders());
    },
    async getUsersList(){
        return axios.get("/users/byRole/2" , authHeaders());
    },
    async login(user: {user:{username: string, password: string}},token?: string){
        const params = new URLSearchParams();
        params.append('username', user.user.username);
        params.append('password', user.user.password);
        return axios.post('/login', params, authHeaders());
    },
    //End points Shows
    async createShows(show: { show: { duration: number, image: string, price: null, name: string, description: string, id: number, onSaleDate: string, category: null, capacity: null, status: string } }) {
        return axios.post('/shows', show, authHeaders());
    },

    async update(id:number, show: { show: { duration: number, image: string, price: null, name: string, description: string, id: number, onSaleDate: string, category: null, capacity: null, status: string } }) {
        return axios.put(`/shows/${id}`, show, authHeaders());
    },
    async createCompany(company: { company: { name: string, description: string, image: string}}) {
        return axios.post('/companies', company, authHeaders());
    },
    async modCompany(id: number, company: { company: {name: string, description: string, image: string, managers: Array<Object>}}){
        return axios.put(`/companies/${id}`, company, authHeaders());
    },
    async getShows(name?: string, categoryId?: number) {
        const params = new URLSearchParams();
        if (name) {
            params.append('name', name);
        }
        if (categoryId) {
            params.append('categoryId', categoryId.toString());
        }
        return axios.get<IShow[]>('/shows', {params:params, headers: {Authorization: getToken()}});
    },
    async getCompanies() {
        return axios.get('/companies', authHeaders());
    },
    async getShow(id: number) {
        return axios.get<IShow>(`/shows/${id}`);
    },
    async deleteShow(id: number) {
        return axios.delete<boolean>(`/shows/${id}`, authHeaders());
    },
    async deleteCompany(id: number) {
        return axios.delete<boolean>(`/companies/${id}`, authHeaders());
    },
    async getCompany(id: number) {
        return axios.get(`/companies/${id}`, authHeaders());
    },
    
    //End points Categories
    async createCategories(category: { category: { name: string, description: string, id: number } }, token?: string) {
        return axios.post('/categories', category, authHeaders());
    },
    async getCategories() {
        return axios.get<ICategory[]>('/categories');
    },
    async getCategory(id: number) {
        return axios.get<ICategory>(`/categories/${id}`);
    },
    //End points User
    async createUser(user: { user: { firstName: string, lastName: string, password: string, lastActive: number, createTime: number, active: boolean, id: number, username: string } }, token?: string) {
        // @ts-ignore
        return axios.post('/users', user, null);
    },
    async findUserByUsername(username: string) {
        return axios.get('/users/${username}');
    }


}