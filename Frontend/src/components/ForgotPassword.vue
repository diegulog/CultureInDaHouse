<template>
    <v-container fluid fill-height>
             <v-layout align-center justify-center>
                <v-flex xs12 sm8 md4>
                   <v-card class="elevation-12">
                      <v-toolbar dark color="primary">
                         <v-toolbar-title>Contraseña olvidada</v-toolbar-title>
                      </v-toolbar>
                      <v-card-text>
                      <form ref="formForget" @submit.prevent="login()">
                             <v-text-field v-model="email"  name="email" label="Correo electrónico" type="text" required lazy-rules
                            :rules="[
                                (val) => (val && val.length > 0) || 'Introduzca correo electrónico',
                            ]" ></v-text-field>
                            <v-btn type="submit" class="mt-4" color="primary" value="log in">Restablecer contraseña</v-btn>
                       </form>
                      </v-card-text>
                   </v-card>
                 
                </v-flex>
             </v-layout>
          </v-container>
</template>

<script>
import Vue from 'vue'
import axios from 'axios';
 export default Vue.extend({
    name: "ForgotPassword", 
    data() {
     return {
       email: "",
     };
   },
   methods: {

    async handleSubmit ()
    {
        const response = await axios.post ('forgot', {
            email: this.email
        });
        console.log(response)
    }
   }
});

</script>

<style lang="css" scoped>
</style>