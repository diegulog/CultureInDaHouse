<template>
  <v-main class="pa-100px">
    <v-container class="pa-0 d-flex justify-center">
      <v-card max-width="600" class="mx-auto">
        <v-card>
          <v-card-title>
            <span class="text-h5">Añadir evento</span>
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
              </v-row>
              <v-row>
                <v-col cols="12" sm="6" md="6">
                  <v-text-field v-model="price" suffix="€" persistent-hint required label="Precio *"
                                :rules="numRules"></v-text-field>
                </v-col>

                <v-col cols="12" sm="6" md="6">
                  <v-text-field v-model="capacity" label="Capacidad *" :rules="numRules" persistent-hint
                                required></v-text-field>
                </v-col>
              </v-row>
              <v-row>
                <v-col cols="12" sm="6" md="6">
                  <v-text-field v-model="duration" label="Duración *" value="00:00" type="time"
                                :rules="durRules"></v-text-field>
                </v-col>

                <v-col cols="12" sm="6" md="4">
                  <v-menu v-model="menu" :close-on-content-click="false" :nudge-right="40" transition="scale-transition"
                          offset-y min-width="auto">
                    <template v-slot:activator="{ on, attrs }">
                      <v-text-field v-model="dateShow" label="Fecha" prepend-icon="mdi-calendar" readonly v-bind="attrs"
                                    v-on="on"></v-text-field>
                    </template>
                    <v-date-picker
                        v-model="dateShow"
                        @input="menu = false"
                    ></v-date-picker>
                  </v-menu>
                </v-col>
                

                <v-col cols="12" sm="6" md="6">
                  <v-select v-model="catShow" :items="categoryList" label="Categoría *" persistent-hint required
                            item-text="name" :rules="stringRules"></v-select>
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
          <v-btn class="mr-4"
                 @click="create">
            Crear
          </v-btn>
          <v-btn @click="clear">
            Borrar
          </v-btn>
        </v-card-text>
      </v-card>
    </v-container>

  </v-main>
</template>

<script>
import Vue from 'vue'
import {api} from "@/api";
import {getToken} from "@/utils";
import {firebaseStorage} from "@/firabase";
//import {api} from '@/api';
export default Vue.extend({
  name: 'AddEvent',
  props: {
    categoryList: {
      type: Array,
      required: true,
    },
  },
  data: () => ({

    valid: true,
    name: '',
    description: '',
    price: null,
    capacity: null,
    duration: null,
    dateShow: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
    catShow: null,
    image: '',
    menu: false,
    file: null,

    return: {
      pickerDate: null//(new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
    },
    stringRules: [
      v => !!v || 'El campo no puede estar vacío',
      v => v.length <= 255 || 'El texto no puede contener más de 255 caracteres',
    ],
    numRules: [
      v => !!v || 'El campo no puede estar vacío',
      v => !isNaN(v) || 'Introduce un número',
    ],
    durRules: [
      v => !!v || 'El campo no puede estar vacío',
      v => v != '00:00' || 'Debe tener una duración',
    ]
  }),

  methods: {
    updateShows() {
      this.$emit('update-shows');
    },
    onFileSelected(file) {
      console.log(file);
    },
    create() {
      firebaseStorage.saveImage(this.file).then((url) => {
        console.log(url);
        if (this.$refs.form != null) {
          if (!this.$refs.form.validate()) {
            alert('Existen campos incorrectos');
          } else {
            let id = null;
            for (let i = 0; i < this.categoryList.length; i++) {
              if (this.categoryList[i].name == this.catShow) {
                id = this.categoryList[i];
              }
            }
            var durMin = 0;
            if (this.duration != null) {
              durMin = parseInt(this.duration.substr(0, 2)) * 60 + parseInt(this.duration.substr(3, 5));
            }
            let token = getToken();
            api.createShows({
              show: {
                id: 0,
                name: this.name,
                description: this.description,
                image: url,
                price: this.price,
                duration: durMin,
                capacity: this.capacity,
                onSaleDate: this.dateShow,
                status: "CREATED",
                category: id,
              }
            }, token).then(() => {
              alert('El evento ha sido añadido correctamente');
              this.clear();
              this.updateShows();
            }).catch((err) => {
              if (err.response.status === 403) {
                alert("No tiene permisos para crear eventos");
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
      this.price = null;
      this.capacity = null;
      this.duration = null;
      this.dateShow = '';
      this.catShow = null;
      this.image = '';
    }
  },
});
</script>

<style scoped>
.v-text-field {
  width: 400px;
}
</style>
