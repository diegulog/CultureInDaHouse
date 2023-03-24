<template>
  <v-main class="pa-100px">
    <v-container class="pa-0 d-flex justify-center">
      <v-card max-width="500" class="mx-auto">

        <v-toolbar dark>
          <v-btn icon @click="clearSearch"><v-icon>mdi-close</v-icon></v-btn>
          <v-text-field hide-details single-line label="Buscar evento" v-model="searchTerm"></v-text-field>
        </v-toolbar>

        <v-card-text class="py-0 mt-8" >
          <v-select single-line multiple label="Filtrar por categoría" class="d-inline" :items="categoryList"
          item-text="name" v-model="searchCat"></v-select>
        </v-card-text>

      </v-card>
    </v-container>
  </v-main>
</template>

<script>
  import Vue from 'vue'

  export default Vue.extend({
    name: 'SearchBar',
    props: {
      categoryList: {
        type: Array,
        required: true,
      },
    },
      data: () => ({
        searchTerm: '',
        searchCat: '',
        
      }),
    methods:{
      clearSearch(){
        this.searchTerm = "";
        this.searchCat = '';
      },
      filter(value){
        this.$emit('filter',value)
      },
      search(value){
        this.$emit('search',value);
      }
    },
    watch:{
      searchCat:function(value){
        this.filter(value)
      },
      searchTerm:function (value){
        this.search(value);
      }
    },
  })
</script>

<style scoped>
 .v-text-field{
      width: 400px;
}
</style>
