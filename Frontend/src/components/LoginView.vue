<template>
   <v-app >
      <v-main>
         <v-container fluid fill-height>
            <v-layout align-center justify-center>
               <v-flex xs12 sm8 md4>
                  <v-card class="elevation-12">
                     <v-toolbar dark color="primary">
                        <v-toolbar-title>Iniciar sesión</v-toolbar-title>
                     </v-toolbar>
                     <v-card-text>
                     <form ref="form" @submit.prevent="loginAdd()">
                            <v-text-field v-model="username" name="username" label="Correo electrónico" type="text" required lazy-rules :rules="[(val) => (val && val.length > 0) || 'Introduzca correo electrónico',]"></v-text-field>
                           
                            <v-text-field v-model="password" name="password" label="Contraseña" type="password"  required lazy-rules :rules="[(val) =>(val !== null && val !== '') || 'Introduzca contraseña',]" ></v-text-field>
                          <div v-if="error" color="orange">
                             Usuario no registrado. Compruebe email y contraseña.
                          </div>
                          <div v-if="success" id="success">
                              Inicio de sesión correcto.
                          </div>
                           <v-card-actions>
                             <v-btn type="submit" class="mt-4" color="primary" value="log in">Iniciar sesión</v-btn>
                             <v-col class="text-right">
                                ¿No tienes cuenta? <a href="#" class="pl-2" @click="register" >Registrate</a>
                             </v-col>
                           </v-card-actions>
                      </form>
                     </v-card-text>
                  </v-card>
               </v-flex>
            </v-layout>
         </v-container>
      </v-main>
   </v-app>
</template>
 
<script>
 import Vue from 'vue'
 import {saveToken, removeToken} from "@/utils";
 import {api} from "@/api";

 export default Vue.extend({
   name: "LoginView",
   components: {
   },
   data() {
     return {
      username: "",
      password: "",
      error: null,
      success: false
     };
   },
   methods: {
     register(){
       this.$emit('register-on');
     },
    clear(){
       this.email="";
       this.password = "";
    },
   async loginAdd() {
      this.success = false;
      this.error = null;
      let email = this.username;
      let pass = this.password;

      api.login({user:{
         username: email,
         password: pass
         }}).then(response => {
         removeToken();
         saveToken(response.headers.authorization);
         this.$emit('login', response);
         /*api.getUser(email).then(response => {
         console.log(response);

         });*/
         }
      ).catch(()=>{
         this.clear();
         alert("Las credenciales no son correctas");
      });
   }
   },
 });
</script>
