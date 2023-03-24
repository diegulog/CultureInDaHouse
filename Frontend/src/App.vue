<template>
  <v-app>
    <v-app-bar z-index="2" app color="primary" dark justify="space-between">
      <v-toolbar-title>
        <span class="orange--text">Culture</span><span class="font-weight-light">InDaHouse</span>
      </v-toolbar-title>

      <v-spacer></v-spacer>
      <h4 justify="center" v-if="isLogged"><span class="font-weight-light" >Bienvenido</span> <span class="orange--text">{{user.firstName}} {{user.lastName}}</span></h4>

      <v-spacer></v-spacer>

      <v-btn text color="orange" plain small v-on:click="home">Inicio</v-btn>
      <v-btn text color="orange" plain small v-if="user.role == 0" v-on:click="addCategoryOn">Añadir categoría</v-btn>
      <v-btn text color="orange" small plain v-if="user.role == 0 || user.role == 1" v-on:click="addEventOn">Añadir evento</v-btn>
      <v-btn text color="orange" plain small v-if="user.role == 0 || user.role == 1" v-on:click="gestionarEmpresaOn">Gestionar empresa</v-btn>

      <v-btn text color="orange" plain small v-if= "!isLogged" v-on:click="loginOn"><v-icon> mdi-account-circle-outline</v-icon></v-btn>
      <v-btn color="orange" small plain v-if="isLogged" v-on:click="logout"><v-icon> mdi-logout</v-icon></v-btn>
    </v-app-bar>

  <v-main>
    <v-component v-if="showSearch">
        <v-img src="https://en.wikipedia.org/wiki/Cats_(musical)#/media/File:CatsMusicalLogo.jpg"></v-img>
        <SearchBar @filter="filter" @search="setSearchTerm" :categoryList="categoryList"/>
        <ShowList :showsList="list" :user="user" @update-shows="updateShows"/>
      </v-component>
      <v-component v-if="showAddEvent">
        <AddEvent :categoryList="categoryList" @update-shows="updateShows"></AddEvent>
      </v-component>
      <v-component v-if="showAddCategory">
        <AddCategory @update-categories="updateCategories"></AddCategory>
      </v-component>
    <v-component v-if="showGestionarEmpresa">
      <ListCompanies :user="user" :companyList="companyList" :usersList="usersList"></ListCompanies>
    </v-component>
      <v-component v-if="showLogin">
        <LoginView @login="login" @register-on="registerOn"/>
      </v-component>
      <v-component v-if="showRegister">
      <Register @home="home" @login-on="loginOn"></Register>
      </v-component>
  </v-main>
  </v-app>
</template>


<script>
import {removeToken} from '@/utils';
import Vue from 'vue';
import ListCompanies from "@/components/ListCompanies";
import SearchBar from './components/SearchBar.vue';
import ShowList from './components/ShowList.vue';
import AddEvent from './components/AddEvent.vue'
import AddCategory from './components/AddCategory.vue';

import LoginView from './components/LoginView.vue'
import Register from './components/Register.vue';
import {api} from "@/api";
import {getToken} from "@/utils";


/* TODO esto es una función y no se exactamente dónde tiene que ir, la he dejado aquí porque aquí funciona pero no se exactamente si es aquí, aunque luego de darle un rato al coco creo que sí
function searchCookie(nameCookie) {
  let listCookies = document.cookie.split(";");
  if (listCookies == -1 || listCookies == null || listCookies ==""){
    return false
  }
  let miCookie;

  for (let i in listCookies) {
    let search = listCookies[i].search(nameCookie);
    if (search > -1) {
      miCookie = listCookies[i]
    }
  }
  let positionEqual = miCookie.indexOf("=");
  return miCookie.substring(positionEqual+1);
}*/

export default Vue.extend({
  name: 'App',

  components: {
    LoginView,
    SearchBar,
    ShowList,
    AddEvent,
    AddCategory,
    ListCompanies,
    Register
  },


  data: () => ({
    usersList: [],
    showRegister: false,
    showAddCategory: false,
    showGestionarEmpresa: false,
    showAddEvent: false,
    showSearch: true,
    showLogin: false,
    showLogOutButton: false,
    showLoginButton: false,
    categoryList: [],
    showsList: [],
    searchTerm: "",
    searchCat: "",
    user:{
      id: 0,
      username: '',
      firstName: '',
      lastName: '',
      role: 5,
    },
    companyList: [],
  }),

  methods: {
    logout(){
      removeToken();
      alert("Has cerrado sesión");
      this.clear();
      this.home();
      location.reload();
    },
    clear(){
      this.user = {
        id: 0,
        username: '',
        firstName: '',
        lastName: '',
        role: 5,
      };
    },
    login(){
      api.getUserMe().then(res => {
        this.user.id = res.data.id;
        this.user.username = res.data.username;
        this.user.firstName = res.data.firstName;
        this.user.lastName = res.data.lastName;
        this.user.role = res.data.roles[0].id;
        location.reload();
        
      });

      //
    },
    showsListFiltered(valueo, valuet) {
      let list = [];

      for (let i = 0; i < this.showsList.length; i++) {
        if (this.showsList[i].name.toLowerCase().includes(valueo.toLowerCase())) {
          if (valuet.length == 0) {
            list.push(this.showsList[i]);
          }
          for (let j = 0; j < valuet.length; j++) {
            if (this.showsList[i].category.name == valuet[j]) {
              list.push(this.showsList[i]);
            }
          }
        }
      }
      return list;
    },
    setSearchTerm(searchText) {
      this.searchTerm = searchText;
    },
    filter(cat) {
      this.searchCat = cat;
    },
    updateShows() {
      api.getShows()
          .then((response) => {
            this.showsList = response.data;
          }).catch((err)=>{
             console.error(err);
           });
      this.home();
    },
    updateCategories(){
        api.getCategories()
            .then((response) => {
                this.categoryList = response.data;
            }).catch((err)=>{
               console.error(err);
            });
        this.home();
    },
    addEventOn() {
      this.showAddCategory = false;
      this.showGestionarEmpresa = false;
      this.showAddEvent = true;
      this.showSearch = false;
      this.showLogin = false;
      this.showRegister = false;

    },
    addCategoryOn() {
      this.showAddCategory = true;
      this.showGestionarEmpresa = false;
      this.showAddEvent = false;
      this.showSearch = false;
      this.showLogin = false;
      this.showRegister = false;
    },
    gestionarEmpresaOn() {
      this.showAddCategory = false;
      this.showGestionarEmpresa = true;
      this.showAddEvent = false;
      this.showSearch = false;
      this.showLogin = false;
      this.showRegister = false;
    },
    home() {
      this.showAddCategory = false;
      this.showGestionarEmpresa = false;
      this.showAddEvent = false;
      this.showSearch = true;
      this.showLogin = false;
      this.showRegister = false;
    },
    loginOn() {
      this.showAddCategory = false;
      this.showGestionarEmpresa = false;
      this.showAddEvent = false;
      this.showSearch = false;
      this.showLogin = true;
      this.showRegister = false;
    },
    registerOn(){
      this.showAddCategory = false;
      this.showGestionarEmpresa = false;
      this.showAddEvent = false;
      this.showSearch = false;
      this.showLogin = false;
      this.showRegister = true;
    },
  },
  mounted() {
    api.getUsersList().then((response)=>{
      this.usersList = response.data;
    });
    api.getCompanies().then((response) => {
    this.companyList = response.data;
    });
    api.getUserMe().then(res => {
      this.user.id = res.data.id;
      this.user.username = res.data.username;
      this.user.firstName = res.data.firstName;
      this.user.lastName = res.data.lastName;
      this.user.role = res.data.roles[0].id;
      if(this.user.role==1){
        console.log("psss")
        api.getCompanyFromUser(this.user.id).then(res=>{
          console.log("psss")
          this.companyList = res.data;
          console.log(this.companyList)
    });
      }
    });
    api.getCategories().then((response) => {
      this.categoryList = response.data;
    });
    api.getShows().then((response) => {
      this.showsList = response.data;
    });
    
  },
  computed: {
    isLogged(){
      return getToken() != null;
    },
    list() {
      if (this.searchTerm.length == 0 && this.searchCat.length == 0) {
        return this.showsList;
      } else {
        return this.showsListFiltered(this.searchTerm, this.searchCat);
      }
    }
  }

});
</script>