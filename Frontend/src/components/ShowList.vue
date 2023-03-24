<template>
  <v-component>
    <v-row class="my-10 px-5">
      <v-col cols="6" v-for="show in showsList" :key="show.id">
        <v-card class="pa-4"><ShowCard :show="show" :user="user" @update-shows="updateShows"></ShowCard>
        <v-overlay v-if="show.status=='CANCELLED'" absolute
                   color="#751a04"></v-overlay>
        </v-card>
      </v-col>
    </v-row>
  </v-component>
  </template>
  <script>
  import { defineComponent } from "vue";
  import ShowCard from "./ShowCard.vue";
  
  export default defineComponent({
    name: "ShowList",
    props: {
      user: {
        type: Object,
        required: true,
      },
      showsList: {
        type: Array,
        required: true,
      },
    },
    components: { ShowCard },
    methods:{
      updateShows(){
        this.$emit('update-shows');
      }
    }
  
  });
  </script>
  <style scoped>
  </style>