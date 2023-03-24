<template>
  <v-container class="pa-0 d-flex justify-center">
    <v-card width="50%" class="mx-auto">

      <v-card>
        <v-btn icon class="close" @click="toggle" small color="teal"><v-icon small>mdi-close</v-icon></v-btn>

        <v-card-title>
          <span class="text-h5">Modificar empresa</span>
        </v-card-title>
      </v-card>

      <v-card-text>
        <v-container>
          <v-form ref="form">
            <v-row>
                <v-text-field v-model="name" :label="company.name" required :rules="stringRules"></v-text-field>
            </v-row>
            <v-row>
                <v-text-field v-model="description" :label="company.description" persistent-hint required :rules="stringRules"></v-text-field>
            </v-row>
            <v-row justify="center">
            </v-row>
            <v-row>
              <v-text-field v-model="image" :label="company.image" filled prepend-icon="mdi-camera" :rules="stringRules"></v-text-field>
            </v-row>
          </v-form>
        </v-container>
        <v-btn class="mr-4" @click="mod"> Modificar </v-btn>
        <v-btn @click="clear"> Borrar </v-btn>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
import Vue from 'vue'
import {api} from '@/api';
export default Vue.extend({
  name: 'ModCompany',
  props: {
    company: {
      type: Object,
      required: true,
    },
  },
  data: () => ({
    name: this.company.name,
    description: this.company.description,
    image: this.company.image,
    stringRules: [
      v => !!v || 'El campo no puede estar vacío',
      v => v.length <= 255 || 'El texto no puede contener más de 255 caracteres',
    ],
  }),

  methods: {
    toggle(){
      this.$emit('closeModCompany');
    },
    mod(){
      if(!this.name){
        this.name = this.company.name;
      }
      if(!this.description){
        this.description = this.company.description;
      }
      if(!this.image){
        this.image = this.company.image;
      }
      
      api.modCompany(this.company.id, {company:{
          name: this.name,
          description: this.description,
          image: this.image,
        }}).then(() => {
        alert('Los datos de la empresa se han modificado correctamente');
        location.reload();
      });
    },
    clear(){
      this.name = '';
      this.description= '';
      this.image = '';
    }
  },
});
</script>

<style scoped>
.close{
  position: absolute;
  top: 0px;
  right: 0;
}
</style>
