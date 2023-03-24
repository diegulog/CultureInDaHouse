<template>
  <v-container class="pa-0 d-flex justify-center">
    <v-card width="50%" class="mx-auto">
      <v-card>
        <v-btn icon class="close" @click="toggle" small color="teal">
          <v-icon small>mdi-close</v-icon>
        </v-btn>
        <v-card-title>
          <span class="text-h5">Añadir empresa</span>
        </v-card-title>
      </v-card>
      <v-card-text>
        <v-container>
          <v-form ref="form">
            <v-row>
              <v-col cols="12" sm="6" md="6">
                <v-text-field v-model="name" label="Nombre *" required :rules="stringRules"></v-text-field>
              </v-col>
              <v-col cols="12" sm="6" md="6">
                <v-text-field v-model="description" label="Descripción *" persistent-hint required
                              :rules="stringRules"></v-text-field>
              </v-col>
              <v-col cols="6">
                <v-select ref="seleccionado" class="pa-0" v-model="userToAssign[company.id]" :items="usersList"
                          label="Asignar gestor"
                          persistent-hint required item-text="firstName" ></v-select>
                <!--                <v-select ref="seleccionado" label="Asignar gestor">-->
                <!--                  <option v-for="elemento in usersList" :key="elemento.username">{{elemento.id}}</option>-->
                <!--                </v-select>-->
              </v-col>
            </v-row>
            <v-row justify="center">

            </v-row>
            <v-row>
              <v-file-input
                  accept="image/*"
                  filled prepend-icon="mdi-camera"
                  v-model="file"
                  type="file"
                  label="Imagen"
                  outlined
                  autocomplete="off"
                  @change="onFileSelected"
              ></v-file-input>
            </v-row>
          </v-form>
        </v-container>
        <v-btn class="mr-4" @click="create"> Crear</v-btn>
        <v-btn @click="clear"> Borrar</v-btn>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
import Vue from 'vue'
import {api} from "@/api";
import {firebaseStorage} from "@/firabase";
//import {api} from '@/api';
export default Vue.extend({
  name: 'AddCompany',
  props: {
    usersList: {
      type: Array,
      required: true,
    },
    categoryList: {
      type: Array,
      required: true,
    },
  },
  data: () => ({
    userToAssign: Array,
    company: Object,
    valid: true,
    name: '',
    description: '',
    image: '',
    menu: false,
    stringRules: [
      v => !!v || 'El campo no puede estar vacío',
      v => v.length <= 255 || 'El texto no puede contener más de 255 caracteres',
    ],
  }),
  methods: {
    toggle() {
      this.$emit('toggleAddCompany');
    },
    updateShows() {
      this.$emit('update-shows');
    },
    onFileSelected(file) {
      console.log(file);
    },
    create() {

      firebaseStorage.saveImage(this.file).then((url) => {
        if (this.$refs.form != null) {
        if (!this.$refs.form.validate()) {
          this.seleccionado = this.$refs.seleccionado.value;
          alert('Existen campos incorrectos');
        } else {
          api.createCompany({
            company: {
              name: this.name,
              description: this.description,
              image: url,
            }
          }).then((res) => {
            let user = null;
            for (const element of this.usersList) {
              if (element.firstName === this.$refs.seleccionado.value) {
                user = element;
                break;
              }
            }
            let companyId = res.data;
            api.getCompany(companyId).then((res) => {
              let companyToMod = res.data;
              api.modCompany(companyToMod.id, {
                company: {
                  name: companyToMod.name,
                  description: companyToMod.description,
                  image: companyToMod.image,
                  managers: [user],
                }
              })
            })

            alert('La empresa ha sido creada correctamente');
            location.reload();
          }).catch((err) => {
            if (err.response.status === 403) {
              alert("No tiene permisos para crear empresas");
            } else {
              alert(err.response.data.message);
            }
          });
        }
      }


    });
    },
    clear() {
      this.name = '';
      this.description = '';
      this.image = '';
    }
  },
});
</script>

<style scoped>
.close {
  position: absolute;
  top: 0px;
  right: 0;
}

.v-text-field {
  width: 400px;
}
</style>
