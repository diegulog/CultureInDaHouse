<template>
  <v-app >
    <v-main>
      <v-container fluid fill-height>
        <v-layout align-center justify-center>
          <v-flex xs12 sm8 md4>
            <v-card class="elevation-12">
              <v-toolbar dark color="primary">
                <v-toolbar-title>Registrarse</v-toolbar-title>
              </v-toolbar>
              <v-card-text>
                <v-container>
                  <v-form ref="registerForm" @submit.prevent="registerAdd()" >
                    <v-row>
                      <v-col cols="20" sm="12" md="12">
                        <v-text-field v-model="firstName" label="Nombre *" required :rules="stringRules" lazy-rules></v-text-field>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col cols="12" sm="12" md="12">
                        <v-text-field v-model="lastName" label="Apellidos *" required :rules="stringRules" lazy-rules></v-text-field>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col cols="12" sm="12" md="12">
                        <v-text-field v-model="username" :rules="[emailRules.email, stringRules]"  label="Email *"  hint="example@email.com" required lazy-rules></v-text-field>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col cols="12" sm="12" md="12">
                        <v-text-field v-model="password" required :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'" :rules="[passwordRules.required, passwordRules.min, passwordRules.containsUpperCase, passwordRules.containsLowerCase, passwordRules.containsNumber, passwordRules.containsSpecial]"
                            :type="showPassword ? 'text' : 'password'" name="input-10-1" label="Contraseña" @click:append="showPassword = !showPassword" lazy-rules></v-text-field>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col cols="12" sm="12" md="12">
                        <v-text-field v-model="repeatedPassword" required :append-icon="showRepeatedPassword ? 'mdi-eye' : 'mdi-eye-off'" :rules="[passwordRules.required, passwordMatch]" :type="showRepeatedPassword ? 'text' : 'password'" name="input-10-1" label="Repite Contraseña" @click:append="showRepeatedPassword = !showRepeatedPassword" lazy-rules></v-text-field>
                      </v-col>
                    </v-row>
                    <v-row justify="center">
                    </v-row>
                  </v-form>
                </v-container>
                <v-btn class="mt-4" color="primary" :disabled="!valid" type="submit" @click="register">Registrarse</v-btn>
                <v-btn class="mt-4" @click="clear"> Limpiar </v-btn>
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
import {api} from "@/api";

export default Vue.extend({
  // eslint-disable-next-line vue/multi-word-component-names
    name: 'Register',
    data: () => ({
        valid: true,
        firstName: '',
        lastName: '',
        password: '',
        username: '',
        repeatedPassword: '',

        showPassword: false,
        showRepeatedPassword: false,

        passwordRules: {
          required: value => !!value || 'Este campo no puede estar vacío.',
          min: v => v.length >= 8 || 'Mínimo 8 caracteres',
          containsUpperCase: value => {
            const pattern = /[A-Z]/
            return pattern.test(value) || 'Debe contener al menos una Mayúscula'
          },
          containsLowerCase: value => {
            const pattern = /[a-z]/
            return pattern.test(value) || 'Debe contener al menos una minúscula'
          },
          containsNumber: value => {
            const pattern = /[0-9]/
            return pattern.test(value) || 'Debe contener al menos un número'
          },
          containsSpecial: value => {
            const pattern = /[#?!@$%^&*\-+]/
            return pattern.test(value) || 'Debe contener al menos un carácter especial'
          },
        },
        emailRules: {
          email: value => {
            const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
            return pattern.test(value) || 'E-mail no válido.'
          }
        },
        stringRules: [
            v => !!v || 'El campo no puede estar vacío',
            v => v.length <= 255 || 'El texto no puede contener más de 255 caracteres',
        ],
    }),
    computed: {
        passwordMatch() {
            return () => this.password === this.repeatedPassword || "Las contraseñas no coinciden";
        }
    },
    methods: {
        register(){
            if(this.$refs.registerForm!=null){
                if(!this.$refs.registerForm.validate()){
                  alert('Existen campos incorrectos');
                } else {

                      let dateToday = Date.now();
                      api.createUser( {user:{
                          id: 0,
                          username: this.username,
                          password: this.password,
                          firstName: this.firstName,
                          lastName: this.lastName,
                          lastActive: dateToday,
                          createTime: dateToday,
                          active: true,
                        }})
                          .then((response) => {
                            if(response.data === ''){
                              alert("El email pertenece a otro usuario, por favor introduzca uno diferente.")
                            } else {
                              alert('Te has registrado correctamente');
                              this.$emit('login-on');
                            }
                          })
                          .catch((error)=>{
                            alert(error.response.data.message);
                          });

                }
            }
        },
        clear(){
            this.firstName = '';
            this.lastName = '';
            this.username = '';
            this.password=  '';
            this.repeatedPassword= '';
        },
    },
});
</script>