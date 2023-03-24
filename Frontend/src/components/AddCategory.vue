<template>
  <v-main class="pa-100px">
        
        <v-container class="pa-0 d-flex justify-center">
        <v-card max-width="600" class="mx-auto">
            <v-card>
                <v-card-title>
                    <span class="text-h5">Añadir categoría</span>
                </v-card-title>
            </v-card>
            <v-card-text>
                <v-container>
                    <v-form ref="form">
                      <v-row>
                        <v-col cols="12" sm="6" md="4" >
                          <v-text-field label="Nombre*" required :rules="stringRules" v-model="name"></v-text-field>
                        </v-col>
                        <v-col cols="12" sm="6" md="8" >
                          <v-text-field label="Descripción*" required :rules="stringRules" v-model="description"></v-text-field>
                        </v-col>                
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

export default Vue.extend({
    name: 'AddCategory',
    data: () => ({
        name: '',
        description: '',
        stringRules: [
            (v) => !!v || 'El campo no puede estar vacío',
        ],
    }),
    methods: {
        updateCategories(){
            this.$emit('update-categories');
        },
        create(){
            if(this.$refs.form!=null){
                if(!this.$refs.form.validate()){
                alert('Existen campos incorrectos');
            }else{
                let token = getToken();
                api.createCategories({category:{
                        id: Math.random() * 100,
                        name: this.name,
                        description: this.description,
                    }}, token).then(() => {
                        alert('La categoría ha sido añadido correctamente');
                        this.clear();
                        this.updateCategories();
                        }).catch((err)=>{
                            alert(err.response.data.message);
                    });
            
            }
        }
    },
    clear(){
        this.name = '';
        this.description = '';
    },
    },
});
</script>

<style scoped>
    .v-text-field {
        width: 400px;
    }
</style>