<template>
  <v-component>
    <v-row v-if="user.role==0">
      <v-spacer></v-spacer>
      <v-btn v-if="!showAddCompany" small class="mr-5 mt-5" @click="toggleAddCompany" color="light-blue" fab>+</v-btn>
    </v-row>
    
    <v-row class="my-10 px-5">
      <v-col cols="6" v-for="company in companyList" :key="company.id">
        
        <v-card class="pa-4">
          <v-col v-if="company.managers[0] != null" > 
               <h3 class="font-weight-bold text-center" >Empresa gestionada por: {{company.managers[0].firstName}}</h3>
            </v-col>
          <v-row>
            <v-col cols="3">
              <v-btn small class="delete" @click="del(company.id)" plain>
                <v-icon small>mdi-delete</v-icon>
              </v-btn>
              <v-btn small class="modify" plain @click="openModCompany(company.id)">
                <v-icon small>mdi-puzzle-edit</v-icon>
              </v-btn>
            </v-col>
            <v-col v-if="user.role==0" cols="6">
              <v-select class="pa-0" v-model="userToAssign[company.id]" :items="usersList" label="Asignar gestor"
                        persistent-hint required item-text="firstName" :rules="stringRules"></v-select>
            </v-col>
            
            <v-col v-if="user.role==0" cols="2">
              <v-btn @click="assign(company.id)" small fab>
                <v-icon small>mdi-account-check</v-icon>
              </v-btn>
            </v-col>
          </v-row>
          <h2 class="font-weight-bold text-center">{{ company.name }}</h2>
          <div class="show-image">
            <v-img class="my-5" max-height="20rem" :src="company.image"></v-img>
          </div>
          <v-row>
            <v-col>
              <span>{{ company.description }}</span>
            </v-col>
          </v-row>
        </v-card>
      </v-col>
    </v-row>
    <AddCompany @toggleAddCompany="toggleAddCompany" :usersList="usersList" class="fix"
                v-if="showAddCompany"></AddCompany>
    <ModCompany @closeModCompany="closeModCompany" :company="company" class="fix" v-if="showModCompany"></ModCompany>
  </v-component>


</template>

<script>
import {defineComponent} from "vue";
import AddCompany from "@/components/AddCompany";
import ModCompany from "@/components/modCompany";
import {api} from "@/api";

export default defineComponent({
  name: "ShowList",
  props: {
    user: {
      type: Object,
      required: true,
    },
    usersList: {
      type: Array,
      required: true,
    },
    companyList: {
      type: Array,
      required: true,
    }
  },
  data: () => ({
    userToAssign: Array,
    company: Object,
    showAddCompany: false,
    showModCompany: false,
  }),
  components: {ModCompany, AddCompany},
  methods: {
    assign(companyId) {
      console.log(companyId);
      console.log(this.userToAssign[companyId]);
      let user = null;
      for (const element of this.usersList) {
        if (element.firstName == this.userToAssign[companyId]) {
          user = element;
        }
      }
      console.log(user)
      api.getCompany(companyId).then((res) => {
        let companyToMod = res.data;
        console.log(companyToMod)
        //{name: companyToMod.name, description: companyToMod.description, image: companyToMod.image, managers: [{user}]}
        api.modCompany(companyToMod.id, {
          company: {
            name: companyToMod.name,
            description: companyToMod.description,
            image: companyToMod.image,
            managers: [user],
          }
        }).then(() => {
          alert('Los datos de la empresa se han modificado correctamente');
          location.reload();
        }).catch((err) => {
          alert(err.response.data.message);
        });
      }).catch((err) => {
        alert(err.response.data.message);
      });


    },
    del(id) {
      api.deleteCompany(id).then(() => {
        alert("La empresa ha sido eliminada de la base de datos");
        location.reload();
      }).catch((err) => {
        alert(err.response.data.message);
      });
    },
    openModCompany(id) {
      api.getCompany(id).then((res) => {
        this.company = res.data;
      });
      this.showModCompany = !this.showModCompany;
    },
    closeModCompany() {
      this.showModCompany = !this.showModCompany;
    },
    toggleAddCompany() {
      this.showAddCompany = !this.showAddCompany;
    }
  },
});
</script>
<style scoped>

.delete {
  position: absolute;
  top: 0px;
  left: -10px;
}

.modify {
  position: absolute;
  top: 0px;
  left: 20px;
}

.fix {
  position: fixed;
  top: 200px;
  left: 10px;
}
</style>