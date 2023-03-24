<template>
  <v-component>


    <h2 class="font-weight-bold text-center">{{ show.name }}</h2>

    <div class="show-image">
      <v-img class="my-5" max-height="20rem" :src="show.image"></v-img>
    </div>

    <v-row>
      <v-col cols="8">
        <span class="label">Descripción:</span>
        <span>{{ show.description }}</span>
      </v-col>
      <v-col cols="4">
        <span class="label">Categoría:</span>
        <span>{{ show.category.name }}</span>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <span class="label">Duration:</span>
        <span>{{ show.duration }} min</span>
      </v-col>
      <v-col>
        <span class="label">Precio:</span>
        <span>{{ show.price }}€</span>
      </v-col>
      <v-col>
        <span class="label">Fecha:</span>
        <span>{{ show.onSaleDate }}</span>
      </v-col>
      <v-col v-if="user.role ===0 || user.role ===1">
        <span class="label">Estado:</span>
        <span>{{ show.status }}</span>
      </v-col>
    </v-row>
    <v-row v-if="user.role!==3">
      <v-col>
      </v-col>
      <v-col>
      </v-col>
      <v-col>
        <v-select v-if="user.role ===0 || user.role ===1" class="pa-0" :items="statusList" label="Estado"
                  @change="update"></v-select>
      </v-col>
    </v-row>

  </v-component>

</template>

<script>
import {defineComponent} from "vue";
import {api} from "@/api";

export default defineComponent({
  name: "ShowCard",

  props: {
    user: {
      type: Object,
      required: true,
    },
    show: {
      type: Object,
      required: true,
    },
  },
  data: () => ({
    statusList: ["CREATED", "OPEN", "CANCELLED"]
  }),
  methods: {
    updateShows() {
      this.$emit('update-shows');
    },
    del() {
      api.deleteShow(this.show.id)
          .then(() => {
            alert("El evento se ha cancelado");
            this.updateShows();
          }).catch((err) => {
        alert(err.response.data.message);
      });
    },
    update(selectStatus) {
      api.update(this.show.id, {
        show: {
          id: 0,
          name: this.show.name,
          description: this.show.description,
          image: this.show.image,
          price: this.show.price,
          duration: this.show.duration,
          capacity: this.show.capacity,
          onSaleDate: this.show.onSaleDate,
          status: selectStatus,
          category: this.show.category,
        }
      }).then(() => {
        alert("El evento se ha modificado");
        this.updateShows();
      }).catch((err) => {
        alert(err.response.data.message);
      });
    }
  },
});
</script>

<style scoped>


.label {
  font-weight: bold;
  margin-right: 5px;
}

.delete {
  position: absolute;
  top: 0px;
  left: -15px;
}
</style>
  